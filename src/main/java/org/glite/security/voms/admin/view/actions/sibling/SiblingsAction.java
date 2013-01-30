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
package org.glite.security.voms.admin.view.actions.sibling;

import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.interceptor.ApplicationAware;
import org.apache.struts2.util.ServletContextAware;
import org.glite.security.voms.admin.core.VOMSService;
import org.glite.security.voms.admin.util.AdminServiceContactInfo;
import org.glite.security.voms.admin.view.actions.BaseAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opensymphony.xwork2.Preparable;


@Result(name=BaseAction.SUCCESS, location="siblings")
public class SiblingsAction extends BaseAction implements Preparable, ApplicationAware{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static Logger log = LoggerFactory.getLogger(SiblingsAction.class);
	
	private List<AdminServiceContactInfo> endpoints;

	public void prepare() throws Exception {
		
	}


	/**
	 * @return the endpoints
	 */
	public synchronized List<AdminServiceContactInfo> getEndpoints() {
		return endpoints;
	}

	/**
	 * @param endpoints the endpoints to set
	 */
	public synchronized void setEndpoints(List<AdminServiceContactInfo> endpoints) {
		this.endpoints = endpoints;
	}


	@SuppressWarnings("unchecked")
	@Override
	public void setApplication(Map<String, Object> application) {
		endpoints = (List<AdminServiceContactInfo>) application.get(VOMSService.ENDPOINTS_KEY);
	}
	
}
