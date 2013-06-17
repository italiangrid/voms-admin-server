package org.glite.security.voms.admin.notification;

import java.util.Date;

import org.glite.security.voms.admin.persistence.dao.generic.PeriodicNotificationsDAO;
import org.glite.security.voms.admin.persistence.dao.hibernate.HibernateDAOFactory;
import org.glite.security.voms.admin.persistence.model.PeriodicNotifications;


public class PeriodicNotificationsTimeStorage implements NotificationTimeStorage {

	private String notificationKey;
	private PeriodicNotificationsDAO dao;
	
	public PeriodicNotificationsTimeStorage(String notificationKey) {
		
		this.notificationKey = notificationKey;
		dao = HibernateDAOFactory.instance().getPeriodicNotificationsDAO();
	}

	@Override
	public long getLastNotificationTime() {
		
		PeriodicNotifications pn = dao
			.findByNotificationType(notificationKey);
		
		if (pn == null)
			return 0;
		
		return pn.getLastNotificationTime().getTime();
		
	}

	@Override
	public void setLastNotificationTime(long notificationTime) {
		
		dao.storeLastNotificationTime(notificationKey,
			new Date(notificationTime));
	}

}
