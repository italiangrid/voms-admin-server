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
package org.glite.security.voms.admin.servlets;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.glite.security.voms.admin.configuration.VOMSConfiguration;
import org.glite.security.voms.admin.core.VOMSServiceConstants;
import org.glite.security.voms.admin.operations.CurrentAdmin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author andrea
 * 
 */
public class SecurityContextFilter implements Filter {

  protected Logger log = LoggerFactory.getLogger(SecurityContextFilter.class);

  private String voName;

  public void init(FilterConfig arg0) throws ServletException {

    log.debug("Initializing SecurityContextFilter {}", this);

    voName = VOMSConfiguration.instance()
      .getVOName();
  }

  protected void initContext(HttpServletRequest request) {

    InitSecurityContext.setContextFromRequest(request);
    InitSecurityContext.logConnection();

  }

  protected void initWebappProperties(HttpServletRequest request) {

    request.setAttribute("voName", voName);
    request.setAttribute(VOMSServiceConstants.CURRENT_ADMIN_KEY,
      CurrentAdmin.instance());

    request.setAttribute(VOMSServiceConstants.CURRENT_ADMIN_VO_USER_KEY,
      CurrentAdmin.instance()
        .getVoUser());

  }

  public void doFilter(ServletRequest req, ServletResponse res,
    FilterChain chain) throws IOException, ServletException {

    log.debug("Executing SecurityContextFilter {}", this);

    HttpServletRequest request = (HttpServletRequest) req;

    initContext(request);
    initWebappProperties(request);

    try{
      chain.doFilter(req, res);
    }finally {
      InitSecurityContext.clearContext();
    }
  }

  public void destroy() {

    log.debug("Destroying SecurityContextFilter {}", this);
  }

}
