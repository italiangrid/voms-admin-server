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

package org.glite.security.voms.admin.core.validation;

import java.util.Collections;
import java.util.List;

import org.glite.security.voms.admin.event.EventManager;
import org.glite.security.voms.admin.event.user.SignAUPTaskAssignedEvent;
import org.glite.security.voms.admin.event.user.UserMembershipExpired;
import org.glite.security.voms.admin.event.user.UserSuspendedEvent;
import org.glite.security.voms.admin.persistence.dao.VOMSUserDAO;
import org.glite.security.voms.admin.persistence.dao.generic.AUPDAO;
import org.glite.security.voms.admin.persistence.dao.generic.DAOFactory;
import org.glite.security.voms.admin.persistence.dao.generic.TaskDAO;
import org.glite.security.voms.admin.persistence.dao.hibernate.HibernateDAOFactory;
import org.glite.security.voms.admin.persistence.model.AUP;
import org.glite.security.voms.admin.persistence.model.VOMSUser;
import org.glite.security.voms.admin.persistence.model.VOMSUser.SuspensionReason;
import org.glite.security.voms.admin.persistence.model.task.SignAUPTask;
import org.glite.security.voms.admin.persistence.model.task.Task;
import org.glite.security.voms.admin.persistence.model.task.Task.TaskStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultMembershipCheckBehaviour extends AbstractMembershipCheckBehaviour{
	
	public static final Logger log = LoggerFactory.getLogger(DefaultMembershipCheckBehaviour.class);

	public List<VOMSUser> findAUPFailingMembers() {
		
		AUPDAO aupDAO = DAOFactory.instance().getAUPDAO();
		VOMSUserDAO userDAO = VOMSUserDAO.instance();
		
		return userDAO.getAUPFailingUsers(aupDAO.getVOAUP());
		
	}
	
	public List<VOMSUser> findExpiredMembers() {
		
		return VOMSUserDAO.instance().getExpiredUsers();
		
	}

	public List<VOMSUser> findExpiringMembers() {
		
	    return Collections.emptyList();
	}

	
	protected void handleAUPFailingMember(VOMSUser u){
		
		AUPDAO aupDAO = HibernateDAOFactory.instance().getAUPDAO();
		AUP aup = aupDAO.getVOAUP();
		
		log.debug("Checking user '" + u + "' compliance with '" + aup.getName()
				+ "'");
		TaskDAO taskDAO = DAOFactory.instance().getTaskDAO();

		if (u.getPendingSignAUPTask(aup) == null) {

			SignAUPTask t = taskDAO.createSignAUPTask(aup);
			u.assignTask(t);
			log.debug("Sign aup task assigned to user '{}'",u);
			EventManager.dispatch(new SignAUPTaskAssignedEvent(u, aup));

		} else {

			for (Task t : u.getTasks()) {

				if (t instanceof SignAUPTask) {

					SignAUPTask tt = (SignAUPTask) t;

					if (tt.getAup().equals(aup)
							&& tt.getStatus().equals(TaskStatus.EXPIRED)
							&& !u.getSuspended()) {
						log.info("Suspeding user '" + u
								+ "' that failed to sign AUP in time");

						u.suspend(SuspensionReason.FAILED_TO_SIGN_AUP);
						EventManager.dispatch(new UserSuspendedEvent(u,
								SuspensionReason.FAILED_TO_SIGN_AUP));
					}

				}
			}

		}
		
	}
	
	public void handleAUPFailingMembers(List<VOMSUser> aupFailingMembers) {
		
		if (aupFailingMembers == null || aupFailingMembers.isEmpty()){
			return;
		}
		
		for (VOMSUser u: aupFailingMembers)
			handleAUPFailingMember(u);
		
	}

	public void handleExpiredMembers(List<VOMSUser> expiredMembers) {
		
		for (VOMSUser u : expiredMembers) {

			if (!u.isSuspended()) {

				log.debug("Suspending user '" + u
						+ "' since its membership has expired.");
				u.suspend(SuspensionReason.MEMBERSHIP_EXPIRATION);
				EventManager.dispatch(new UserMembershipExpired(u));

			}else{
			    
			    log.debug("User {} has expired but is currently already suspended.", u);
			}
		}
	}

	public void handleMembersAboutToExpire(List<VOMSUser> expiringMembers) {
	    	
		return;

	}

	

}
