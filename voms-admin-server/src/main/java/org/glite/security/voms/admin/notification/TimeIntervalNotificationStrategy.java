/**
 * Copyright (c) Istituto Nazionale di Fisica Nucleare (INFN). 2006-2016
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
 */
package org.glite.security.voms.admin.notification;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This strategy sends out notification according to a fixed time interval
 * scheme consulting a {@link NotificationTimeStorage} to get (and set) the time
 * of the last notification sent out.
 * 
 * The periodicity is set at creation time with the {@link #notificationPeriod}
 * and {@link #timeUnit} parameters.
 * 
 * The notification will be sent out using the {@link #notificationService} also
 * specified at creation time.
 * 
 * @author andreaceccanti
 * 
 */
public class TimeIntervalNotificationStrategy extends
  BaseConditionalNotificationStrategy {

  private static final Logger log = LoggerFactory
    .getLogger(TimeIntervalNotificationStrategy.class);

  protected static final long DEFAULT_NOTIFICATION_PERIOD = 1L;
  protected static final TimeUnit DEFAULT_TIME_UNIT = TimeUnit.DAYS;

  protected long notificationPeriod = DEFAULT_NOTIFICATION_PERIOD;
  protected TimeUnit timeUnit = DEFAULT_TIME_UNIT;

  public TimeIntervalNotificationStrategy(
    NotificationTimeStorage notificationTimeStorage,
    NotificationServiceIF notificationService, long notificationPeriod,
    TimeUnit notificationTimeUnit) {

    super(notificationTimeStorage, notificationService);

    this.notificationPeriod = notificationPeriod;
    this.timeUnit = notificationTimeUnit;

  }

  public TimeIntervalNotificationStrategy(NotificationTimeStorage timeStorage,
    NotificationServiceIF notificationService) {

    super(timeStorage, notificationService);
  }

  public synchronized boolean notificationRequired() {

    if (notificationTimeStorage.getLastNotificationTime() == 0) {
      log.debug("Notification required since no notification was ever sent.");
      return true;
    }

    long now = new Date().getTime();

    long nextNotificationTime = getNextNotificationMessageTime();

    if (nextNotificationTime < now) {

      if (log.isDebugEnabled()) {

        log.debug("Notification required since next scheduled "
          + "notification time {} is in the past. (time elapsed in msecs: {})",
          nextNotificationTime, (now - nextNotificationTime));
      }

      return true;
    }

    if (log.isDebugEnabled()) {

      log.debug("No notification required. "
        + "Next notification in the future and scheduled for {}.",
        nextNotificationTime);

    }

    return false;
  }

  private long getNextNotificationMessageTime() {

    long lastNotificationTime = notificationTimeStorage
      .getLastNotificationTime();
    long nextNotificationTime = lastNotificationTime
      + timeUnit.toMillis(notificationPeriod);

    log.debug("Computing next notification message time. "
      + "lastNotificationTime: {} nextNotificationTime: {}",
      lastNotificationTime, nextNotificationTime);

    return nextNotificationTime;

  }

  /**
   * @return the notificationPeriod
   */
  public long getNotificationPeriod() {

    return notificationPeriod;
  }

  /**
   * @param notificationPeriod
   *          the notificationPeriod to set
   */
  public void setNotificationPeriod(int notificationPeriod) {

    this.notificationPeriod = notificationPeriod;
  }

  /**
   * @return the timeUnit
   */
  public TimeUnit getTimeUnit() {

    return timeUnit;
  }

  /**
   * @param timeUnit
   *          the timeUnit to set
   */
  public void setTimeUnit(TimeUnit timeUnit) {

    this.timeUnit = timeUnit;
  }

}
