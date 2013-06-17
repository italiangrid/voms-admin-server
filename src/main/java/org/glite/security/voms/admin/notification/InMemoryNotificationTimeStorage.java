package org.glite.security.voms.admin.notification;


public class InMemoryNotificationTimeStorage implements NotificationTimeStorage {

	long lastSentNotificationTime = 0;

	@Override
	public long getLastNotificationTime() {

		return lastSentNotificationTime;
	}

	@Override
	public void setLastNotificationTime(long time) {

		this.lastSentNotificationTime = time;

	}

}
