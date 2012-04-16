package org.glite.security.voms.admin.core.validation.strategies.impl;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.glite.security.voms.admin.core.validation.ValidationManager;
import org.glite.security.voms.admin.core.validation.strategies.ExpiredMembersLookupStrategy;
import org.glite.security.voms.admin.core.validation.strategies.HandleExpiredMembersStrategy;
import org.glite.security.voms.admin.notification.ConditionalSendNotificationStrategy;
import org.glite.security.voms.admin.notification.NotificationUtil;
import org.glite.security.voms.admin.notification.TimeIntervalNotificationStrategy;
import org.glite.security.voms.admin.notification.messages.ExpiredMembersWarning;
import org.glite.security.voms.admin.persistence.dao.VOMSUserDAO;
import org.glite.security.voms.admin.persistence.model.VOMSUser;
import org.glite.security.voms.admin.persistence.model.VOMSUser.SuspensionReason;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GracePeriodExpiredMembersStrategy implements
		HandleExpiredMembersStrategy, ExpiredMembersLookupStrategy {

	public static final Logger log = LoggerFactory.getLogger(GracePeriodExpiredMembersStrategy.class);
	
	public static final long DEFAULT_GRACE_PERIOD_IN_DAYS = 7;
	
	ConditionalSendNotificationStrategy notificationStrategy;
	
	long gracePeriod = DEFAULT_GRACE_PERIOD_IN_DAYS;
	
	
	
	public GracePeriodExpiredMembersStrategy(long gracePeriod, int notificationInterval) {
		
		this.gracePeriod = gracePeriod;
		
		this.notificationStrategy = new TimeIntervalNotificationStrategy(notificationInterval);
		
	}
	
	public GracePeriodExpiredMembersStrategy(int notificationInterval) {
		this.notificationStrategy = new TimeIntervalNotificationStrategy(notificationInterval);
	}
	
	public List<VOMSUser> findExpiredMembers() {
		return VOMSUserDAO.instance().findExpiredUsers();
	}

	
	protected void sendNotificationToAdmins(List<VOMSUser> expiredMembers){
		
		if (expiredMembers.isEmpty())
			return;
		
		if (notificationStrategy.notificationRequired()){
			
			log.info("Sending out notification about EXPIRED VO members.");
			ExpiredMembersWarning m = new ExpiredMembersWarning(expiredMembers);
			m.addRecipients(NotificationUtil.getAdministratorsEmailList());
			notificationStrategy.sendNotification(m);
		}
		
	}
	
	protected void suspendExpiredMembers(List<VOMSUser> expiredMembers){
		
		if (expiredMembers.isEmpty())
			return;
			
		Date now = new Date();
		
		for (VOMSUser u : expiredMembers) {

			if (!u.isSuspended()) {
					
				long timeDiff = now.getTime() - u.getEndTime().getTime();
				
				if (TimeUnit.MILLISECONDS.toDays(timeDiff) > gracePeriod){
					
					log.info("Suspending user '" + u
							+ "' since its membership has expired and grace period is over.");
					
					ValidationManager.instance().suspendUser(u, SuspensionReason.MEMBERSHIP_EXPIRATION);
					
				}else{
					
					log.debug("User '{}' not suspended due to grace period.", u);
					
				}
				
				log.debug("Now: {}, User end time: {}, Grace period (in days): {}", 
						new Object[]{now,u.getEndTime(),gracePeriod});
			
			}else{
			    
			    log.debug("User {} has expired but is currently already suspended.", u);
			}
		}
		
	}
	
	public void handleExpiredMembers(List<VOMSUser> expiredMembers) {
		
		log.debug("Handling expired members: {}", expiredMembers);
		
		suspendExpiredMembers(expiredMembers);
		sendNotificationToAdmins(expiredMembers);
	}

}
