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

import org.glite.security.voms.admin.dao.generic.AUPDAO;
import org.glite.security.voms.admin.dao.generic.DAOFactory;
import org.glite.security.voms.admin.persistence.model.AUP;
import org.glite.security.voms.admin.view.actions.BaseAction;

import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import com.opensymphony.xwork2.validator.annotations.RegexFieldValidator;
import com.opensymphony.xwork2.validator.annotations.RequiredStringValidator;
import com.opensymphony.xwork2.validator.annotations.ValidatorType;

public abstract class AUPVersionActions extends BaseAction implements
		Preparable, ModelDriven<AUP> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	Long aupId;

	AUP aup;

	String version;

	public void prepare() throws Exception {
		if (aup == null) {

			AUPDAO dao = DAOFactory.instance().getAUPDAO();
			aup = dao.findById(aupId, false);
		}

	}

	public AUP getModel() {

		return aup;
	}

	@RequiredStringValidator(type = ValidatorType.FIELD, message = "The version string is required")
	@RegexFieldValidator(type = ValidatorType.FIELD, message = "The version field contains illegal characters!", expression = "^[^<>&=;]*$")
	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public Long getAupId() {
		return aupId;
	}

	public void setAupId(Long aupId) {
		this.aupId = aupId;
	}

}
