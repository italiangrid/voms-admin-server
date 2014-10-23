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
package org.glite.security.voms.admin.operations.groups;

import org.glite.security.voms.admin.operations.BaseContainerReadOperation;
import org.glite.security.voms.admin.operations.VOMSContext;
import org.glite.security.voms.admin.persistence.dao.VOMSGroupDAO;
import org.glite.security.voms.admin.persistence.error.NoSuchGroupException;
import org.glite.security.voms.admin.persistence.model.VOMSGroup;

public class ListChildrenGroupsOperation extends BaseContainerReadOperation {

  private ListChildrenGroupsOperation(VOMSGroup g) {

    super(VOMSContext.instance(g));
  }

  protected Object doExecute() {

    return VOMSGroupDAO.instance().getChildren(__context.getGroup());
  }

  public static ListChildrenGroupsOperation instance(VOMSGroup g) {

    return new ListChildrenGroupsOperation(g);
  }

  public static ListChildrenGroupsOperation instance(String groupName) {

    VOMSGroup g = (VOMSGroup) FindGroupOperation.instance(groupName).execute();
    if (g == null)
      throw new NoSuchGroupException("Group '" + groupName
        + "' is not defined in database!");
    return new ListChildrenGroupsOperation(g);

  }
}
