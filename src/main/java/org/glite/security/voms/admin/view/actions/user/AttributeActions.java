package org.glite.security.voms.admin.view.actions.user;

import java.util.List;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.glite.security.voms.admin.dao.VOMSAttributeDAO;
import org.glite.security.voms.admin.model.VOMSAttributeDescription;
import org.glite.security.voms.admin.operations.users.DeleteUserAttributeOperation;
import org.glite.security.voms.admin.operations.users.SetUserAttributeOperation;
import org.glite.security.voms.admin.view.actions.BaseAction;

import com.opensymphony.xwork2.validator.annotations.RegexFieldValidator;
import com.opensymphony.xwork2.validator.annotations.ValidatorType;

@ParentPackage("base")
@Results( { @Result(name = BaseAction.SUCCESS, location = "attributes.jsp"),
		@Result(name = BaseAction.INPUT, location = "attributes.jsp") })
public class AttributeActions extends UserActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	String attributeName;

	String attributeValue;

	List<VOMSAttributeDescription> attributeClasses;

	@Action("set-attribute")
	public String setAttribute() throws Exception {

		SetUserAttributeOperation.instance(getModel(), attributeName,
				attributeValue).execute();

		return SUCCESS;
	}

	@Action("delete-attribute")
	public String deleteAttribute() throws Exception {

		DeleteUserAttributeOperation.instance(getModel(), attributeName)
				.execute();

		return SUCCESS;
	}

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

	public List<VOMSAttributeDescription> getAttributeClasses() {
		return attributeClasses;
	}

	public void setAttributeClasses(
			List<VOMSAttributeDescription> attributeClasses) {
		this.attributeClasses = attributeClasses;
	}

	@Override
	public void prepare() throws Exception {

		super.prepare();
		attributeClasses = VOMSAttributeDAO.instance()
				.getAllAttributeDescriptions();

	}
}
