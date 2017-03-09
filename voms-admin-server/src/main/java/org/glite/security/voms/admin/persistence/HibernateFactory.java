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
package org.glite.security.voms.admin.persistence;

import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Properties;

import org.apache.commons.lang.Validate;
import org.glite.security.voms.admin.error.VOMSFatalException;
import org.glite.security.voms.admin.persistence.error.VOMSDatabaseException;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HibernateFactory {

  private static final Logger log = LoggerFactory
    .getLogger(HibernateFactory.class);

  private static SessionFactory sessionFactory;

  private static final ThreadLocal<Session> threadSession = new ThreadLocal<Session>();

  private static final ThreadLocal<Transaction> threadTransaction = new ThreadLocal<Transaction>();

  
  public static synchronized void initialize(Configuration configuration) {

    Validate.notNull(configuration);

    if (sessionFactory != null) {
      throw new VOMSDatabaseException(
        "Hibernate session factory already initialized!");
    }

    try {

      sessionFactory = configuration.buildSessionFactory();

    } catch (HibernateException e) {

      log.error("Hibernate session factory creation failed!", e);
      throw new ExceptionInInitializerError(e);

    }

  }
  
  public static synchronized void initialize(Properties databaseProperties) {

    Validate.notNull(databaseProperties);

    if (sessionFactory != null) {
      throw new VOMSDatabaseException(
        "Hibernate session factory already initialized!");
    }

    try {

      Configuration hibernateConf = new AnnotationConfiguration();
      hibernateConf.addProperties(databaseProperties);

      sessionFactory = hibernateConf.configure().buildSessionFactory();

    } catch (HibernateException e) {

      log.error("Hibernate session factory creation failed!", e);
      throw new ExceptionInInitializerError(e);

    }

  }

  public static synchronized void shutdown() {

    if (sessionFactory != null)
      sessionFactory.close();

    unregisterSQLDrivers();
    cleanupThreadLocals();

  }

  protected static void unregisterSQLDrivers() {

    Enumeration<Driver> drivers = DriverManager.getDrivers();

    while (drivers.hasMoreElements()) {
      Driver driver = drivers.nextElement();
      try {

        DriverManager.deregisterDriver(driver);

      } catch (SQLException e) {
        log.warn("Error deregistering driver {}", driver, e);
      }

    }

  }

  protected static void cleanupThreadLocals() {

    if (threadSession != null) {
      threadSession.remove();
    }

    if (threadTransaction != null) {

      threadTransaction.remove();
    }

  }

  public static SessionFactory getFactory() {

    return sessionFactory;
  }

  public static Session getSession() {

    Session s = (Session) threadSession.get();
    try {

      if (s == null) {

        s = sessionFactory.openSession();
        threadSession.set(s);

        log.debug("Opened new session for thread {}. Session: {}", 
          Thread.currentThread(), s);
      }

    } catch (HibernateException ex) {

      log.error("Error getting hibernate session!", ex);
      throw new VOMSFatalException(ex.getMessage(), ex);
    }
    return s;
  }

  public static void closeSession() {

    try {

      Session s = (Session) threadSession.get();
      threadSession.set(null);

      if (s != null) {
        if (s.isOpen()){
          log.debug("Closing session for thread {}", Thread.currentThread());
          s.close();
        } else {
          log.debug("Session is already closed for thread {}", 
            Thread.currentThread());
        }
      } else  {
        log.debug("Attempted to close a null session for thread {}", 
          Thread.currentThread());
      }

    } catch (HibernateException ex) {

      log.error("Error closing hibernate session!", ex);
      throw new VOMSDatabaseException(ex.getMessage(), ex);
    }
  }

  public static void beginTransaction() {

    Transaction tx = (Transaction) threadTransaction.get();
    try {

      if (tx == null) {
        tx = getSession().beginTransaction();
        threadTransaction.set(tx);
        
        if (log.isDebugEnabled()){
          log.debug("Started transation {} for thread {}. ", tx, 
            Thread.currentThread());
        }
      }
    } catch (HibernateException ex) {

      log.error("Error creating hibernate transaction!", ex);
      throw new VOMSDatabaseException(ex.getMessage(), ex);

    }
  }

  public static void commitTransaction() {

    Transaction tx = (Transaction) threadTransaction.get();
    try {

      if (tx != null && !tx.wasCommitted() && !tx.wasRolledBack()){
        if (log.isDebugEnabled()){
          log.debug("Committing transaction {} for thread {}", tx,
            Thread.currentThread());
        }
        tx.commit();
        
      }
      
      threadTransaction.set(null);
    } catch (HibernateException ex) {
      log.error("Error committing hibernate transaction:" + ex.getMessage());
      
      rollbackTransaction();
     
      if (log.isDebugEnabled()){
        log.error("Error committing hibernate transaction!", ex);
      }
      throw new VOMSDatabaseException(ex.getMessage(), ex);
    }
  }

  public static void rollbackTransaction() {

    Transaction tx = (Transaction) threadTransaction.get();
    try {

      threadTransaction.set(null);
      if (tx != null && !tx.wasCommitted() && !tx.wasRolledBack()){
        if (log.isDebugEnabled()){
          log.debug("Rolling back transaction {} for thread {}", tx, 
            Thread.currentThread());
        }
        tx.rollback();
      }
    } catch (HibernateException ex) {
      log.error("Error rolling back hibernate transaction:" + ex.getMessage());

      if (log.isDebugEnabled()){
        log.error("Error rolling back hibernate transaction!", ex);
      }

      throw new VOMSDatabaseException(ex.getMessage(), ex);

    } finally {
      closeSession();
    }

  }
}
