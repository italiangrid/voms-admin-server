package org.glite.security.voms.admin.persistence.dao.generic;

import java.util.Date;

import org.glite.security.voms.admin.persistence.model.PeriodicNotifications;


public interface PeriodicNotificationsDAO {

	public PeriodicNotifications findByNotificationType(String notificationType);
	public void storeLastNotificationTime(String key, Date time);
}
