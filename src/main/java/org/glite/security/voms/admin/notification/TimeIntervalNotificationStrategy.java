package org.glite.security.voms.admin.notification;

import java.util.Calendar;
import java.util.Date;

import org.glite.security.voms.admin.notification.messages.EmailNotification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TimeIntervalNotificationStrategy implements
		ConditionalSendNotificationStrategy {
	
	private static final Logger log = LoggerFactory.getLogger(TimeIntervalNotificationStrategy.class);
	
	private static final int DEFAULT_NOTIFICATION_PERIOD_IN_DAYS = 1;
	
	protected Date lastNotificationSentTime;	
	protected EmailNotification lastNotificationSent;
	
	protected int notificationPeriod = DEFAULT_NOTIFICATION_PERIOD_IN_DAYS;
	
	public TimeIntervalNotificationStrategy(int notificationPeriodInDays) {
		notificationPeriod = notificationPeriodInDays;
	}
	
	public TimeIntervalNotificationStrategy(){}
	
	public synchronized boolean notificationRequired() {
		
		if (lastNotificationSentTime == null){
			log.debug("Notification required since no notification was ever sent.");
			return true;
		}
		
		Date now = new Date();
		
		Date nextNotificationTime = getNextNotificationMessageTime();
		
		if (nextNotificationTime.before(now)){
			log.debug("Notification required since next scheduled notification time {} is in the past.", nextNotificationTime);
			return true;
		}
		
		log.debug("No notification required. Next notification scheduled for {}.", nextNotificationTime);
		return false;
	}

	private Date getNextNotificationMessageTime(){
		
		Calendar cal = Calendar.getInstance();
		
		cal.setTime(lastNotificationSentTime);
		cal.add(Calendar.DAY_OF_YEAR, DEFAULT_NOTIFICATION_PERIOD_IN_DAYS);
		
		return cal.getTime();
		
	}
	
	public synchronized void sendNotification(EmailNotification n) {
		
		if (notificationRequired()){
			
			NotificationService.instance().send(n);
			
			lastNotificationSentTime = new Date();
			lastNotificationSent = n;
			
		}
	}

}
