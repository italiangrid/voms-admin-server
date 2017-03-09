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
package org.glite.security.voms.admin.persistence.dao;

import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.glite.security.voms.admin.configuration.VOMSConfiguration;
import org.glite.security.voms.admin.configuration.VOMSConfigurationConstants;
import org.glite.security.voms.admin.core.VOMSServiceConstants;
import org.glite.security.voms.admin.error.NullArgumentException;
import org.glite.security.voms.admin.persistence.HibernateFactory;
import org.glite.security.voms.admin.persistence.model.VOMSCA;
import org.glite.security.voms.admin.util.DNUtil;

public class VOMSCADAO implements Searchable {

  public static final Logger log = LoggerFactory.getLogger(VOMSCADAO.class);

  private VOMSCADAO() {

    HibernateFactory.beginTransaction();
  }

  public boolean createIfMissing(String caDN, String description) {

    VOMSCA ca = getByName(caDN);

    if (ca == null) {
      createCA(caDN, description);
      return true;
    }

    log.debug(caDN + " is already in the trusted CA database.");
    return false;
  }

  public VOMSCA createCA(String caDN, String description) {

    if (caDN == null)
      throw new NullArgumentException("caDN must be non-null!");

    log.info("Adding  '" + caDN + "' to trusted CA database.");

    VOMSCA ca = new VOMSCA(caDN, description);

    HibernateFactory.getSession().save(ca);

    return ca;
  }

  public VOMSCA create(X509Certificate caCert, String description) {

    assert caCert != null : "CA certificate is null!";
    Date now = new Date();

    if (now.after(caCert.getNotAfter())
      && (!VOMSConfiguration.instance().getBoolean(
        VOMSConfigurationConstants.CREATE_EXPIRED_CAS, false))) {

      log.warn("CA '"
        + DNUtil.getOpenSSLSubject(caCert.getSubjectX500Principal())
        + "' certificate has expired on " + caCert.getNotAfter()
        + " so it will not be added to the voms database!");
      return null;

    }

    VOMSCA ca = new VOMSCA(caCert, description);
    HibernateFactory.getSession().save(ca);
    return ca;

  }

  public void saveOrUpdateTrustedCA(X509Certificate caCert) {

    saveOrUpdateTrustedCA(caCert, null);
  }

  public void checkValidityAndUpdate(VOMSCA ca, X509Certificate caCert) {

    Date now = new Date();

    // if ( now.after( ca.getNotAfter() ) ) {
    //
    // LOG.warn( "CA '" + ca.getSubjectString()
    // + "' has expired! [notAfter=" + ca.getNotAfter() + "]" );
    //
    // // The CA has expired, let's see if the new certificate extends its
    // // validity
    // if ( !now.after( caCert.getNotAfter() ) ) {
    // LOG.info( "Updating validity period for CA '"
    // + ca.getSubjectString() + "' from '" + ca.getNotAfter()
    // + "' to '" + caCert.getNotAfter() + "'." );
    // ca.update( caCert );
    // HibernateFactory.getSession().update( ca );
    //
    // } else {
    //
    // if ( VOMSConfiguration.instance().getBoolean(
    // VOMSConfiguration.DROP_EXPIRED_CAS, false ) ) {
    //
    // // In this case the CA has expired and there is no
    // // substitute certificate
    // // We check if there are certificates bound to this ca, and,
    // // if not, drop
    // // the ca from the database.
    // List <Certificate> certs = CertificateDAO.instance()
    // .getForCA( ca );
    // if ( certs.isEmpty() ) {
    //
    // LOG.warn( "Removing expired CA '"
    // + ca.getSubjectString() + "' from database..." );
    // // First remove the admins
    // VOMSAdminDAO.instance().deleteFromCA( ca );
    // HibernateFactory.getSession().delete( ca );
    // }else
    // LOG.warn("Expired ca '"+ca+"' not removed: user certificates issued by this ca are found in database. Remove such certificates first.");
    //
    // }
    // }

  }

