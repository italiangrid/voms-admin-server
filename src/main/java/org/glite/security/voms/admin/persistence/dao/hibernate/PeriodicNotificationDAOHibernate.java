package org.glite.security.voms.admin.persistence.dao.hibernate;

import java.util.Date;

import org.glite.security.voms.admin.persistence.dao.generic.PeriodicNotificationsDAO;
import org.glite.security.voms.admin.persistence.model.PeriodicNotifications;
import org.hibernate.criterion.Restrictions;

public class PeriodicNotificationDAOHibernate extends
	GenericHibernateDAO<PeriodicNotifications, Long> implements
	PeriodicNotificationsDAO {

	public PeriodicNotifications findByNotificationType(String notificationType){
		
		return findByCriteriaUniqueResult(Restrictions.eq("notificationType", 
			notificationType)); 
	}

	@Override
	public void storeLastNotificationTime(String key, Date time) {

		PeriodicNotifications pn = findByNotificationType(key);
		
		if (pn == null){
			pn = new PeriodicNotifications();
			pn.setNotificationType(key);
		}
		
		pn.setLastNotificationTime(time);
		makePersistent(pn);
	}
	
}
