package org.glite.security.voms.admin.view.actions.group;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.glite.security.voms.admin.model.VOMSGroup;
import org.glite.security.voms.admin.operations.groups.DeleteGroupOperation;
import org.glite.security.voms.admin.view.actions.BaseAction;

import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;

@ParentPackage("base")

@Results({ 
	@Result(name=BaseAction.SUCCESS,location="/group/search.action", type="redirect"),
	@Result(name=BaseAction.INPUT,location="groupCreate")
})

public class GroupActions extends BaseAction implements ModelDriven<VOMSGroup>, Preparable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final Log log = LogFactory.getLog(GroupActions.class);
	
	Long groupId;
	
	String groupName;
	String parentGroupName;
	
	VOMSGroup group;
	
	
	
	@Action(value="delete", interceptorRefs={@InterceptorRef(value="authenticatedStack", params={"tokenSession.includeMethods", "*"})})
	public String delete() throws Exception{
		
		log.debug(String.format("groupId: %d, groupName: %s", groupId, groupName));
		VOMSGroup g = (VOMSGroup)DeleteGroupOperation.instance(getGroupId()).execute();
		
		if (g != null)
			addActionMessage(getText("confirm.group.deletion", g.getName()));
			
		return SUCCESS;
	}

	public Long getGroupId() {
		return groupId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getParentGroupName() {
		return parentGroupName;
	}

	public void setParentGroupName(String parentGroupName) {
		this.parentGroupName = parentGroupName;
	}

	public VOMSGroup getModel() {
		return group;
	}

	public void prepare() throws Exception {
		
		if (getModel() == null){
			
			if (getGroupId() != -1)
				group = groupById(groupId);
			else if (getGroupName() != null && getParentGroupName() == null)
				group = groupByName(getGroupName());
			
		}
		
	}
	
	
	

}