  public void saveOrUpdateTrustedCA(X509Certificate caCert, String description) {

    assert caCert != null : "CA certificate is null!";

    VOMSCA ca = getFromCertificate(caCert);

    if (ca == null) {

      ca = create(caCert, description);
      if (ca != null)
        log.debug("Added [ " + ca.getSubjectString()
          + "] to trusted CA database.");

    }

  }

  public void saveTrustedCA(String caDN) {

    VOMSCA ca = getByName(caDN);

    if (ca == null) {

      log.debug("Adding [ " + caDN + "] to trusted CA database.");
      createCA(caDN, null);

    } else
      log.debug(caDN + " is already in trusted CA database.");

  }

  public VOMSCA getByName(String caDN) {

    if (caDN == null)
      throw new NullArgumentException("caDN must be non-null!");

    String queryString = "from VOMSCA as ca where ca.subjectString = :caDN";

    VOMSCA res = (VOMSCA) HibernateFactory.getSession()
      .createQuery(queryString).setString("caDN", caDN).uniqueResult();

    return res;

  }

  public VOMSCA getByID(Short caID) {

    return (VOMSCA) HibernateFactory.getSession().get(VOMSCA.class, caID);

  }

  public VOMSCA getFromCertificate(X509Certificate cert) {

    VOMSCA model = new VOMSCA(cert, null);

    String query = "from VOMSCA as ca where ca.subjectString = :modelCA";
    VOMSCA result = (VOMSCA) HibernateFactory.getSession().createQuery(query)
      .setString("modelCA", model.getSubjectString()).uniqueResult();

    return result;
  }

  public VOMSCA getByID(short caID) {

    return getByID(new Short(caID));

  }

  public List getAll() {

    String query = "from VOMSCA";

    List res = HibernateFactory.getSession().createQuery(query).list();

    return res;
  }

  public List<VOMSCA> getValid() {

    String query = "from VOMSCA where subjectString not like '/O=VOMS%' order by subjectString";

    List res = HibernateFactory.getSession().createQuery(query).list();

    return res;
  }

  public VOMSCA getGroupCA() {

    return getByName(VOMSServiceConstants.GROUP_CA);
  }

  public VOMSCA getRoleCA() {

    return getByName(VOMSServiceConstants.ROLE_CA);
  }

  public static VOMSCADAO instance() {

    return new VOMSCADAO();
  }

  public int countMatches(String text) {

    if (text == null || "".equals(text.trim()))
      return getValid().size();

    String searchString = "%" + text + "%";

    String query = "select count(distinct ca) from VOMSCA ca where ca.subjectString not like '/O=VOMS%' and ca.subjectString like :searchString";

    int count = ((Long) HibernateFactory.getSession().createQuery(query)
      .setString("searchString", searchString).uniqueResult()).intValue();

    return count;
  }

  public SearchResults getAll(int firstResult, int maxResults) {

    String query = "from VOMSCA where subjectString not like '/O=VOMS%'";

    int count = getValid().size();
    List cas = HibernateFactory.getSession().createQuery(query)
      .setFirstResult(firstResult).setMaxResults(maxResults).list();

    SearchResults results = SearchResults.instance();
    results.setCount(count);
    results.setFirstResult(firstResult);
    results.setResultsPerPage(maxResults);
    results.setResults(cas);
    results.setSearchString(null);

    return results;
  }

  public SearchResults search(String text, int firstResult, int maxResults) {

    if ((text == null || "".equals(text.trim()) || text.length() == 0))
      return getAll(firstResult, maxResults);

    String searchString = "%" + text + "%";
    String query = "select ca from VOMSCA ca where ca.subjectString not like '/O=VOMS%' and ca.subjectString like :searchString";

    List cas = HibernateFactory.getSession().createQuery(query)
      .setString("searchString", searchString).setFirstResult(firstResult)
      .setMaxResults(maxResults).list();

    SearchResults results = SearchResults.instance();

    results.setCount(countMatches(text));
    results.setFirstResult(firstResult);
    results.setResultsPerPage(maxResults);
    results.setResults(cas);
    results.setSearchString(text);

    return results;

  }

}
