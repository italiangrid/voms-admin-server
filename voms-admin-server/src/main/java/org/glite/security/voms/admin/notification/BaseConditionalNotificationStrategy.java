/**
 * Copyright (c) Members of the EGEE Collaboration. 2006-2009.
 * See http://www.eu-egee.org/partners/ for details on the copyright holders.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Authors:
 * 	Andrea Ceccanti (INFN)
 */

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
