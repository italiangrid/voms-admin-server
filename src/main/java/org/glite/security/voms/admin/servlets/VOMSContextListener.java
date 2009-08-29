/*******************************************************************************
 *Copyright (c) Members of the EGEE Collaboration. 2006. 
 *See http://www.eu-egee.org/partners/ for details on the copyright
 *holders.  
 *
 *Licensed under the Apache License, Version 2.0 (the "License"); 
 *you may not use this file except in compliance with the License. 
 *You may obtain a copy of the License at 
 *
 *    http://www.apache.org/licenses/LICENSE-2.0 
 *
 *Unless required by applicable law or agreed to in writing, software 
 *distributed under the License is distributed on an "AS IS" BASIS, 
 *WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 *See the License for the specific language governing permissions and 
 *limitations under the License.
 *
 * Authors:
 *     Andrea Ceccanti - andrea.ceccanti@cnaf.infn.it
 *******************************************************************************/

package org.glite.security.voms.admin.servlets;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.glite.security.voms.admin.common.VOMSException;
import org.glite.security.voms.admin.common.VOMSService;

public class VOMSContextListener implements ServletContextListener {

	static final Log log = LogFactory.getLog(VOMSContextListener.class);

	public VOMSContextListener() {

		super();

	}

	public void contextInitialized(ServletContextEvent ctxtEvent) {

		try {

			VOMSService.start(ctxtEvent.getServletContext());
		} catch (VOMSException e) {

			log.fatal("VOMS-Admin setup failure!", e);

		}

	}

	public void contextDestroyed(ServletContextEvent ctxtEvent) {

		VOMSService.stop();

		// Release commons logging
		// see
		// http://wiki.apache.org/jakarta-commons/Logging/FrequentlyAskedQuestions
		// and
		// http://issues.apache.org/bugzilla/show_bug.cgi?id=26372#c15

		java.beans.Introspector.flushCaches();
		LogFactory.release(Thread.currentThread().getContextClassLoader());
		java.beans.Introspector.flushCaches();

	}

}
