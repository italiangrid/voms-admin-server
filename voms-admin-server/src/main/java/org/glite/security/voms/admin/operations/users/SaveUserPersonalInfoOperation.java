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
package org.glite.security.voms.admin.operations.users;

import org.glite.security.voms.admin.event.EventManager;
import org.glite.security.voms.admin.event.user.UserPersonalInformationUpdatedEvent;
import org.glite.security.voms.admin.operations.BaseUserAdministrativeOperation;
import org.glite.security.voms.admin.operations.VOMSContext;
import org.glite.security.voms.admin.operations.VOMSPermission;
import org.glite.security.voms.admin.persistence.dao.VOMSUserDAO;
import org.glite.security.voms.admin.persistence.model.VOMSUser;

public class SaveUserPersonalInfoOperation extends
  BaseUserAdministrativeOperation {

  VOMSUser targetUser;

  String name;
  String surname;
  String institution;
  String address;
  String phoneNumber;
  String emailAddress;

  public SaveUserPersonalInfoOperation(VOMSUser targetUser, String name,
    String surname, String institution, String address, String phoneNumber,
    String emailAddress) {

    this.targetUser = targetUser;
    this.name = name;
    this.surname = surname;
    this.institution = institution;
    this.address = address;
    this.phoneNumber = phoneNumber;
    this.emailAddress = emailAddress;
  }

  @Override
  protected Object doExecute() {

    if (targetUser == null){
      targetUser = getAuthorizedUser();
    }
    
    targetUser.setName(name);
    targetUser.setSurname(surname);

    targetUser.setInstitution(institution);
    targetUser.setAddress(address);
    targetUser.setPhoneNumber(phoneNumber);
    targetUser.setEmailAddress(emailAddress);

    VOMSUserDAO.instance().update(targetUser);

    EventManager.instance().dispatch(new UserPersonalInformationUpdatedEvent(targetUser));
    
    return targetUser;
  }

  @Override
  protected void setupPermissions() {

    addRequiredPermission(VOMSContext.getVoContext(), VOMSPermission
      .getContainerReadPermission().setMembershipReadPermission()
      .setPersonalInfoReadPermission().setPersonalInfoWritePermission());
  }

}
