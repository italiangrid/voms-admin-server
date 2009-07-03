package org.glite.security.voms.admin.view.actions.user;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.glite.security.voms.admin.operations.users.DeleteUserAttributeOperation;
import org.glite.security.voms.admin.operations.users.SetUserAttributeOperation;
import org.glite.security.voms.admin.view.actions.BaseAction;


@ParentPackage("base")
@Results({
	@Result(name=BaseAction.SUCCESS,location="userDetail"),
	@Result(name=BaseAction.EDIT, location="userDetail"),
	@Result(name=BaseAction.INPUT, location="userDetail")
})
public class AttributeActions extends UserActionSupport{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	String attributeName;
	String attributeValue;
	
	
	
	@Action("set-attribute")
	public String setAttribute() throws Exception{
		
		SetUserAttributeOperation.instance(getModel(), attributeName, attributeValue).execute();
		
		return SUCCESS;
	}
	
	@Action("delete-attribute")
	public String deleteAttribute() throws Exception{
		
		DeleteUserAttributeOperation.instance(getModel(), attributeName).execute();
		
		return SUCCESS;
	}


	public String getAttributeName() {
		return attributeName;
	}

	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
	}

	public String getAttributeValue() {
		return attributeValue;
	}

	public void setAttributeValue(String attributeValue) {
		this.attributeValue = attributeValue;
	}

}
