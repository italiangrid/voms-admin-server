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
package org.glite.security.voms.admin.view.actions.aup;

import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.glite.security.voms.admin.dao.VOMSUserDAO;
import org.glite.security.voms.admin.dao.generic.AUPDAO;
import org.glite.security.voms.admin.dao.generic.DAOFactory;
import org.glite.security.voms.admin.error.VOMSException;
import org.glite.security.voms.admin.operations.CurrentAdmin;
import org.glite.security.voms.admin.persistence.model.AUP;
import org.glite.security.voms.admin.persistence.model.VOMSUser;
import org.glite.security.voms.admin.view.actions.BaseAction;

import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import com.opensymphony.xwork2.validator.annotations.ExpressionValidator;
import com.opensymphony.xwork2.validator.annotations.FieldExpressionValidator;
import com.opensymphony.xwork2.validator.annotations.RegexFieldValidator;
import com.opensymphony.xwork2.validator.annotations.RequiredFieldValidator;
import com.opensymphony.xwork2.validator.annotations.ValidatorType;

@ParentPackage("base")
@Results( {
		@Result(name = BaseAction.INPUT, location = "signAup"),
		@Result(name = BaseAction.SUCCESS, location = "/home/login.action", type = "redirect") })
public class SignAction extends BaseAction implements ModelDriven<AUP>,
		Preparable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	Long aupId;

	String aupAccepted;

	AUP aup;

	@Override
	public String execute() throws Exception {

		VOMSUser u = CurrentAdmin.instance().getVoUser();

		if (u == null)
			throw new VOMSException(
					"Current authenticated client is not a member of the VO and, as such, cannot be entitled to sign AUP for the VO.");

		if (aupAccepted.equals("true"))
			VOMSUserDAO.instance().signAUP(u, aup);
		else {

			addFieldError("aupAccepted",
					"You have to accept the terms of the AUP to proceed!");
			return INPUT;
		}

		return SUCCESS;
	}

	public AUP getModel() {

		return aup;
	}

	public Long getAupId() {
		return aupId;
	}

	public void setAupId(Long aupId) {
		this.aupId = aupId;
	}

	public void prepare() throws Exception {
		if (aup == null) {
			AUPDAO dao = DAOFactory.instance().getAUPDAO();
			aup = dao.findById(aupId, false);
		}
	}

	@RequiredFieldValidator(type = ValidatorType.FIELD, message = "You must sign the AUP.")
	@RegexFieldValidator(type = ValidatorType.FIELD, expression = "^true$", message = "You must accept the terms of the AUP to proceed")
	public String getAupAccepted() {
		return aupAccepted;
	}

	public void setAupAccepted(String aupAccepted) {
		this.aupAccepted = aupAccepted;
	}

}
