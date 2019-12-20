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
package org.glite.security.voms.admin.view.actions.user;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.struts2.interceptor.SessionAware;
import org.glite.security.voms.admin.configuration.VOMSConfiguration;
import org.glite.security.voms.admin.persistence.dao.generic.DAOFactory;
import org.glite.security.voms.admin.persistence.dao.generic.RequestDAO;
import org.glite.security.voms.admin.persistence.model.AUP;
import org.glite.security.voms.admin.persistence.model.VOMSUser;
import org.glite.security.voms.admin.persistence.model.request.CertificateRequest;
import org.glite.security.voms.admin.persistence.model.request.GroupMembershipRequest;
import org.glite.security.voms.admin.persistence.model.request.MembershipRemovalRequest;
import org.glite.security.voms.admin.persistence.model.request.Request;
import org.glite.security.voms.admin.persistence.model.request.RoleMembershipRequest;
import org.glite.security.voms.admin.view.actions.BaseAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;

public class UserActionSupport extends BaseAction implements
  ModelDriven<VOMSUser>, Preparable, SessionAware {

  public static final Logger log = LoggerFactory
    .getLogger(UserActionSupport.class);
  /**
	 * 
	 */
  private static final long serialVersionUID = 1L;

  public static String LOAD_THIS_USER_KEY = "loadUserId";

  Long userId = -1L;

  VOMSUser model;

  protected List<Request> requests;;

  protected List<GroupMembershipRequest> pendingGroupMembershipRequests;

  protected List<RoleMembershipRequest> pendingRoleMembershipRequests;

  protected List<CertificateRequest> pendingCertificateRequests;

  protected List<MembershipRemovalRequest> pendingMembershipRemovalRequests;

  protected Map<String, Object> theSession;
  
  protected Set<String> requiredFields;

  public VOMSUser getModel() {

    return model;
  }

  public void prepare() throws Exception {

    if (getModel() == null) {

      if (getUserId() != -1){
        model = userById(getUserId());
      } else {
        Long loadThisUserKey = (Long) theSession.get(LOAD_THIS_USER_KEY);

        if (loadThisUserKey != null) {
          model = userById(loadThisUserKey);
          theSession.remove(LOAD_THIS_USER_KEY);
        }

      }
    }

    if (getModel() != null){
      refreshPendingRequests();
    }
    
    requiredFields = VOMSConfiguration.instance().getRequiredPersonalInfoFields();
  }

  public Long getUserId() {

    return userId;
  }

  public void setUserId(Long userId) {

    this.userId = userId;
  }

  public List<GroupMembershipRequest> getPendingGroupMembershipRequests() {

    return pendingGroupMembershipRequests;
  }

  public List<RoleMembershipRequest> getPendingRoleMembershipRequests() {

    return pendingRoleMembershipRequests;
  }

  public List<CertificateRequest> getPendingCertificateRequests() {

    return pendingCertificateRequests;
  }

  public List<MembershipRemovalRequest> getPendingMembershipRemovalRequests() {

    return pendingMembershipRemovalRequests;
  }

  protected void refreshPendingRequests() {

    RequestDAO rDAO = DAOFactory.instance().getRequestDAO();

    requests = rDAO.findRequestsFromUser(model);

    pendingGroupMembershipRequests = rDAO
      .findPendingUserGroupMembershipRequests(model);

    pendingRoleMembershipRequests = rDAO
      .findPendingUserRoleMembershipRequests(model);

    pendingCertificateRequests = rDAO.findPendingUserCertificateRequests(model);

    pendingMembershipRemovalRequests = rDAO
      .findPendingUserMembershipRemovalRequests(model);

  }

  public static long getSerialversionuid() {

    return serialVersionUID;
  }

  public List<Request> getRequests() {

    return requests;
  }

  public void setSession(Map<String, Object> session) {

    log.debug("Setting session: " + session);
    this.theSession = session;

  }

  public boolean hasValidAUPAcceptanceRecord() {

    AUP aup = DAOFactory.instance().getAUPDAO().getVOAUP();

    return (getModel().getAUPAccceptanceRecord(aup.getActiveVersion())
      .getValid() == true);

  }
  
  public Set<String> getRequiredFields() {
    return requiredFields;
  }
}
