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
package org.glite.security.voms.admin.integration.orgdb.servlet;

import java.io.IOException;

import javax.persistence.PersistenceException;
import javax.persistence.RollbackException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.glite.security.voms.admin.error.VOMSFatalException;
import org.glite.security.voms.admin.integration.orgdb.database.OrgDBSessionFactory;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OrgDbHibernateSessionFilter implements Filter {

  private static final Logger LOG = LoggerFactory
    .getLogger(OrgDbHibernateSessionFilter.class);
  
  
  @Override
  public void init(FilterConfig filterConfig) throws ServletException {
  
  }

  @Override
  public void doFilter(ServletRequest request, ServletResponse response,
    FilterChain chain) throws IOException, ServletException {

    chain.doFilter(request, response);
    
    SessionFactory sf = OrgDBSessionFactory.getSessionFactory();

    try {

      if (sf == null) {
        throw new VOMSFatalException("OrgDBSessionFactory not initialized!");
      }

      if (sf.getCurrentSession() == null) {
        LOG.debug("No session opened with CERN HR db.");
        return;
      }

      if (sf.getCurrentSession()
        .getTransaction() == null) {
        LOG.debug("No transaction opened with CERN HR db.");
        return;
      }

      if (!sf.getCurrentSession()
        .getTransaction()
        .isActive()) {
        LOG.debug("Current CERN HR db transaction is not active");
        return;
      }

      sf.getCurrentSession()
        .getTransaction()
        .commit();

    } catch (RollbackException e) {
      LOG.error("Error committing CERN HR db transaction: " + e.getMessage(),
        e);

      try {
        sf.getCurrentSession()
          .getTransaction()
          .rollback();
      } catch (PersistenceException pe) {
        LOG.error(
          "Error rolling back CERN HR db transaction: " + pe.getMessage(), pe);
      }

    } catch (Throwable t) {
      LOG.error(t.getMessage(), t);

    } finally {
      
      if (sf.getCurrentSession() != null) {
        try {
          sf.getCurrentSession()
            .close();
        } catch (HibernateException e) {
          LOG.error("Error closing CERN HR db session: " + e.getMessage(), e);
        }
      }
    }
  }

  @Override
  public void destroy() {
  }

}
