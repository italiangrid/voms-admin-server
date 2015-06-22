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
package org.glite.security.voms.admin.operations.roles;

import org.apache.commons.lang.Validate;
import org.glite.security.voms.admin.event.EventManager;
import org.glite.security.voms.admin.event.vo.role.RoleAttributeSetEvent;
import org.glite.security.voms.admin.operations.BaseAttributeRWOperation;
import org.glite.security.voms.admin.operations.VOMSContext;
import org.glite.security.voms.admin.operations.groups.FindGroupOperation;
import org.glite.security.voms.admin.persistence.dao.VOMSRoleDAO;
import org.glite.security.voms.admin.persistence.error.NoSuchGroupException;
import org.glite.security.voms.admin.persistence.error.NoSuchRoleException;
import org.glite.security.voms.admin.persistence.model.VOMSGroup;
import org.glite.security.voms.admin.persistence.model.VOMSRole;
import org.glite.security.voms.admin.persistence.model.VOMSRoleAttribute;
import org.glite.security.voms.service.attributes.AttributeValue;

public class SetRoleAttributeOperation extends BaseAttributeRWOperation {

  String attributeName;

  String attributeValue;

  protected Object doExecute() {

    VOMSRoleAttribute ra = VOMSRoleDAO.instance().setAttribute(
      __context.getRole(), __context.getGroup(), attributeName, attributeValue);

    EventManager.instance().dispatch(new RoleAttributeSetEvent(__context.getRole(), ra));

    return ra;
  }

  private SetRoleAttributeOperation(VOMSGroup g, VOMSRole r, String aName,
    String aValue) {

    super(VOMSContext.instance(g, r));

    attributeName = aName;
    attributeValue = aValue;

  }

  public static SetRoleAttributeOperation instance(String groupName,
    String roleName, AttributeValue val) {

    Validate.notNull(val, "attribute value must be non-null!");

    VOMSGroup g = (VOMSGroup) FindGroupOperation.instance(groupName).execute();
    VOMSRole r = (VOMSRole) FindRoleOperation.instance(roleName).execute();

    if (g == null)
      throw new NoSuchGroupException("Group '" + groupName + "' not found!");

    if (r == null)
      throw new NoSuchRoleException("Role '" + roleName + "' not found!");

    return new SetRoleAttributeOperation(g, r, val.getAttributeClass()
      .getName(), val.getValue());

  }

  public static SetRoleAttributeOperation instance(VOMSGroup g, VOMSRole r,
    String aName, String aValue) {

    return new SetRoleAttributeOperation(g, r, aName, aValue);
  }

}
