package org.glite.security.voms.admin.common.tasks;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.glite.security.voms.admin.dao.VOMSUserDAO;
import org.glite.security.voms.admin.dao.generic.AUPDAO;
import org.glite.security.voms.admin.dao.generic.DAOFactory;
import org.glite.security.voms.admin.dao.generic.TaskDAO;
import org.glite.security.voms.admin.database.HibernateFactory;
import org.glite.security.voms.admin.event.EventManager;
import org.glite.security.voms.admin.event.user.SignAUPTaskAssignedEvent;
import org.glite.security.voms.admin.event.user.UserSuspendedEvent;
import org.glite.security.voms.admin.model.AUP;
import org.glite.security.voms.admin.model.VOMSUser;
import org.glite.security.voms.admin.model.task.SignAUPTask;
import org.glite.security.voms.admin.model.task.Task;
import org.glite.security.voms.admin.model.task.Task.TaskStatus;

public class AUPAcceptanceCheckTask extends TimerTask {
	
	private static final Log log = LogFactory.getLog( AUPAcceptanceCheckTask.class );
	
	private static AUPAcceptanceCheckTask instance;
	
	Timer timer;
	
	public static AUPAcceptanceCheckTask instance(Timer t){
		
		if (instance == null)
			instance = new AUPAcceptanceCheckTask(t);
		
		return instance;
		
	}
	
	protected AUPAcceptanceCheckTask(Timer t) {
		this.timer = t;
		
		if (timer != null){
			
			long period = 30L;
			
			log.info("Scheduling AUPAcceptanceCheckTask with period: "+ period+" seconds.");
			timer.schedule(this,30*1000, period*1000);
			
		}
	}

	@Override
	public void run() {
		
		// log.info("AUP Acceptance test task started...");
		HibernateFactory.beginTransaction();
		
		
		AUPDAO aupDAO = DAOFactory.instance().getAUPDAO();
		VOMSUserDAO userDAO = VOMSUserDAO.instance();
		TaskDAO taskDAO = DAOFactory.instance().getTaskDAO();
		
		List<VOMSUser> gridAupFailingUsers;
		
		// Grid AUP checking
		try{
			
			gridAupFailingUsers  = userDAO.getGridAUPFailingUsers();
		
		}catch (Throwable t) {
			
			// This should never happen
			log.error("Error executing HSQL query: "+t.getMessage());
			return;
		}
		
		AUP gridAUP = aupDAO.getGridAUP();
		
		for (VOMSUser u: gridAupFailingUsers){
			
			// log.debug("Found user '"+u+"' failing Grid AUP acceptance.");
			
			if (!u.hasSignAUPTaskPending(gridAUP)){
			
				SignAUPTask t = taskDAO.createSignAUPTask(gridAUP);
				u.assignTask(t);
				log.debug("Sign aup task assigned to user '"+u+"'.");
				EventManager.dispatch(new SignAUPTaskAssignedEvent(u, gridAUP));
			
			}else{
				
				// Suspend users that did not sign aup task in time
				for (Task t: u.getTasks()){
					
					if (t instanceof SignAUPTask){
						
						SignAUPTask tt = (SignAUPTask)t;
						
						if (tt.getAup().equals(gridAUP) && tt.getStatus().equals(TaskStatus.EXPIRED) && !u.isSuspended()){
							log.info("Suspeding user '"+u+"' that failed to sign GRID AUP in time");
							
							String reason = "User failed to accept Grid AUP in the allowed time"; 
							u.suspend(reason);
							EventManager.dispatch(new UserSuspendedEvent(u,reason));
						}
						
					}
				}
				
			}
		}
		
		
		
		
		
		HibernateFactory.commitTransaction();
		// log.info("AUP Acceptance test task done...");

	}

	
	
}
