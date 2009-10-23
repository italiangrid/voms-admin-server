/**
 * Copyright (c) Members of the EGEE Collaboration. 2006-2009.
 * See http://www.eu-egee.org/partners/ for details on the copyright holders.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Authors:
 * 	Andrea Ceccanti (INFN)
 */
package org.glite.security.voms.admin.view.actions.attribute;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.glite.security.voms.admin.dao.VOMSAttributeDAO;
import org.glite.security.voms.admin.model.VOMSAttributeDescription;
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
	
	public void validateCreate(){
		
		VOMSAttributeDescription desc = VOMSAttributeDAO.instance().getAttributeDescriptionByName(attributeName);
		if (desc!=null)
			addFieldError("attributeName", "Attribute class '"+attributeName+"' already exists!");
	}

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
