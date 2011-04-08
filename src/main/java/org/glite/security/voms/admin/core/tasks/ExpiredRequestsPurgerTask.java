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
package org.glite.security.voms.admin.core.tasks;

import java.util.List;

import org.glite.security.voms.admin.configuration.VOMSConfiguration;
import org.glite.security.voms.admin.configuration.VOMSConfigurationConstants;
import org.glite.security.voms.admin.event.EventManager;
import org.glite.security.voms.admin.event.registration.VOMembershipRequestExpiredEvent;
import org.glite.security.voms.admin.persistence.dao.generic.DAOFactory;
import org.glite.security.voms.admin.persistence.dao.generic.RequestDAO;
import org.glite.security.voms.admin.persistence.model.request.NewVOMembershipRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExpiredRequestsPurgerTask implements Runnable, RegistrationServiceTask{

	private static final Logger log = LoggerFactory
			.getLogger(ExpiredRequestsPurgerTask.class);

	private long requestLifetime;

	private boolean warnUsers;

	public ExpiredRequestsPurgerTask() {
		
		VOMSConfiguration conf = VOMSConfiguration.instance();

		requestLifetime = conf.getLong(VOMSConfigurationConstants.VO_MEMBERSHIP_EXPIRATION_TIME,
				300);
		
		warnUsers = conf.getBoolean(VOMSConfigurationConstants.VO_MEMBERSHIP_UNCONFIRMED_REQ_WARN_POLICY, false);
		
	}
	 
	public void run() {
		
		if (requestLifetime < 0){
			log.debug("Request purger NOT STARTED since a negative lifetime for requests was set in configuration.");
			return;
		}
			
		RequestDAO dao = DAOFactory.instance().getRequestDAO();
		
		List<NewVOMembershipRequest> expiredRequests = dao.findExpiredVOMembershipRequests();
				
		for (NewVOMembershipRequest req: expiredRequests){
			
			log.info("Removing unconfirmed request '{}' from database since the confirmation period has expired.", req);
			dao.makeTransient(req);
			
			if (warnUsers)
				EventManager.dispatch(new VOMembershipRequestExpiredEvent(req));			
			
		}
		
	}

}
