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
		log.info("User "+u);
		
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
