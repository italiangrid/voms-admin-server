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
package org.glite.security.voms.admin.operations.users;

import org.glite.security.voms.admin.event.EventManager;
import org.glite.security.voms.admin.event.user.UserDeletedEvent;
import org.glite.security.voms.admin.operations.BaseVoRWOperation;
import org.glite.security.voms.admin.persistence.dao.VOMSUserDAO;
import org.glite.security.voms.admin.persistence.error.NoSuchUserException;
import org.glite.security.voms.admin.persistence.model.VOMSUser;

public class DeleteUserOperation extends BaseVoRWOperation {

  VOMSUser usr = null;

  Long id;

  private DeleteUserOperation(VOMSUser u) {

    this.usr = u;
  }

  private DeleteUserOperation(Long id) {

    this.id = id;
  }

  public Object doExecute() {

    if (usr == null){
      usr = VOMSUserDAO.instance().delete(id);
    }
    else{
      VOMSUserDAO.instance().delete(usr);
    }
    
    EventManager.instance().dispatch(new UserDeletedEvent(usr));
    return usr;
  }

  public static DeleteUserOperation instance(VOMSUser u) {

    return new DeleteUserOperation(u);
  }

  public static DeleteUserOperation instance(Long id) {

    return new DeleteUserOperation(id);
  }

  public static DeleteUserOperation instance(String username, String userCa) {

    VOMSUser u = (VOMSUser) FindUserOperation.instance(username, userCa)
      .execute();
    if (u == null)
      throw new NoSuchUserException("User '" + username + "," + userCa
        + "' not found in database!");
    return new DeleteUserOperation(u);
  }
}
