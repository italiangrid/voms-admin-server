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
package org.glite.security.voms.admin.event.request;

import org.glite.security.voms.admin.event.EventDescription;
import org.glite.security.voms.admin.persistence.model.request.RoleMembershipRequest;

@EventDescription(
  message = "approved role assignment request from user '%s %s' for role '%s' in group '%s'",
  params = { "requestorGivenName", "requestorSurname", "requestedRoleName",
    "requestedRoleGroupName" })
public class RoleMembershipApprovedEvent extends RoleMembershipRequestEvent {

  public RoleMembershipApprovedEvent(RoleMembershipRequest req) {

    super(req);
    // TODO Auto-generated constructor stub
  }

}
