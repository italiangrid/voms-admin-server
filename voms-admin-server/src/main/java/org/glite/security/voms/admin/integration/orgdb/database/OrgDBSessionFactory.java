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
package org.glite.security.voms.admin.integration.orgdb.database;

import java.util.Properties;

import javax.persistence.PersistenceException;
import javax.persistence.RollbackException;

import org.glite.security.voms.admin.integration.orgdb.model.Country;
import org.glite.security.voms.admin.integration.orgdb.model.Experiment;
import org.glite.security.voms.admin.integration.orgdb.model.Institute;
import org.glite.security.voms.admin.integration.orgdb.model.InstituteAddress;
import org.glite.security.voms.admin.integration.orgdb.model.Participation;
import org.glite.security.voms.admin.integration.orgdb.model.VOMSOrgDBPerson;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.context.internal.ThreadLocalSessionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OrgDBSessionFactory {

  static final Logger log = LoggerFactory.getLogger(OrgDBSessionFactory.class);

  private static volatile SessionFactory orgDbSessionFactory;

  public static Configuration buildConfiguration(
    Properties orgDbHibernateProperties) {

    Configuration cfg = new Configuration()
      .addAnnotatedClass(Country.class).addAnnotatedClass(Experiment.class)
      .addAnnotatedClass(Institute.class)
      .addAnnotatedClass(InstituteAddress.class)
      .addAnnotatedClass(Participation.class)
      .addAnnotatedClass(VOMSOrgDBPerson.class)
      .setProperties(orgDbHibernateProperties);

    // Hardwired configuration properties
    Properties p = new Properties();
    p.setProperty("hibernate.current_session_context_class",
      ThreadLocalSessionContext.class.getName());

    log.debug("Hardwired configuration properties: {}", p);

    cfg.addProperties(p);

    return cfg;

  }

  public synchronized static void initialize(Properties orgbHibernateProperties) {

    if (orgDbSessionFactory != null)
      throw new OrgDBError("Session factory already initialized!");

    try {

      Configuration cfg = buildConfiguration(orgbHibernateProperties);

      orgDbSessionFactory = cfg.configure().buildSessionFactory();

    } catch (HibernateException e) {

      String errorMsg = String.format(
        "Cannot initialize OrgDB database connection: %s", e.getMessage());
      log.error(errorMsg, e);
      throw new OrgDBError(errorMsg, e);

    }
  }

  public static SessionFactory getSessionFactory() {

    if (orgDbSessionFactory == null)
      throw new OrgDBError("Session factory not initialized!");

    return orgDbSessionFactory;
  }
  
  public static void rollbackTransaction() {
    if (orgDbSessionFactory == null)
      throw new OrgDBError("Session factory not initialized!");
    
    Session s = orgDbSessionFactory.getSessionFactory().getCurrentSession();
    
    if (!s.isConnected()){
      throw new OrgDBError("Session to OrgDB is not connected");
    }
    
    rollbackTransaction(s.getTransaction());
  }
  
  private static void rollbackTransaction(Transaction tx) {
    if (tx == null){
      throw new OrgDBError("Cannot rollback a null transaction");
    }
    
    try {
      tx.rollback();
    } catch(PersistenceException e){
      log.error("Error rolling back transaction: "+e.getMessage(),e);
    }
  }

  
  public static void commitTransaction() {
    if (orgDbSessionFactory == null)
      throw new OrgDBError("Session factory not initialized!");
    
    Session s = orgDbSessionFactory.getSessionFactory().getCurrentSession();
    
    if (!s.isConnected()){
      throw new OrgDBError("Session to OrgDB is not connected");
    }
    
    Transaction tx = s.getTransaction();
    
    if (tx == null){
      throw new OrgDBError("Cannot commit a null transaction");
    }
    
    if (!tx.isActive()){
      throw new OrgDBError("Cannot commit an inactive transaction");
    }
    
    try{
      tx.commit();
    } catch(RollbackException e){
     log.error("Error committing OrgDB transaction: "+e.getMessage(), e);
     tx.markRollbackOnly();
     rollbackTransaction();
    }
    
  }
  
  
  
  public static void shutdown() {

    if (getSessionFactory() != null)
      getSessionFactory().close();

  }

  private OrgDBSessionFactory() {

  }
}
