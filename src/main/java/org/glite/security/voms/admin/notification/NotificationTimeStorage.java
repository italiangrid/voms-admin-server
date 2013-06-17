package org.glite.security.voms.admin.notification;


public interface NotificationTimeStorage {
	
	public long getLastNotificationTime();
	public void setLastNotificationTime(long notificationTime);

}
