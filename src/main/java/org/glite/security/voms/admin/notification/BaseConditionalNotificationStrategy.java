package org.glite.security.voms.admin.notification;

import java.util.Date;

import org.glite.security.voms.admin.notification.messages.VOMSNotification;

public abstract class BaseConditionalNotificationStrategy implements
	ConditionalSendNotificationStrategy {

	protected NotificationTimeStorage notificationTimeStorage;
	protected NotificationServiceIF notificationService;
	protected VOMSNotification lastNotificationSent;

	protected BaseConditionalNotificationStrategy(
		NotificationTimeStorage timeStorage,
		NotificationServiceIF notificationService) {

		notificationTimeStorage = timeStorage;
		this.notificationService = notificationService;
	}

	/**
	 * @return the notificationTimeStorage
	 */
	public NotificationTimeStorage getNotificationTimeStorage() {

		return notificationTimeStorage;
	}

	/**
	 * @param notificationTimeStorage
	 *          the notificationTimeStorage to set
	 */
	public void setNotificationTimeStorage(
		NotificationTimeStorage notificationTimeStorage) {

		this.notificationTimeStorage = notificationTimeStorage;
	}

	/**
	 * @return the notificationService
	 */
	public NotificationServiceIF getNotificationService() {

		return notificationService;
	}

	/**
	 * @param notificationService
	 *          the notificationService to set
	 */
	public void setNotificationService(NotificationServiceIF notificationService) {

		this.notificationService = notificationService;
	}

	/**
	 * @return the lastNotificationSent
	 */
	public VOMSNotification getLastNotificationSent() {

		return lastNotificationSent;
	}

	/**
	 * @param lastNotificationSent
	 *          the lastNotificationSent to set
	 */
	public void setLastNotificationSent(VOMSNotification lastNotificationSent) {

		this.lastNotificationSent = lastNotificationSent;
	}

	@Override
	public synchronized void sendNotification(VOMSNotification n) {

		if (notificationRequired()) {

			notificationService.send(n);
			notificationTimeStorage.setLastNotificationTime(new Date().getTime());
			setLastNotificationSent(n);
		}

	}

}
