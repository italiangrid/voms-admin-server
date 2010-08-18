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
import java.util.Timer;
import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.glite.security.voms.admin.configuration.VOMSConfiguration;
import org.glite.security.voms.admin.configuration.VOMSConfigurationConstants;
import org.glite.security.voms.admin.event.EventManager;
import org.glite.security.voms.admin.event.user.SignAUPTaskAssignedEvent;
import org.glite.security.voms.admin.event.user.UserMembershipExpired;
import org.glite.security.voms.admin.event.user.UserSuspendedEvent;
import org.glite.security.voms.admin.persistence.HibernateFactory;
import org.glite.security.voms.admin.persistence.dao.VOMSUserDAO;
import org.glite.security.voms.admin.persistence.dao.generic.AUPDAO;
import org.glite.security.voms.admin.persistence.dao.generic.DAOFactory;
import org.glite.security.voms.admin.persistence.dao.generic.TaskDAO;
import org.glite.security.voms.admin.persistence.model.AUP;
import org.glite.security.voms.admin.persistence.model.VOMSUser;
import org.glite.security.voms.admin.persistence.model.VOMSUser.SuspensionReason;
import org.glite.security.voms.admin.persistence.model.task.SignAUPTask;
import org.glite.security.voms.admin.persistence.model.task.Task;
import org.glite.security.voms.admin.persistence.model.task.Task.TaskStatus;

public class MembershipValidityCheckTask extends DatabaseTransactionTask {

	private static final Logger log = LoggerFactory
			.getLogger(MembershipValidityCheckTask.class);

	private static MembershipValidityCheckTask instance;

	Timer timer;

	public static MembershipValidityCheckTask instance(Timer t) {

		if (instance == null)
			instance = new MembershipValidityCheckTask(t);

		return instance;

	}

	protected MembershipValidityCheckTask(Timer t) {
		this.timer = t;

		boolean registrationEnabled = VOMSConfiguration.instance().getBoolean(
				VOMSConfigurationConstants.REGISTRATION_SERVICE_ENABLED, true);

		if (!registrationEnabled) {
			log
					.info("MembershipValidityCheck thread not started since registration is DISABLED for this vo.");
			return;
		}
		
		boolean readOnly = VOMSConfiguration.instance().getBoolean(VOMSConfigurationConstants.READONLY, false);
		
		if (readOnly){
			log.info(this.getClass().getSimpleName() + " thread not started since this is a READ ONLY voms admin instance.");
			return;
		}

		if (timer != null) {

			long period = VOMSConfiguration.instance().getLong(
					VOMSConfigurationConstants.MEMBERSHIP_CHECK_PERIOD, 30L);

			log.info("Scheduling MembershipValidityCheckTask with period: "
					+ period + " seconds.");
			timer.schedule(this, 30 * 1000, period * 1000);

		}
	}

	protected void checkAndPossiblySuspendUser(VOMSUser u, AUP aup) {

		log.debug("Checking user '" + u + "' compliance with '" + aup.getName()
				+ "'");
		TaskDAO taskDAO = DAOFactory.instance().getTaskDAO();

		if (u.getPendingSignAUPTask(aup) == null) {

			SignAUPTask t = taskDAO.createSignAUPTask(aup);
			u.assignTask(t);
			log.debug("Sign aup task assigned to user '" + u + "'.");
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

	@Override
	protected void doRun() {
		log.debug("started...");
		
		AUPDAO aupDAO = DAOFactory.instance().getAUPDAO();
		VOMSUserDAO userDAO = VOMSUserDAO.instance();

		// Suspend users whose membership has expired
		// and inform VO managers
		List<VOMSUser> expiredMembers = userDAO.getExpiredUsers();

		for (VOMSUser u : expiredMembers) {

			if (!u.isSuspended()) {

				log.debug("Suspending user '" + u
						+ "' since its membership has expired.");
				u.suspend(SuspensionReason.MEMBERSHIP_EXPIRATION);
				EventManager.dispatch(new UserMembershipExpired(u));

			}
		}

		// Suspend users that failed to sign AUP in time
		List<VOMSUser> voAupFailingUsers = userDAO.getAUPFailingUsers(aupDAO
				.getVOAUP());

		log.debug("voAUPFailingUsers:" + voAupFailingUsers);

		for (VOMSUser u : voAupFailingUsers)
			checkAndPossiblySuspendUser(u, aupDAO.getVOAUP());

		log.debug("done.");
		
	}
}
