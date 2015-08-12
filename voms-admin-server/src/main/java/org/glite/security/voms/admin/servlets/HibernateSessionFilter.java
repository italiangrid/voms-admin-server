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
package org.glite.security.voms.admin.servlets;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.glite.security.voms.admin.persistence.HibernateFactory;

public class HibernateSessionFilter implements Filter {

  private static final Logger log = LoggerFactory
    .getLogger(HibernateSessionFilter.class);

  public void init(FilterConfig arg0) throws ServletException {

    log.debug("Initializing HibernateSessionFilter {}", this);
  }

  public void doFilter(ServletRequest req, ServletResponse res,
    FilterChain chain) throws IOException, ServletException {

    chain.doFilter(req, res);

    try {
      log.debug("Executing HibernateSessionFilter {}", this);
      HibernateFactory.commitTransaction();

    } finally {

      HibernateFactory.closeSession();

    }

  }

  public void destroy() {

    log.debug("Destroying HibernateSessionFilter {}", this);
  }

}
