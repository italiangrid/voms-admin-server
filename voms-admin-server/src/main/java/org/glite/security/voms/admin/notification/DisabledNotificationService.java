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

import java.util.List;

import org.glite.security.voms.admin.notification.messages.VOMSNotification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public enum DisabledNotificationService implements NotificationServiceIF {

  INSTANCE;

  public static final Logger LOG = LoggerFactory
    .getLogger(DisabledNotificationService.class);

  @Override
  public void start() {

    LOG.info(
      "Starting DISABLED notification service: email notification going to /dev/null");
  }

  @Override
  public void send(VOMSNotification n) {

    n.buildMessage();

    LOG.warn(
      "Outgoing notification will be discarded as the notification service is DISABLED. Subject:{}, To:{}, Body:{}",
      new Object[] { n.getSubject(), n.getRecipientList(), n.getMessage() });
  }

  @Override
  public List<Runnable> shutdownNow() {

    LOG.info("Shutting down DISABLED notification service");
    return null;
  }

}
