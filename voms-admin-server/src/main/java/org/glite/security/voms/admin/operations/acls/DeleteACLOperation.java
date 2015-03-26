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
package org.glite.security.voms.admin.operations.acls;

import org.glite.security.voms.admin.event.EventManager;
import org.glite.security.voms.admin.event.vo.acl.ACLDeletedEvent;
import org.glite.security.voms.admin.operations.BaseVomsOperation;
import org.glite.security.voms.admin.operations.VOMSContext;
import org.glite.security.voms.admin.operations.VOMSPermission;
import org.glite.security.voms.admin.persistence.HibernateFactory;
import org.glite.security.voms.admin.persistence.model.ACL;
import org.glite.security.voms.admin.persistence.model.VOMSAdmin;

public class DeleteACLOperation extends BaseVomsOperation {

  ACL acl;

  VOMSAdmin admin;

  VOMSPermission permission;

  public DeleteACLOperation(ACL acl, VOMSAdmin a, VOMSPermission p) {

    this.acl = acl;
    this.admin = a;
    this.permission = p;
  }

  protected Object doExecute() {

    acl.removePermissions(admin);

    HibernateFactory.getSession().save(acl);
    EventManager.instance().dispatch(new ACLDeletedEvent(acl));
    
    return acl;
  }

  public static DeleteACLOperation instance(ACL acl, VOMSAdmin a,
    VOMSPermission p) {

    return new DeleteACLOperation(acl, a, p);
  }

  protected void setupPermissions() {

    VOMSContext ctxt = VOMSContext.instance(acl.getGroup(), acl.getRole());
    VOMSPermission p = VOMSPermission.getEmptyPermissions()
      .setACLReadPermission().setACLWritePermission();
    addRequiredPermission(ctxt, p);

  }

}
