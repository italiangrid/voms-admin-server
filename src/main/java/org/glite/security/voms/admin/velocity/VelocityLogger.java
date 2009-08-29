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
package org.glite.security.voms.admin.velocity;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.velocity.runtime.RuntimeServices;
import org.apache.velocity.runtime.log.LogSystem;

public class VelocityLogger implements LogSystem {

	private static final Log log = LogFactory.getLog(VelocityLogger.class);

	public void init(RuntimeServices runtime) throws Exception {

		log.info("Initializing Velocity logging services.");

	}

	public void logVelocityMessage(int debugLevel, String message) {

		switch (debugLevel) {

		case LogSystem.DEBUG_ID:
			log.debug(message);
			break;

		case LogSystem.INFO_ID:
			log.info(message);
			break;

		case LogSystem.WARN_ID:
			log.warn(message);
			break;

		case LogSystem.ERROR_ID:
			log.error(message);
			break;

		default:
			log.info(message);
			break;
		}

	}

}
