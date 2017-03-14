/**
 * Copyright (c) Istituto Nazionale di Fisica Nucleare (INFN). 2006-2016
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.glite.security.voms.admin.persistence.dao.hibernate;

import java.net.URL;
import java.util.Date;
import java.util.List;

import org.glite.security.voms.admin.error.NullArgumentException;
import org.glite.security.voms.admin.error.VOMSException;
import org.glite.security.voms.admin.persistence.dao.generic.AUPDAO;
import org.glite.security.voms.admin.persistence.error.AlreadyExistsException;
import org.glite.security.voms.admin.persistence.error.NoSuchAUPVersionException;
import org.glite.security.voms.admin.persistence.model.AUP;
import org.glite.security.voms.admin.persistence.model.AUPAcceptanceRecord;
import org.glite.security.voms.admin.persistence.model.AUPVersion;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

public class AUPDAOHibernate extends NamedEntityHibernateDAO<AUP, Long>
  implements AUPDAO {

  protected AUP newAUP(String name, String description) {

    if (name == null)
      throw new NullArgumentException("name cannot be null!");

    if (findByName(name) != null)
      throw new AlreadyExistsException("AUP named '" + name
        + "' already exists!");

    AUP aup = new AUP();
    aup.setName(name);
    aup.setDescription(description);

    // Default reacceptance period is 365 days
    aup.setReacceptancePeriod(365);

    return aup;

  }

  protected AUP createAUP(String name, String description, String version,
    String text) {

    if (version == null)
      throw new NullArgumentException("version cannot be null!");

    if (text == null)
      throw new NullArgumentException("text cannot be null!");

    AUP aup = newAUP(name, description);
    AUPVersion firstVersion = newAUPVersion(aup, version);

    firstVersion.setText(text);
    firstVersion.setActive(true);
    aup.getVersions().add(firstVersion);

    makePersistent(aup);

    return aup;

  }

  protected AUP createAUP(String name, String description, String version,
    URL url) {

    if (version == null)
      throw new NullArgumentException("version cannot be null!");

    if (url == null)
      throw new NullArgumentException("url cannot be null!");

    AUP aup = newAUP(name, description);

    AUPVersion firstVersion = newAUPVersion(aup, version);
    firstVersion.setUrl(url.toString());
    firstVersion.setActive(true);
    aup.getVersions().add(firstVersion);

    makePersistent(aup);

    return aup;

  }

  protected AUPVersion newAUPVersion(AUP aup, String version) {

    if (aup == null)
      throw new NullArgumentException("aup cannot be null!");

    AUPVersion aupVersion = aup.getVersion(version);

    if (aupVersion != null)
      throw new AlreadyExistsException("Version '" + version
        + "' already exists for AUP named '" + aup.getName() + "'.");

    if (version == null)
      throw new NullArgumentException("version cannot be null!");

    aupVersion = new AUPVersion();

    aupVersion.setVersion(version);
    aupVersion.setCreationTime(new Date());
    aupVersion.setAup(aup);

    return aupVersion;

  }

  public AUPVersion addVersion(AUP aup, String version, URL url) {

    AUPVersion v = newAUPVersion(aup, version);

    v.setUrl(url.toString());
    aup.getVersions().add(v);
    
    return v;

  }

  public AUPVersion addVersion(AUP aup, String version, String text) {

    AUPVersion v = newAUPVersion(aup, version);
    v.setText(text);
    aup.getVersions().add(v);
    
    return v;
  }

  public AUPVersion removeVersion(AUP aup, String version) {

    if (aup == null)
      throw new NullArgumentException("aup cannot be null!");

    AUPVersion aupVersion = aup.getVersion(version);

    if (aupVersion == null)
      throw new NoSuchAUPVersionException("No AUP version found for version '"
        + version + "'.");

    if (aupVersion.getActive())
      throw new VOMSException(
        "The currently active aup version cannot be removed!");

    dropAcceptanceRecordsForAUPVersion(aupVersion);
    aup.getVersions().remove(aupVersion);

    return aupVersion;
  }

  public AUP getGridAUP() {

    return findByName(AUP.GRID_AUP_NAME);
  }

  public AUP getVOAUP() {

    return findByName(AUP.VO_AUP_NAME);
  }

  public AUP createGridAUP(String description, String version, URL url) {

    return createAUP(AUP.GRID_AUP_NAME, description, version, url);
  }

  public AUP createGridAUP(String description, String version, String text) {

    return createAUP(AUP.GRID_AUP_NAME, description, version, text);
  }

  public AUP createVOAUP(String description, String version, URL url) {

    return createAUP(AUP.VO_AUP_NAME, description, version, url);
  }

  public AUP createVOAUP(String description, String version, String text) {

    return createAUP(AUP.VO_AUP_NAME, description, version, text);
  }

  public AUPVersion setActiveVersion(AUP aup, String version) {

    if (aup == null)
      throw new NullArgumentException("aup cannot be null!");

    if (version == null)
      throw new NullArgumentException("version cannot be null!");

    AUPVersion v = aup.getVersion(version);

    if (v == null)
      throw new NoSuchAUPVersionException("Aup version '" + version
        + "' not defined for AUP '" + aup.getName() + "'.");

    aup.setActiveVersion(v);
    
    return v;
  }

  private void dropAcceptanceRecordsForAUPVersion(AUPVersion aupVersion) {

    Criteria crit = getSession().createCriteria(AUPAcceptanceRecord.class).add(
      Restrictions.eq("aupVersion", aupVersion));
    List<AUPAcceptanceRecord> records = crit.list();

    for (AUPAcceptanceRecord r : records) {

      r.getUser().getAupAcceptanceRecords().remove(r);
      r.setUser(null);
      r.setAupVersion(null);

      getSession().delete(r);
    }

  }
}
