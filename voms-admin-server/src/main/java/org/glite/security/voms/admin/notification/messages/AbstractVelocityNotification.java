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
package org.glite.security.voms.admin.notification.messages;

import org.apache.commons.lang.StringUtils;
import org.apache.velocity.VelocityContext;
import org.glite.security.voms.admin.notification.NotificationUtil;

public abstract class AbstractVelocityNotification extends
  VelocityEmailNotification {

  public static final String subjectPrefix = "[VOMS Admin]";
  VelocityContext context;
  String templatePrefix;

  public AbstractVelocityNotification() {

    setTemplatePrefix("/templates");
    context = new VelocityContext();
  }

  public void addToContext(String key, String value) {

    context.put(key, value);
  }

  protected void setupContext() {

    context.put("serviceManagementURL", NotificationUtil.getServiceURL());
  }

  @Override
  public void buildMessage() {

    setupContext();

    String templateFileName = String.format("/%s/%s.vm", getTemplatePrefix(),
      getClass().getSimpleName());

    setTemplateFile(templateFileName);

    buildMessageFromTemplate(context);

  }

  public String getTemplatePrefix() {

    return templatePrefix;
  }

  public void setTemplatePrefix(String templatePrefix) {

    this.templatePrefix = templatePrefix;
  }

  @Override
  public void setSubject(String subject) {

    super.setSubject(subjectPrefix + " " + subject);
  }

  @Override
  public String toString() {

    return "" + super.toString() + "[subject='" + getSubject()
      + "',recipients='" + StringUtils.join(getRecipientList(), ",") + "']";
  }

}
