/**
 * Copyright (c) Istituto Nazionale di Fisica Nucleare (INFN). 2006-2015
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
package org.glite.security.voms.admin.notification.messages;

import java.util.Date;

import org.glite.security.voms.admin.configuration.VOMSConfiguration;
import org.glite.security.voms.admin.event.user.aup.SignAUPTaskReminderEvent;
import org.glite.security.voms.admin.util.URLBuilder;

public class SignAUPReminderMessage extends AbstractVelocityNotification {

  final SignAUPTaskReminderEvent event;

  public SignAUPReminderMessage(SignAUPTaskReminderEvent e) {
    event = e;
    getRecipientList().add(e.getPayload()
      .getEmailAddress());
  }

  @Override
  public void buildMessage() {

    VOMSConfiguration conf = VOMSConfiguration.instance();
    String voName = conf.getVOName();

    String theSubject = String.format(
      "Request to sign VO %s acceptable usage policy (AUP).", conf.getVOName());

    setSubject(theSubject);

    Date expirationDate = event.getTask()
      .getExpiryDate();

    context.put("voName", voName);
    context.put("aup", event.getAup());
    context.put("user", event.getTask()
      .getUser());
    context.put("recipient", getRecipientList().get(0));

    context.put("signAUPURL",
      URLBuilder.baseVOMSURLFromConfiguration() + "/sign-aup");

    context.put("expirationDate", expirationDate);

    super.buildMessage();
  }

}
