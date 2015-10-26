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

public class DeleteACLEntryOperation extends BaseVomsOperation {

  private static final Logger log = LoggerFactory
    .getLogger(DeleteACLEntryOperation.class);

  private ACL acl;
  private VOMSAdmin admin;

  private boolean recursive = false;

  protected Object doExecute() {

    if (!isRecursive()) {

      log.debug("Removing ACL entry for admin '" + admin + "' in ACL '"
        + acl.getContext() + "'.");
      ACLDAO.instance().deleteACLEntry(acl, admin);
      EventManager.instance().dispatch(new ACLUpdatedEvent(acl));
      return acl;

    }

    // Recursive behaviour
    try {

      if (!acl.getContext().isGroupContext()) {

        log.debug("Removing ACL entry for admin '" + admin + "' in ACL '"
          + acl.getContext() + "' [recursive].");
        ACLDAO.instance().deleteACLEntry(acl, admin);
        EventManager.instance().dispatch(new ACLUpdatedEvent(acl));

        return acl;

      } else {

        List<VOMSGroup> childrenGroups = VOMSGroupDAO.instance().getChildren(
          acl.getGroup());
        Iterator<VOMSGroup> childIter = childrenGroups.iterator();

        while (childIter.hasNext()) {

          VOMSGroup childGroup = (VOMSGroup) childIter.next();
          DeleteACLEntryOperation op = instance(childGroup.getACL(), admin,
            recursive);
          op.execute();
        }

        log.debug("Removing ACL entry for admin '" + admin + "' in ACL '"
          + acl.getContext() + "' [recursive].");
        ACLDAO.instance().deleteACLEntry(acl, admin);
        EventManager.instance().dispatch(new ACLUpdatedEvent(acl));

        return acl;
      }

    } catch (VOMSAuthorizationException e) {
      log.warn("Authorization Error deleting recursively ACL entry", e);
    }

    return null;
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

  private DeleteACLEntryOperation(ACL acl, VOMSAdmin admin) {

    this.acl = acl;
    this.admin = admin;
  }

  private DeleteACLEntryOperation(ACL acl, VOMSAdmin admin, boolean propagate) {

    this.acl = acl;
    this.admin = admin;
    this.recursive = propagate;
  }

  public static DeleteACLEntryOperation instance(ACL acl, VOMSAdmin admin) {

    return new DeleteACLEntryOperation(acl, admin);
  }

  public static DeleteACLEntryOperation instance(ACL acl, VOMSAdmin admin,
    boolean propagate) {

    return new DeleteACLEntryOperation(acl, admin, propagate);
  }

  protected boolean isRecursive() {

    return recursive;
  }

}
