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
import org.glite.security.voms.admin.event.user.UserMembershipExpired;
import org.glite.security.voms.admin.event.user.UserSuspendedEvent;
import org.glite.security.voms.admin.model.AUP;
import org.glite.security.voms.admin.model.VOMSUser;
import org.glite.security.voms.admin.model.VOMSUser.SuspensionReason;
import org.glite.security.voms.admin.model.task.SignAUPTask;
import org.glite.security.voms.admin.model.task.Task;
import org.glite.security.voms.admin.model.task.Task.TaskStatus;

public class MembershipValidityCheckTask extends TimerTask {
	
	private static final Log log = LogFactory.getLog( MembershipValidityCheckTask.class );
	
	private static MembershipValidityCheckTask instance;
	
	Timer timer;
	
	public static MembershipValidityCheckTask instance(Timer t){
		
		if (instance == null)
			instance = new MembershipValidityCheckTask(t);
		
		return instance;
		
	}
	
	protected MembershipValidityCheckTask(Timer t) {
		this.timer = t;
		
		if (timer != null){
			
			long period = 30L;
			
			log.info("Scheduling MembershiValidityCheckTask with period: "+ period+" seconds.");
			timer.schedule(this,30*1000, period*1000);
			
		}
	}

	@Override
	public void run() {
		
		HibernateFactory.beginTransaction();
		
		
		AUPDAO aupDAO = DAOFactory.instance().getAUPDAO();
		VOMSUserDAO userDAO = VOMSUserDAO.instance();
		TaskDAO taskDAO = DAOFactory.instance().getTaskDAO();
		
		// Suspend users whose membership has expired
		// and inform VO managers
		List<VOMSUser> expiredMembers = userDAO.getExpiredUsers();
		
		for (VOMSUser u: expiredMembers){
			
			if (!u.isSuspended()){
				
				log.debug("Suspending user '"+u+"' since its membership has expired.");
				u.suspend(SuspensionReason.MEMBERSHIP_EXPIRATION);
				EventManager.dispatch(new UserMembershipExpired(u));
				
			}
		}
		
		// Suspend users that failed to sign AUP in time
		List<VOMSUser> gridAupFailingUsers;
			
		gridAupFailingUsers  = userDAO.getGridAUPFailingUsers();
		
		AUP gridAUP = aupDAO.getGridAUP();
		
		for (VOMSUser u: gridAupFailingUsers){
			
			if (!u.hasSignAUPTaskPending(gridAUP)){
			
				SignAUPTask t = taskDAO.createSignAUPTask(gridAUP);
				u.assignTask(t);
				log.debug("Sign aup task assigned to user '"+u+"'.");
				EventManager.dispatch(new SignAUPTaskAssignedEvent(u, gridAUP));
			
			}else{
				
				for (Task t: u.getTasks()){
					
					if (t instanceof SignAUPTask){
						
						SignAUPTask tt = (SignAUPTask)t;
						
						if (tt.getAup().equals(gridAUP) && tt.getStatus().equals(TaskStatus.EXPIRED) && !u.isSuspended()){
							log.info("Suspeding user '"+u+"' that failed to sign GRID AUP in time");
							
							u.suspend(SuspensionReason.FAILED_TO_SIGN_AUP);
							EventManager.dispatch(new UserSuspendedEvent(u,SuspensionReason.FAILED_TO_SIGN_AUP));
						}
						
					}
				}
				
			}
		}
		
		HibernateFactory.commitTransaction();

	}	
}
