/**
 * Copyright (c) Members of the EGEE Collaboration. 2006-2009.
 * See http://www.eu-egee.org/partners/ for details on the copyright holders.
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
 *
 * Authors:
 * 	Andrea Ceccanti (INFN)
 */
package org.glite.security.voms.admin.persistence.dao;

import org.glite.security.voms.admin.persistence.HibernateFactory;
import org.glite.security.voms.admin.persistence.SchemaVersion;
import org.glite.security.voms.admin.persistence.model.VOMSDBVersion;

public class VOMSVersionDAO {

  private VOMSVersionDAO() {

  }

  public static VOMSVersionDAO instance() {

    return new VOMSVersionDAO();
  }

  public VOMSDBVersion createCurrentVersion() {

    VOMSDBVersion v = new VOMSDBVersion();
    v.setVersion(SchemaVersion.VOMS_DB_VERSION);
    v.setAdminVersion(SchemaVersion.VOMS_ADMIN_DB_VERSION);

    return v;
  }

  public void setupVersion() {

    VOMSDBVersion v = getVersion();

    if (v == null) {

      v = createCurrentVersion();
      HibernateFactory.getSession().save(v);

    } else {

      if (v.getVersion() != SchemaVersion.VOMS_DB_VERSION) {
        HibernateFactory.getSession().delete(v);
        HibernateFactory.getSession().save(createCurrentVersion());
      }
    }

  }

  public VOMSDBVersion getVersion() {

    return (VOMSDBVersion) HibernateFactory.getSession()
      .createQuery("from VOMSDBVersion").uniqueResult();

  }
}
