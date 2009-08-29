package org.glite.security.voms.admin.view.actions.attribute;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.glite.security.voms.admin.operations.attributes.CreateAttributeDescriptionOperation;
import org.glite.security.voms.admin.operations.attributes.DeleteAttributeDescriptionOperation;
import org.glite.security.voms.admin.view.actions.BaseAction;

import com.opensymphony.xwork2.validator.annotations.RegexFieldValidator;
import com.opensymphony.xwork2.validator.annotations.RequiredStringValidator;
import com.opensymphony.xwork2.validator.annotations.ValidatorType;

@ParentPackage("base")
@Results( {
		@Result(name = BaseAction.SUCCESS, location = "/attribute/search.action", type = "redirect"),

		@Result(name = BaseAction.INPUT, location = "attributeManage"),

		@Result(name = BaseAction.LIST, location = "attributeManage")

})
public class AttributeActions extends BaseAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	Long attributeId;

	String attributeName;
	String attributeDescription;
	Boolean checkUniqueness;

	@Action(value = "create", interceptorRefs = { @InterceptorRef(value = "authenticatedStack", params = {
			"tokenSession.includeMethods", "create, delete" }) })
	public String create() throws Exception {

		CreateAttributeDescriptionOperation.instance(attributeName,
				attributeDescription, checkUniqueness).execute();

		return LIST;
	}

	@SkipValidation
	@Action(value = "delete", interceptorRefs = { @InterceptorRef(value = "authenticatedStack", params = {
			"tokenSession.includeMethods", "create, delete" }) })
	public String delete() throws Exception {

		DeleteAttributeDescriptionOperation.instance(attributeName).execute();

		return LIST;
	}

	public Long getAttributeId() {
		return attributeId;
	}

	public void setAttributeId(Long attributeId) {
		this.attributeId = attributeId;
	}

	@RequiredStringValidator(type = ValidatorType.FIELD, message = "Attribute name is required.")
	@RegexFieldValidator(type = ValidatorType.FIELD, message = "The attribute name field contains illegal characters!", expression = "^[^<>&=;]*$")
	public String getAttributeName() {
		return attributeName;
	}

	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
	}

	@RegexFieldValidator(type = ValidatorType.FIELD, message = "The attribute description field contains illegal characters!", expression = "^[^<>&=;]*$")
	public String getAttributeDescription() {
		return attributeDescription;
	}

	public void setAttributeDescription(String attributeDescription) {
		this.attributeDescription = attributeDescription;
	}

	public Boolean getCheckUniqueness() {
		return checkUniqueness;
	}

	public void setCheckUniqueness(Boolean checkUniqueness) {
		this.checkUniqueness = checkUniqueness;
	}
}
