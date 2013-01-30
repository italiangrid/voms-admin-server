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

package org.glite.security.voms.admin.servlets.vomses;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.glite.security.voms.admin.error.VOMSException;
import org.glite.security.voms.admin.servlets.VOMSContextListener;
import org.glite.security.voms.admin.util.AdminServiceContactInfo;
import org.glite.security.voms.admin.util.AdminServiceContactUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VOMSESContextListener implements ServletContextListener {

	static final Logger log = LoggerFactory.getLogger(VOMSContextListener.class);
	
	private static final String CONFDIR_KEY = "confdir";
	public static final String VERSION_KEY = "version";
	public static final String ENDPOINTS_KEY = "endpoints";
	

	private void initVersion(ServletContextEvent sce){
		
		InputStream versionPropStream = this.getClass().getClassLoader()
				.getResourceAsStream("version.properties");
		
		Properties versionProps = new Properties();
		
		try {
			versionProps.load(versionPropStream);
		
		} catch (IOException e) {
			log.error("Error loading version properties: "+e.getMessage(),e);
			throw new VOMSException(e);
		}
		
		sce.getServletContext().setAttribute(VERSION_KEY, versionProps.get("voms-admin.server.version"));
		
	}
	
	private void initEndpoints(ServletContextEvent sce){
		List<AdminServiceContactInfo> endpoints= new ArrayList<AdminServiceContactInfo>();
		
		String confDir = sce.getServletContext().getInitParameter(CONFDIR_KEY);
		log.info("Loading VOMS service endpoints from {}", confDir);
		endpoints = AdminServiceContactUtil.getAdminServiceContactInfo(confDir);
		Collections.sort(endpoints);
		log.debug("Endpoint information loaded: {}", endpoints);
		
		sce.getServletContext().setAttribute(ENDPOINTS_KEY, endpoints);
	}
	
	
	@Override
	public void contextInitialized(ServletContextEvent sce) {
		log.info("VOMSES webapp starting...");
		initVersion(sce);
		initEndpoints(sce);
		sce.getServletContext().setAttribute("host", sce.getServletContext().getInitParameter("host"));
		log.info("VOMSES succesfully initialized.");
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		
		log.info("VOMSES webapp stopped.");
	}

}
