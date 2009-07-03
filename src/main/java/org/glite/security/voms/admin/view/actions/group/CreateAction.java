package org.glite.security.voms.admin.view.actions.group;


import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.glite.security.voms.admin.model.VOMSGroup;
import org.glite.security.voms.admin.operations.groups.CreateGroupOperation;
import org.glite.security.voms.admin.view.actions.BaseAction;

import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import com.opensymphony.xwork2.validator.annotations.RegexFieldValidator;
import com.opensymphony.xwork2.validator.annotations.RequiredStringValidator;
import com.opensymphony.xwork2.validator.annotations.ValidatorType;


@ParentPackage("base")

@Results({ 
	@Result(name=BaseAction.SUCCESS,location="/group/search.action", type="redirect"),
	@Result(name=BaseAction.INPUT,location="groupCreate")
})

@Action(value="create", interceptorRefs={@InterceptorRef(value="authenticatedStack", params={"tokenSession.includeMethods", "execute"})})
public class CreateAction extends BaseAction implements ModelDriven<VOMSGroup>, Preparable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	VOMSGroup group;
	
	
	String groupName;
	String parentGroupName;
	
	
	@Override
	public String execute() throws Exception {
	
		if (groupName == null && parentGroupName == null)
			return INPUT;
		
		String name = getParentGroupName()+"/"+getGroupName();
		
		VOMSGroup g = (VOMSGroup)CreateGroupOperation.instance(name).execute();
		
		if ( g != null)
			addActionMessage(getText("confirm.group.creation", g.getName()));
		
		return SUCCESS;
		
	}
	
	public VOMSGroup getModel() {
		
		return group;
	}

	public void prepare() throws Exception {
		
		
	}

	@RequiredStringValidator(type=ValidatorType.FIELD,message="A name for the group is required!")
	@RegexFieldValidator(type=ValidatorType.FIELD, message="The group name field contains illegal characters!", expression="^[^<>&=;]*$")
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
	
	

}
