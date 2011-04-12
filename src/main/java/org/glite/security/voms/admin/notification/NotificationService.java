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

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.glite.security.voms.admin.configuration.VOMSConfiguration;
import org.glite.security.voms.admin.configuration.VOMSConfigurationConstants;
import org.glite.security.voms.admin.notification.messages.EmailNotification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NotificationService {

	private static final Logger log = LoggerFactory.getLogger(NotificationService.class);

	private static NotificationService singleton = null;

	private LinkedBlockingQueue<EmailNotification> outgoingMessages = new LinkedBlockingQueue<EmailNotification>();

	private ExecutorService executorService = Executors.newSingleThreadExecutor();

	int maxDeliveryAttemptCount = VOMSConfiguration.instance().getInt(
			"voms.noification.max_delivery_attempt_count", 5);

	long sleepTimeBeforeRetry = 30;

	private NotificationService() {
	    
	    	executorService.submit(new NotificationRunner(outgoingMessages));
	    	
	}
	
	
	public synchronized static void shutdown(){
	    
	    if (singleton == null){
		log.debug("Notification service has not been started and, as such, cannot be shut down.");
		return;
	    }
	
	    
	    singleton.shutdownNow();
	    
	}

	public synchronized static NotificationService instance() {

		if (singleton == null)
			singleton = new NotificationService();

		return singleton;
	}
	
	

	public void send(EmailNotification n) {

		log.debug("Adding notification '" + n + "' to outgoing message queue.");
		if (!VOMSConfiguration.instance().getBoolean(VOMSConfigurationConstants.NOTIFICATION_DISABLED, false)){
		    outgoingMessages.add(n);
		}
		else{
		    log.warn("Outgoing notification {} will be discarded as the notification service is DISABLED.", n.toString());
		}

	}

	class NotificationRunner implements Runnable {

		final LinkedBlockingQueue<EmailNotification> outgoingQueue;

		public NotificationRunner(
				LinkedBlockingQueue<EmailNotification> outgoingQueue) {
			this.outgoingQueue = outgoingQueue;
		}

		public void run() {
			for (;;) {

				boolean deliveryHadErrors = false;

				try {

					EmailNotification n = outgoingQueue.take();
					log.debug("Fetched outgoing message " + n);

					try {
						n.send();
						deliveryHadErrors = false;
						log.debug("Notification '" + n
								+ "' delivered succesfully.");

					} catch (VOMSNotificationException e) {

						deliveryHadErrors = true;
						log.error("Error dispatching email notification '" + n
								+ "': " + e, e);

						if (n.getDeliveryAttemptCount() < maxDeliveryAttemptCount) {
							outgoingQueue.put(n);
						} else
							log.warn("Discarding notification '" + n
									+ "' after " + n.getDeliveryAttemptCount()
									+ " failed delivery attempts.");

					} catch (Throwable t) {

						log.error("Error dispatching email notification '" + n
								+ "': " + t.getMessage());
						if (log.isDebugEnabled())
							log.error("Error dispatching email notification '"
									+ n + "': " + t.getMessage(), t);

						deliveryHadErrors = true;

					}

					if (deliveryHadErrors && !outgoingQueue.isEmpty())
						TimeUnit.SECONDS.sleep(sleepTimeBeforeRetry);

				} catch (InterruptedException e) {

					return;
				}
			}

		}
	}

	/**
	 * @return
	 * @see java.util.concurrent.ExecutorService#shutdownNow()
	 */
	protected List<Runnable> shutdownNow() {
	    return executorService.shutdownNow();
	}
	
}
