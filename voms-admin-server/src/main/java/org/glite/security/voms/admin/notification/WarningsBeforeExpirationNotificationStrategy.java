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
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This strategy sends out notifications based on an expiration time
 * and a set of intervals calculated before that expiration time
 * according to a {@link TimeUnit}.
 * 
 * As an example, given an expiration time, time unit set to {@link TimeUnit#DAYS},
 * and "5,3,1" as the warning times, notifications will be sent out
 * 5, 3, and 1 days before the expiration time.
 * 
 * Of course the notification must be triggered by an external thread
 * using the {@link #sendNotification}
 * method. 
 * 
 * The {@link #notificationRequired} method will return a {@link Boolean}
 * telling if a call to {@link #sendNotification} would result in a 
 * notification being sent.
 *  
 * @author andreaceccanti
 *
 */
public class WarningsBeforeExpirationNotificationStrategy 
	extends BaseConditionalNotificationStrategy
{

	private static final Logger log = 
		LoggerFactory.getLogger(WarningsBeforeExpirationNotificationStrategy.class);
	
	private static final TimeUnit DEFAULT_TIME_UNIT = TimeUnit.DAYS;
	private TimeUnit timeUnit = DEFAULT_TIME_UNIT;
	
	private long expirationTime;
	private int[] warningTimes;
	
	
	private void sanityChecks(){
		
		if (warningTimes.length == 0)
			throw new IllegalArgumentException("Warning times should not be empty.");
		
		for (int t: warningTimes){
			if (t <= 0)
				throw new IllegalArgumentException(
					"Warning times should be positive integers! '"+t+"' <= 0,");
		}
		
		long now = System.currentTimeMillis();
		
		if (expirationTime < now)
			throw new IllegalArgumentException("Expiration time is in the past!");
		
	}
	
	
	public WarningsBeforeExpirationNotificationStrategy(
		NotificationTimeStorage timeStorage,
		NotificationServiceIF notificationService,
		int[] warningTimes,
		TimeUnit warningUnit,
		long expirationTime) {

		super(timeStorage, notificationService);
		
		this.warningTimes = warningTimes;
		this.timeUnit = warningUnit;
		this.expirationTime = expirationTime;
		
		sanityChecks();
	}
	
	private long getNextNotificationMessageTime(){
		
		for (int t: warningTimes){
			
			long nextWarningTime = expirationTime - timeUnit.toMillis(t);
			
			if (nextWarningTime < 0)
				new IllegalArgumentException("Please set the time " +
						"unit and expiration time for this strategy correctly!");
			
			if (notificationTimeStorage.getLastNotificationTime() > nextWarningTime)
				continue;
			
			return nextWarningTime;
		}
		
		return 0L;
	}
	
	@Override
	public boolean notificationRequired() {
		
		if (notificationTimeStorage.getLastNotificationTime() == 0){
			return true;
		}
		
		long now = new Date().getTime();
		
		if (now > expirationTime){
			log.debug("No notification required. Expiration time reached.");
			return false;
			
		}
		
		long nextWarningTime = getNextNotificationMessageTime();
		
		if (now >= nextWarningTime && 
			notificationTimeStorage.getLastNotificationTime() < nextWarningTime){
			
			if (log.isDebugEnabled()){
				
				log.debug("Notification required. " +
						"Last notification time: {} Now: {} Next warning time: {}",
						new Object[]{notificationTimeStorage.getLastNotificationTime(),
						now,
						nextWarningTime});
				}
			
			return true;
			
			}
		
		log.debug("No notification required.");
		return false;
	}
	
	
	
	
	/**
	 * @return the timeUnit
	 */
	public TimeUnit getTimeUnit() {
	
		return timeUnit;
	}

	
	/**
	 * @param timeUnit the timeUnit to set
	 */
	public void setTimeUnit(TimeUnit timeUnit) {
	
		this.timeUnit = timeUnit;
	}

}
