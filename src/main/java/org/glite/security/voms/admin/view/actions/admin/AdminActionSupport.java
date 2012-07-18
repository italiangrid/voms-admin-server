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
package org.glite.security.voms.admin.view.actions.admin;

import org.glite.security.voms.admin.operations.CurrentAdmin;
import org.glite.security.voms.admin.persistence.model.VOMSAdmin;
import org.glite.security.voms.admin.view.actions.BaseAction;

import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;


public class AdminActionSupport extends BaseAction implements
		ModelDriven<VOMSAdmin>, Preparable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	VOMSAdmin admin;

	public VOMSAdmin getModel() {

		return admin;
	}

	public void prepare() throws Exception {

		admin = CurrentAdmin.instance().getAdmin();

	}

	public String getRealSubject(){
		
		return CurrentAdmin.instance().getRealSubject();
	}
}
