/**
 * Copyright (c) Istituto Nazionale di Fisica Nucleare (INFN). 2006-2015
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
package org.glite.security.voms.admin.integration.orgdb.dao;

import org.glite.security.voms.admin.integration.orgdb.database.OrgDBError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Returns Hibernate-specific instances of DAOs.
 * <p/>
 * If for a particular DAO there is no additional non-CRUD functionality, we use
 * a nested static class to implement the interface in a generic way. This
 * allows clean refactoring later on, should the interface implement business
 * data access methods at some later time. Then, we would externalize the
 * implementation into its own first-level class.
 * 
 * @author Christian Bauer
 * @author Andrea Ceccanti
 */
public class OrgDBHibernateDAOFactory extends OrgDBDAOFactory {

  private static Logger log = LoggerFactory
    .getLogger(OrgDBHibernateDAOFactory.class);

  @SuppressWarnings("unchecked")
  private OrgDBGenericHibernateDAO instantiateDAO(Class daoClass) {

    try {
      // log.debug("Instantiating DAO: " + daoClass);
      return (OrgDBGenericHibernateDAO) daoClass.newInstance();

    } catch (Exception e) {
      log.error("Can not instantiate DAO: {}. Cause: {}", daoClass,
        e.getMessage());
      log.error(e.getMessage(), e);
      throw new OrgDBError(e.getMessage(), e);

    }
  }

  @Override
  public OrgDBVOMSPersonDAO getVOMSPersonDAO() {

    return (OrgDBVOMSPersonDAO) instantiateDAO(OrgDBVOMSPersonDAOHibernate.class);
  }

}
