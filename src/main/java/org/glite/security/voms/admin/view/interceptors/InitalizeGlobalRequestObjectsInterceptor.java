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

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.StrutsStatics;
import org.glite.security.voms.admin.configuration.VOMSConfiguration;
import org.glite.security.voms.admin.configuration.VOMSConfigurationConstants;
import org.glite.security.voms.admin.persistence.dao.generic.DAOFactory;
import org.glite.security.voms.admin.persistence.model.AUP;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

public class InitalizeGlobalRequestObjectsInterceptor extends AbstractInterceptor
		implements StrutsStatics {

	/**
     * 
     */
	private static final long serialVersionUID = 1L;
	
	protected void setupGlobalObjects(HttpServletRequest req){
		
		if (!VOMSConfiguration.instance().getVOName().equals("siblings")){
			
			AUP aup = DAOFactory.instance().getAUPDAO().getVOAUP();
			
			req.setAttribute("registrationEnabled", VOMSConfiguration.instance().getBoolean(
					VOMSConfigurationConstants.REGISTRATION_SERVICE_ENABLED, true));
			
			req.setAttribute("readOnlyPI", 
					VOMSConfiguration.instance().getBoolean(VOMSConfigurationConstants.VOMS_INTERNAL_RO_PERSONAL_INFORMATION,false));
			
			req.setAttribute("readOnlyMembershipExpiration", 
					VOMSConfiguration.instance().getBoolean(VOMSConfigurationConstants.VOMS_INTERNAL_RO_MEMBERSHIP_EXPIRATION_DATE,false));
			
			req.setAttribute("disableMembershipEndTime", 
					VOMSConfiguration.instance().getBoolean(VOMSConfigurationConstants.DISABLE_MEMBERSHIP_END_TIME, false));
			
			req.setAttribute("defaultAUP", aup);
			
		}
		
	}

	public String intercept(ActionInvocation ai) throws Exception {
		
		HttpServletRequest req = ServletActionContext.getRequest(); 
		
		setupGlobalObjects(req);

		return ai.invoke();
	}

	

	
	
	
}
