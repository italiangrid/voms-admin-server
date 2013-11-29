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
package org.glite.security.voms.admin.servlets;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.glite.security.voms.admin.configuration.VOMSConfiguration;
import org.glite.security.voms.admin.core.VOMSServiceConstants;
import org.glite.security.voms.admin.operations.CurrentAdmin;
import org.italiangrid.utils.voms.CurrentSecurityContext;
import org.italiangrid.utils.voms.VOMSSecurityContext;
import org.italiangrid.voms.VOMSValidators;
import org.italiangrid.voms.ac.VOMSACValidator;
import org.italiangrid.voms.ac.VOMSValidationResult;
import org.italiangrid.voms.ac.ValidationResultListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author andrea
 * 
 */
public class SecurityContextFilter implements Filter, ValidationResultListener {

	public static final String SECURITY_CONTEXT_SESSION_KEY = "voms-admin-security-context";
	public static final int SESSION_LIFETIME_IN_SECONDS = 120;
	
	protected Logger log = LoggerFactory.getLogger(SecurityContextFilter.class);

	private VOMSACValidator validator;
	
	public void init(FilterConfig arg0) throws ServletException {

		log.debug("Initializing SecurityContextFilter {}", this);
		validator = VOMSValidators.newValidator(this);
	}

	protected void initContext(HttpServletRequest request){
		
		HttpSession s = request.getSession(true);
		s.setMaxInactiveInterval(SESSION_LIFETIME_IN_SECONDS);
		
		VOMSSecurityContext sc = (VOMSSecurityContext) 
			s.getAttribute(SECURITY_CONTEXT_SESSION_KEY);
		
		if (sc == null){
			InitSecurityContext.setContextFromRequest(request, validator);
			s.setAttribute(SECURITY_CONTEXT_SESSION_KEY, 
				CurrentSecurityContext.get());
			InitSecurityContext.logConnection();
		}else
			CurrentSecurityContext.set(sc);
		
	}
	
	protected void initWebappProperties(HttpServletRequest request){
		String voName = VOMSConfiguration.instance().getVOName();
		request.setAttribute("voName", voName);
		request.setAttribute(VOMSServiceConstants.CURRENT_ADMIN_KEY, CurrentAdmin
				.instance());
	}
	
	public void doFilter(ServletRequest req, ServletResponse res,
			FilterChain chain) throws IOException, ServletException {

		log.debug("Executing SecurityContextFilter {}", this);
		
		HttpServletRequest request=  (HttpServletRequest)req;
		
		initContext(request);
		initWebappProperties(request);
		
		chain.doFilter(req, res);
	}

	public void destroy() {
		log.debug("Destroying SecurityContextFilter {}", this);
	}

	@Override
	public void notifyValidationResult(VOMSValidationResult result) {
		if (!result.isValid())
			log.warn("VOMS attributes validation result: {}", result);
		else
			log.debug("VOMS attributes validation result: {}", result);
	}

}
