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
package org.glite.security.voms.admin.notification.audit;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.glite.security.voms.admin.core.tasks.DatabaseTransactionTaskWrapper;
import org.glite.security.voms.admin.core.tasks.VOMSExecutorService;
import org.glite.security.voms.admin.persistence.model.notification.Notification;

public class LogNotificationDelivery {

  private LogNotificationDelivery() {
  }

  public static ScheduledFuture<?> logDeliverySuccess(Notification n) {

    LogNotificationDeliveyOutcomeTask task = new LogNotificationDeliverySuccessTask(
      n);

    return VOMSExecutorService.instance()
      .schedule(new DatabaseTransactionTaskWrapper(task, false), 10,
        TimeUnit.MILLISECONDS);

  }

  public static ScheduledFuture<?> logDeliveryError(Notification n) {

    LogNotificationDeliveyOutcomeTask task = new LogNotificationDeliveryErrorTask(
      n);

    return VOMSExecutorService.instance()
      .schedule(new DatabaseTransactionTaskWrapper(task, false), 10,
        TimeUnit.MILLISECONDS);

  }

}
