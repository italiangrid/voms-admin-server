package org.glite.security.voms.admin.notification;

import org.glite.security.voms.admin.notification.messages.EmailNotification;

public interface ConditionalSendNotificationStrategy {
	public void sendNotification(EmailNotification n);
	public boolean notificationRequired();
}
