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
package org.glite.security.voms.admin.view.actions.group;

import java.util.List;

import org.glite.security.voms.admin.operations.groups.ListGroupsOperation;
import org.glite.security.voms.admin.persistence.dao.VOMSAttributeDAO;
import org.glite.security.voms.admin.persistence.model.VOMSGroup;
import org.glite.security.voms.admin.persistence.model.attribute.VOMSAttributeDescription;
import org.glite.security.voms.admin.view.actions.BaseAction;
import org.hibernate.Hibernate;

import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;

public class GroupActionSupport extends BaseAction implements
  ModelDriven<VOMSGroup>, Preparable {

  /**
	 * 
	 */
  private static final long serialVersionUID = 1L;

  Long groupId = -1L;
  VOMSGroup group;

  List<VOMSAttributeDescription> attributeClasses;

  List<VOMSGroup> visibleGroups;
  
  public VOMSGroup getModel() {

    return group;
  }

  public void prepare() throws Exception {

    if (getModel() == null){
      if (getGroupId() != -1){
        group = groupById(getGroupId());
        Hibernate.initialize(group.getAttributes());
      }
    }

    attributeClasses = (List<VOMSAttributeDescription>) VOMSAttributeDAO
      .instance().getAllAttributeDescriptions();
    
    visibleGroups = (List<VOMSGroup>) ListGroupsOperation.instance().execute();
  }

  public Long getGroupId() {

    return groupId;
  }

  public void setGroupId(Long groupId) {

    this.groupId = groupId;
  }

  public List<VOMSAttributeDescription> getAttributeClasses() {

    return attributeClasses;
  }

  public void setAttributeClasses(
    List<VOMSAttributeDescription> attributeClasses) {

    this.attributeClasses = attributeClasses;
  }

  
  public List<VOMSGroup> getVisibleGroups() {
  
    return visibleGroups;
  }
  
}
