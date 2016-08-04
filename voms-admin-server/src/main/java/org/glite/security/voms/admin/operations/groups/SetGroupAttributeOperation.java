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
package org.glite.security.voms.admin.operations.groups;

import org.glite.security.voms.admin.event.EventManager;
import org.glite.security.voms.admin.event.vo.group.GroupAttributeSetEvent;
import org.glite.security.voms.admin.operations.BaseAttributeRWOperation;
import org.glite.security.voms.admin.operations.VOMSContext;
import org.glite.security.voms.admin.operations.VOMSOperation;
import org.glite.security.voms.admin.persistence.dao.VOMSGroupDAO;
import org.glite.security.voms.admin.persistence.error.NoSuchGroupException;
import org.glite.security.voms.admin.persistence.model.VOMSGroup;
import org.glite.security.voms.admin.persistence.model.VOMSGroupAttribute;
import org.glite.security.voms.service.attributes.AttributeValue;

public class SetGroupAttributeOperation extends BaseAttributeRWOperation {

  String attributeName;

  String attributeDescription;

  String attributeValue;

  private SetGroupAttributeOperation(VOMSGroup g, String aName, String aDesc,
    String aValue) {

    super(VOMSContext.instance(g));
    attributeName = aName;
    attributeDescription = aDesc;
    attributeValue = aValue;
  }

  public Object doExecute() {

    VOMSGroupAttribute ga = VOMSGroupDAO.instance().setAttribute(
      __context.getGroup(), attributeName, attributeValue);

    EventManager.instance().dispatch(new GroupAttributeSetEvent(__context.getGroup(), ga));

    return ga;
  }

  public static VOMSOperation instance(String groupName, AttributeValue value) {

    VOMSGroup g = (VOMSGroup) FindGroupOperation.instance(groupName).execute();

    if (g == null)
      throw new NoSuchGroupException("Group '" + groupName
        + "' does not exist in this vo.");
    return new SetGroupAttributeOperation(g, value.getAttributeClass()
      .getName(), value.getAttributeClass().getDescription(), value.getValue());
  }

  public static VOMSOperation instance(VOMSGroup g, String aName, String aDesc,
    String aValue) {

    return new SetGroupAttributeOperation(g, aName, aDesc, aValue);
  }
}
