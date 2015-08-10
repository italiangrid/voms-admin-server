package org.glite.security.voms.admin.view.actions.user;

import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.glite.security.voms.admin.operations.groups.FindGroupOperation;
import org.glite.security.voms.admin.persistence.model.VOMSGroup;
import org.glite.security.voms.admin.view.actions.BaseAction;

@Results({ @Result(name = BaseAction.SUCCESS, location = "prepareGroupRequest") })
public class PrepareGroupMembershipRequestAction extends BaseAction {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  Long userId = -1L;
  Long groupId = -1L;

  VOMSGroup group;

  public Long getUserId() {

    return userId;
  }

  public void setUserId(Long userId) {

    this.userId = userId;
  }

  public Long getGroupId() {

    return groupId;
  }

  public void setGroupId(Long groupId) {

    this.groupId = groupId;
  }

  @Override
  public String execute() throws Exception {

    group = (VOMSGroup) FindGroupOperation.instance(groupId).execute();

    if (group == null) {
      addActionError("No group found for id  " + groupId);
    }

    return SUCCESS;
  }

  public VOMSGroup getGroup() {

    return group;
  }

}
