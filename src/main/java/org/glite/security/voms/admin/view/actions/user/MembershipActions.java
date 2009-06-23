package org.glite.security.voms.admin.view.actions.user;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.glite.security.voms.admin.model.VOMSGroup;
import org.glite.security.voms.admin.model.VOMSRole;
import org.glite.security.voms.admin.model.VOMSUser;
import org.glite.security.voms.admin.operations.groups.AddMemberOperation;
import org.glite.security.voms.admin.operations.groups.RemoveMemberOperation;
import org.glite.security.voms.admin.operations.users.AssignRoleOperation;
import org.glite.security.voms.admin.operations.users.DismissRoleOperation;
import org.glite.security.voms.admin.view.actions.BaseAction;

import com.opensymphony.xwork2.ModelDriven;

@ParentPackage("base")
@Results({
	@Result(name=BaseAction.LIST,location="/user/search.action", type="redirect"),
	@Result(name=BaseAction.SUCCESS,location="userDetail"),
	@Result(name=BaseAction.EDIT, location="userDetail")
})
public class MembershipActions extends BaseAction implements ModelDriven<VOMSUser>{
	
	
	
	public static final Log log = LogFactory.getLog(MembershipActions.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	Long userId;
	Long groupId;
	Long roleId;
	
	VOMSUser model;
	
	@Action("add-to-group")	
	public String addToGroup() throws Exception{
		
		log.debug(String.format("userId: %d, groupId: %d, roleId: %d", userId, groupId, roleId));
		
		VOMSUser u = userById(userId);
		VOMSGroup g = groupById(groupId);
		
		AddMemberOperation.instance(u, g).execute();
		
		addActionMessage(getText("confirm.user.add_to_group", new String[]{u.toString(), g.toString()}));
		
		setModel(u);
		
		return SUCCESS;
	}
	
	@Action(value="remove-from-group")
	public String removeFromGroup() throws Exception{
		
		log.debug(String.format("userId: %d, groupId: %d, roleId: %d", userId, groupId, roleId));
		
		VOMSUser u = userById(userId);
		VOMSGroup g = groupById(groupId);
		
		RemoveMemberOperation.instance(u, g).execute();
		
		addActionMessage(getText("confirm.user.remove_from_group", new String[]{u.toString(), g.toString()}));
		
		setModel(u);
		
		return SUCCESS;	
		
	}
	
	@Action(value="assign-role")
	public String assignRole() throws Exception{
		
		log.debug(String.format("userId: %d, groupId: %d, roleId: %d", userId, groupId, roleId));
		
		VOMSUser u = userById(userId);
		VOMSGroup g = groupById(groupId);
		VOMSRole r = roleById(roleId);
		
		AssignRoleOperation.instance(u, g, r).execute();
		
		addActionMessage(getText("confirm.user.assign_role", new String[]{u.toString(), g.toString(), r.toString()}));
		
		setModel(u);
		
		return SUCCESS;
		
	}
	
	
	@Action("dismiss-role")
	public String dismissRole() throws Exception{
		
		log.debug(String.format("userId: %d, groupId: %d, roleId: %d", userId, groupId, roleId));
		
		VOMSUser u = userById(userId);
		VOMSGroup g = groupById(groupId);
		VOMSRole r = roleById(roleId);
		
		DismissRoleOperation.instance(u, g, r).execute();
		addActionMessage(getText("confirm.user.dismiss_role", new String[]{u.toString(), g.toString(), r.toString()}));
		
		setModel(u);
		
		return SUCCESS;
		
	}
	
	@Action(value="load")
	public String load() throws Exception{
		
		if (userId == null)
			return LIST;
		
		model = userById(userId);
		return EDIT;
	}
	
	public VOMSUser getModel() {
		return model;
	}

	public void setModel(VOMSUser model) {
		this.model = model;
	}

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

	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}
}
