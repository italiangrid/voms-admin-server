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

import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.glite.security.voms.admin.error.VOMSAuthorizationException;
import org.glite.security.voms.admin.event.EventManager;
import org.glite.security.voms.admin.event.vo.acl.ACLUpdatedEvent;
import org.glite.security.voms.admin.operations.BaseVomsOperation;
import org.glite.security.voms.admin.operations.VOMSPermission;
import org.glite.security.voms.admin.persistence.dao.ACLDAO;
import org.glite.security.voms.admin.persistence.dao.VOMSGroupDAO;
import org.glite.security.voms.admin.persistence.model.ACL;
import org.glite.security.voms.admin.persistence.model.VOMSAdmin;
import org.glite.security.voms.admin.persistence.model.VOMSGroup;

public class SaveACLEntryOperation extends BaseVomsOperation {

  private static final Logger log = LoggerFactory
    .getLogger(SaveACLEntryOperation.class);

  private ACL acl;
  private VOMSAdmin admin;
  private VOMSPermission perms;

  private boolean recursive = false;

  protected Object doExecute() {

    ACLDAO.instance().saveACLEntry(acl, admin, perms);
    EventManager.instance().dispatch(new ACLUpdatedEvent(acl));

    if (isRecursive() && acl.getContext().isGroupContext()) {

      try {

        List<VOMSGroup> childrenGroups = VOMSGroupDAO.instance().getChildren(
          acl.getGroup());
        
        Iterator<VOMSGroup> childIter = childrenGroups.iterator();

        while (childIter.hasNext()) {

          VOMSGroup childGroup = childIter.next();
          SaveACLEntryOperation op = instance(childGroup.getACL(), admin,
            perms, true);
          op.execute();

        }

      } catch (VOMSAuthorizationException e) {

        log.warn("Authorization Error saving recursively ACL entry !", e);

      } catch (RuntimeException e) {

        throw e;
      }
    }
    return acl;

  }

  protected void setupPermissions() {

    VOMSPermission requiredPerms = null;

    if (acl.isDefautlACL())
      requiredPerms = VOMSPermission.getEmptyPermissions()
        .setACLDefaultPermission().setACLReadPermission()
        .setACLWritePermission();
    else
      requiredPerms = VOMSPermission.getEmptyPermissions()
        .setACLReadPermission().setACLWritePermission();

    addRequiredPermission(acl.getContext(), requiredPerms);
  }

  private SaveACLEntryOperation(ACL acl, VOMSAdmin admin, VOMSPermission perms,
    boolean recursive) {

    this.acl = acl;
    this.admin = admin;
    this.perms = perms;
    this.recursive = recursive;

  }

  public static SaveACLEntryOperation instance(ACL acl, VOMSAdmin admin,
    VOMSPermission perms) {

    return new SaveACLEntryOperation(acl, admin, perms, false);
  }

  public static SaveACLEntryOperation instance(ACL acl, VOMSAdmin admin,
    VOMSPermission perms, boolean recursive) {

    return new SaveACLEntryOperation(acl, admin, perms, recursive);
  }

  public boolean isRecursive() {

    return recursive;
  }

  public void setRecursive(boolean recursive) {

    this.recursive = recursive;
  }

}
