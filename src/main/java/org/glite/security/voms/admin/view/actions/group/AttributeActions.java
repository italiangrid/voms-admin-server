package org.glite.security.voms.admin.view.actions.group;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.glite.security.voms.admin.operations.groups.DeleteGroupAttributeOperation;
import org.glite.security.voms.admin.operations.groups.SetGroupAttributeOperation;
import org.glite.security.voms.admin.view.actions.BaseAction;

import com.opensymphony.xwork2.validator.annotations.RegexFieldValidator;
import com.opensymphony.xwork2.validator.annotations.ValidatorType;

@ParentPackage("base")
@Results( { @Result(name = BaseAction.SUCCESS, location = "attributes.jsp"),
		@Result(name = BaseAction.INPUT, location = "attributes.jsp") })
public class AttributeActions extends GroupActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	String attributeName;

	String attributeValue;

	public String getAttributeName() {
		return attributeName;
	}

	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
	}

	@RegexFieldValidator(type = ValidatorType.FIELD, message = "This field contains illegal characters!", expression = "^[^<>&=;]*$")
	public String getAttributeValue() {
		return attributeValue;
	}

	public void setAttributeValue(String attributeValue) {
		this.attributeValue = attributeValue;
	}

	@Action("set-attribute")
	public String setAttribute() throws Exception {

		SetGroupAttributeOperation.instance(getModel(), getAttributeName(),
				null, getAttributeValue()).execute();
		return SUCCESS;
	}

	@Action("delete-attribute")
	public String deleteAttribute() throws Exception {

		DeleteGroupAttributeOperation.instance(getModel(), getAttributeName())
				.execute();
		return SUCCESS;
	}
}
