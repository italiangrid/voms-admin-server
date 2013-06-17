package org.glite.security.voms.admin.notification.messages;


public interface VOMSNotification {
	public void send();
	public int getDeliveryAttemptCount();
}
