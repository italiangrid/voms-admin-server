package org.glite.security.voms.admin.view.actions.user;

import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.glite.security.voms.admin.database.AttributeValueAlreadyAssignedException;
import org.glite.security.voms.admin.operations.users.SetUserAttributeOperation;
import org.glite.security.voms.admin.view.actions.BaseAction;

@ParentPackage("base")
@Results( { @Result(name = BaseAction.SUCCESS, location = "attributes.jsp"),
		@Result(name = BaseAction.INPUT, location ="attributes.jsp") })
public class SetAttributeAction extends AttributeActions {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	
	@Override
	public String execute() throws Exception {
		
		try{
			SetUserAttributeOperation.instance(getModel(), attributeName,
					attributeValue).execute();
		
		}catch (AttributeValueAlreadyAssignedException e){
			addFieldError("attributeValue", "The attribute value '"+attributeValue+"' is already assigned to another user!");
			return INPUT;
		}
		
		addActionMessage(getText("confirm.user.attribute_set", new String[]{attributeName, getModel().getShortName()}));
		
		return SUCCESS;
	}

}
