package org.glite.security.voms.admin.view.actions.apiv2;

import java.util.Collection;

import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.json.annotations.JSON;
import org.glite.security.voms.admin.operations.groups.CreateGroupOperation;
import org.glite.security.voms.admin.persistence.model.VOMSGroup;
import org.glite.security.voms.admin.view.actions.BaseAction;

import com.opensymphony.xwork2.validator.annotations.RegexFieldValidator;
import com.opensymphony.xwork2.validator.annotations.RequiredStringValidator;
import com.opensymphony.xwork2.validator.annotations.ValidatorType;

@ParentPackage("json")
@Results( { @Result(name = BaseAction.SUCCESS, type = "json"),
	@Result(name = BaseAction.INPUT, type = "json") })
public class CreateGroupAction extends BaseAction{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	String groupName;
	String groupDescription;
	
	@Override
	public String execute() throws Exception {
		
		if ("".equals(groupDescription.trim())){
			groupDescription=null;
		}
		
		CreateGroupOperation op  = CreateGroupOperation.instance(groupName, groupDescription, false);
		VOMSGroup g = (VOMSGroup) op.execute();
		
		addActionMessage(String.format("Group %s created succesfully.",g.getName()));
		
		return SUCCESS;
	}
	
	@JSON(serialize=false)
	@RequiredStringValidator(type = ValidatorType.FIELD, message = "Please provide a name for the group.")
	@RegexFieldValidator(type = ValidatorType.FIELD, message = "The group name field contains illegal characters!", expression = "^[^<>&=;]*$")
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	
	@JSON(serialize=false)
	@RequiredStringValidator(type = ValidatorType.FIELD, message = "Please provide a description for the group.")
	@RegexFieldValidator(type = ValidatorType.FIELD, message = "The group description field contains illegal characters!", expression = "^[^<>&=;]*$")
	public String getGroupDescription() {
		return groupDescription;
	}
	
	public void setGroupDescription(String groupDescription) {
		this.groupDescription = groupDescription;
	}
	
	@JSON(serialize=true)
	@Override
	public Collection<String> getActionMessages() {
		return super.getActionMessages();
	}
}
