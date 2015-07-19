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
package org.glite.security.voms.admin.operations.users;

import org.glite.security.voms.admin.apiv2.VOMSUserJSON;
import org.glite.security.voms.admin.configuration.VOMSConfiguration;
import org.glite.security.voms.admin.configuration.VOMSConfigurationConstants;
import org.glite.security.voms.admin.event.EventManager;
import org.glite.security.voms.admin.event.user.UserCreatedEvent;
import org.glite.security.voms.admin.event.user.aup.UserSignedAUPEvent;
import org.glite.security.voms.admin.operations.BaseVomsOperation;
import org.glite.security.voms.admin.operations.VOMSContext;
import org.glite.security.voms.admin.operations.VOMSPermission;
import org.glite.security.voms.admin.persistence.dao.VOMSUserDAO;
import org.glite.security.voms.admin.persistence.dao.generic.DAOFactory;
import org.glite.security.voms.admin.persistence.model.VOMSUser;
import org.glite.security.voms.admin.persistence.model.request.NewVOMembershipRequest;

public class CreateUserOperation extends BaseVomsOperation {

  VOMSUser usr = null;

  String caDN = null;

  private CreateUserOperation(VOMSUser user, String caSubject) {

    usr = user;
    caDN = caSubject;
  }

  private CreateUserOperation(String username, String caName, String cn,
    String certUri, String email) {

    usr = new VOMSUser();
    usr.setDn(username);
    usr.setEmailAddress(email);

    caDN = caName;

  }

  private CreateUserOperation(NewVOMembershipRequest request) {

    usr = VOMSUser.fromRequesterInfo(request.getRequesterInfo());
    caDN = request.getRequesterInfo().getCertificateIssuer();

  }

  private CreateUserOperation(VOMSUserJSON user, String certSubject,
    String caSubject) {

    usr = VOMSUser.fromVOMSUserJSON(user);
    usr.setDn(certSubject);
    caDN = caSubject;

  }

  protected Object doExecute() {

    VOMSUser user = VOMSUserDAO.instance().create(usr, caDN);

    EventManager.instance().dispatch(new UserCreatedEvent(user));
    
    // Create an AUP signature record for this user if the automatically created
    // users are not required to sign the AUP
    if (!VOMSConfiguration.instance()
      .getBoolean(
        VOMSConfigurationConstants.REQUIRE_AUP_SIGNATURE_FOR_CREATED_USERS,
        false)){
      
      VOMSUserDAO.instance().signAUP(user);
      EventManager.instance().dispatch(new UserSignedAUPEvent(user, DAOFactory.instance().getAUPDAO().getVOAUP()));
    }

    return user;
  }

  public static CreateUserOperation instance(NewVOMembershipRequest request) {

    return new CreateUserOperation(request);
  }

  public static CreateUserOperation instance(VOMSUser user, String caString) {

    return new CreateUserOperation(user, caString);
  }

  public static CreateUserOperation instance(VOMSUserJSON user,
    String certificateSubject, String caString) {

    return new CreateUserOperation(user, certificateSubject, caString);
  }

  public static CreateUserOperation instance(String username, String caName,
    String cn, String certUri, String email) {

    return new CreateUserOperation(username, caName, cn, certUri, email);
  }

  protected void setupPermissions() {

    addRequiredPermission(VOMSContext.getVoContext(), VOMSPermission
      .getContainerRWPermissions().setMembershipRWPermission());

  }

}
