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
package org.glite.security.voms.admin.operations.requests;

import org.glite.security.voms.admin.error.IllegalRequestStateException;
import org.glite.security.voms.admin.operations.BaseVomsOperation;
import org.glite.security.voms.admin.operations.CurrentAdmin;
import org.glite.security.voms.admin.operations.users.FindUserOperation;
import org.glite.security.voms.admin.persistence.dao.VOMSGroupDAO;
import org.glite.security.voms.admin.persistence.dao.VOMSRoleDAO;
import org.glite.security.voms.admin.persistence.error.NoSuchGroupException;
import org.glite.security.voms.admin.persistence.error.NoSuchRoleException;
import org.glite.security.voms.admin.persistence.error.NoSuchUserException;
import org.glite.security.voms.admin.persistence.model.VOMSGroup;
import org.glite.security.voms.admin.persistence.model.VOMSRole;
import org.glite.security.voms.admin.persistence.model.VOMSUser;
import org.glite.security.voms.admin.persistence.model.request.Request;
import org.glite.security.voms.admin.persistence.model.request.Request.STATUS;

public abstract class BaseHandleRequestOperation<T extends Request>
  extends BaseVomsOperation {

  protected DECISION decision = DECISION.REJECT;
  T request;

  public BaseHandleRequestOperation(T request, DECISION decision) {

    this.request = request;
    this.decision = decision;
  }

  protected abstract void approve();

  protected void approveRequest() {

    CurrentAdmin admin = CurrentAdmin.instance();
    request.approve(admin.getRealSubject(), admin.getRealIssuer());

  }

  protected void rejectRequest() {

    CurrentAdmin admin = CurrentAdmin.instance();
    request.reject(admin.getRealSubject(), admin.getRealIssuer());
  }

  protected void checkRequestStatus(STATUS status) {

    if (!request.getStatus().equals(status))
      throw new IllegalRequestStateException(
        "Illegal state for request: " + request.getStatus());
  }

  @Override
  protected final Object doExecute() {

    if (decision.equals(DECISION.APPROVE))
      approve();
    else
      reject();

    return request;

  }

  protected final VOMSGroup findGroupByName(String groupName) {

    VOMSGroup g = VOMSGroupDAO.instance().findByName(groupName);

    if (g == null)
      throw new NoSuchGroupException(
        "Requested group '" + groupName + "' does not exist in this VO.");

    return g;
  }

  protected final VOMSRole findRoleByNameAllowNull(String roleName) {

    VOMSRole r = VOMSRoleDAO.instance().findByName(roleName);

    return r;

  }

  protected final VOMSRole findRoleByName(String roleName) {

    VOMSRole r = VOMSRoleDAO.instance().findByName(roleName);

    if (r == null)
      throw new NoSuchRoleException(
        "Requested role '" + roleName + "' does not exist in this VO.");

    return r;

  }

  public DECISION getDecision() {

    return decision;
  }

  public T getRequest() {

    return request;
  }

  protected final VOMSUser getRequesterAsVomsUser() {

    VOMSUser u = (VOMSUser) FindUserOperation
      .instance(request.getRequesterInfo().getCertificateSubject(),
        request.getRequesterInfo().getCertificateIssuer())
      .execute();

    if (u == null)
      throw new NoSuchUserException(String.format("User '%s, %s' not found!",
        request.getRequesterInfo().getCertificateSubject(),
        request.getRequesterInfo().getCertificateIssuer()));

    return u;
  }

  protected abstract void reject();

  protected void setDecision(DECISION decision) {

    this.decision = decision;
  }
}
