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
package org.glite.security.voms.admin.view.interceptors;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.StrutsStatics;
import org.glite.security.voms.admin.common.VOMSServiceConstants;
import org.glite.security.voms.admin.common.InitSecurityContext;
import org.glite.security.voms.admin.common.VOMSConfiguration;
import org.glite.security.voms.admin.operations.CurrentAdmin;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

public class AuthenticatedAccessInterceptor extends AbstractInterceptor
		implements StrutsStatics {

	/**
     * 
     */
	private static final long serialVersionUID = 1L;

	private static final Log log = LogFactory
			.getLog(AuthenticatedAccessInterceptor.class);
	
	
	public void destroy() {

		// TODO Auto-generated method stub

	}

	public void init() {

		
		

	}

	public String intercept(ActionInvocation ai) throws Exception {
		
		

		
		HttpServletRequest req = ServletActionContext.getRequest(); 
		
		InitSecurityContext.setContextFromRequest(req);
		
		if (!VOMSConfiguration.instance().getVOName().equals("siblings")){
			req.setAttribute("voName", VOMSConfiguration.instance().getVOName());
			req.setAttribute(VOMSServiceConstants.CURRENT_ADMIN_KEY, CurrentAdmin
				.instance());
			req.setAttribute("registrationEnabled", VOMSConfiguration.instance().getBoolean(
					VOMSConfiguration.REGISTRATION_SERVICE_ENABLED, true));
			
		}
		

		return ai.invoke();
	}

	

	
	
	
}
