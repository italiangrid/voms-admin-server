package org.glite.security.voms.admin.notification;

import java.util.List;

import org.glite.security.voms.admin.notification.messages.VOMSNotification;

public interface NotificationServiceIF {

	public void send(VOMSNotification n);
	public List<Runnable> shutdownNow();
}
