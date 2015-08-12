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

import java.util.List;

import org.glite.security.voms.admin.configuration.VOMSConfiguration;
import org.glite.security.voms.admin.persistence.model.VOMSUser;

public class MembershipExpirationWarning extends AbstractVelocityNotification {

  String subject;
  List<VOMSUser> users;

  public MembershipExpirationWarning(List<VOMSUser> users) {

    this.users = users;
  }

  @Override
  protected void buildMessage() {

    VOMSConfiguration conf = VOMSConfiguration.instance();
    String voName = conf.getVOName();

    setSubject(String.format("Membership expiration notice for VO %s.", voName));
    context.put("voName", voName);
    context.put("expiringUsers", users);

    super.buildMessage();
  }

}
