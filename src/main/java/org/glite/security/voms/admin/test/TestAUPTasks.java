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
package org.glite.security.voms.admin.test;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.glite.security.voms.admin.dao.VOMSUserDAO;
import org.glite.security.voms.admin.dao.generic.AUPDAO;
import org.glite.security.voms.admin.dao.generic.DAOFactory;
import org.glite.security.voms.admin.dao.generic.TaskDAO;
import org.glite.security.voms.admin.database.HibernateFactory;
import org.glite.security.voms.admin.model.VOMSUser;
import org.glite.security.voms.admin.model.task.SignAUPTask;

public class TestAUPTasks {

	public static final Log log = LogFactory.getLog(TestAUPTasks.class);

	public TestAUPTasks() {

		HibernateFactory.beginTransaction();

		VOMSUser u = TestUtils.createUser();
		log.info("User " + u);

		TaskDAO tDAO = DAOFactory.instance().getTaskDAO();
		AUPDAO aupDAO = DAOFactory.instance().getAUPDAO();

		SignAUPTask t1 = tDAO.createSignAUPTask(aupDAO.getVOAUP());
		u.assignTask(t1);

		HibernateFactory.commitTransaction();

		HibernateFactory.beginTransaction();

		SignAUPTask t2 = tDAO.createSignAUPTask(aupDAO.getVOAUP());
		u.assignTask(t2);

		HibernateFactory.commitTransaction();

		HibernateFactory.beginTransaction();

		VOMSUserDAO.instance().signAUP(u, aupDAO.getVOAUP());

		HibernateFactory.commitTransaction();

		SignAUPTask t3 = u.getPendingSignAUPTask(aupDAO.getVOAUP());

		log.info("done");

	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		TestUtils.configureLogging();
		TestUtils.setupVOMSConfiguration();
		TestUtils.setupVOMSDB();

		new TestAUPTasks();

		System.exit(0);

	}

}
