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

import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.glite.security.voms.admin.configuration.VOMSConfiguration;
import org.glite.security.voms.admin.dao.generic.DAOFactory;
import org.glite.security.voms.admin.dao.generic.TaskDAO;
import org.glite.security.voms.admin.database.HibernateFactory;
import org.glite.security.voms.admin.model.task.Task;
import org.glite.security.voms.admin.model.task.Task.TaskStatus;

public class TaskStatusUpdater extends TimerTask {

	public static Log log = LogFactory.getLog(TaskStatusUpdater.class);

	private static TaskStatusUpdater instance = null;

	Timer timer;

	private TaskStatusUpdater(Timer t) {
		timer = t;

		boolean registrationEnabled = VOMSConfiguration.instance().getBoolean(
				"voms.request.webui.enabled", true);

		if (!registrationEnabled) {
			log
					.info("TaskStatusUpdater thread not started since registration is DISABLED for this vo.");
			return;
		}

		if (timer != null) {

			long period = 10L;

			log.info("Scheduling TaskStatusUpdater with period: " + period
					+ " seconds.");
			timer.schedule(this, 5 * 1000, period * 1000);

		}

	}

	public static TaskStatusUpdater instance(Timer t) {

		if (instance == null)
			instance = new TaskStatusUpdater(t);

		return instance;

	}

	@Override
	public void run() {
		// log.info("TaskStatusUpdater started...");
		HibernateFactory.beginTransaction();

		TaskDAO taskDAO = DAOFactory.instance().getTaskDAO();

		List<Task> activeTasks = taskDAO.getActiveTasks();

		for (Task t : activeTasks) {

			Date now = new Date();

			if (t.getExpiryDate().before(now)) {
				log.debug("Task " + t
						+ " has expired, setting its status to EXPIRED.");
				t.setStatus(TaskStatus.EXPIRED);

			}
		}

		HibernateFactory.commitTransaction();
		HibernateFactory.closeSession();

	}

}
