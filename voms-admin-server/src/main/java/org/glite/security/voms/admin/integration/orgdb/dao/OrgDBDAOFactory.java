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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Defines all DAOs and the concrete factories to get the conrecte DAOs.
 * <p>
 * To get a concrete DAOFactory, call <tt>instance()</tt> with one of the
 * classes that extend this factory.
 * <p>
 * Implementation: If you write a new DAO, this class has to know about it. If
 * you add a new persistence mechanism, add an additional concrete factory for
 * it as a constant, like <tt>HIBERNATE</tt>.
 * 
 * @author Christian Bauer
 */
public abstract class OrgDBDAOFactory {

  private static Logger log = LoggerFactory.getLogger(OrgDBDAOFactory.class);

  /**
   * Creates a standalone DAOFactory that returns unmanaged DAO beans for use in
   * any environment Hibernate has been configured for. Uses
   * HibernateUtil/SessionFactory and Hibernate context propagation
   * (CurrentSessionContext), thread-bound or transaction-bound, and transaction
   * scoped.
   */
  public static final Class HIBERNATE = OrgDBHibernateDAOFactory.class;

  /**
   * Factory method for instantiation of concrete factories.
   */
  public static OrgDBDAOFactory instance(Class factory) {

    try {
      // log.debug("Creating concrete DAO factory: " + factory);
      return (OrgDBDAOFactory) factory.newInstance();
    } catch (Exception ex) {
      throw new RuntimeException("Couldn't create DAOFactory: " + factory);
    }
  }

  public static OrgDBDAOFactory instance() {

    return instance(HIBERNATE);
  }

  public abstract OrgDBVOMSPersonDAO getVOMSPersonDAO();

}
