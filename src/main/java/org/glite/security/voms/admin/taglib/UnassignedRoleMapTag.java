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
package org.glite.security.voms.admin.taglib;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.glite.security.voms.admin.error.VOMSAuthorizationException;
import org.glite.security.voms.admin.operations.users.FindUnassignedRoles;
import org.glite.security.voms.admin.operations.users.FindUserOperation;
import org.glite.security.voms.admin.persistence.dao.VOMSUserDAO;
import org.glite.security.voms.admin.persistence.model.VOMSGroup;
import org.glite.security.voms.admin.persistence.model.VOMSUser;

public class UnassignedRoleMapTag extends TagSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	String userId;
	String var;

	@Override
	public int doStartTag() throws JspException {

		VOMSUser u = VOMSUserDAO.instance().findById(Long.parseLong(userId));
		
		Map unassignedRoles = new HashMap();

		Iterator<VOMSGroup> groups = u.getGroups().iterator();

		while (groups.hasNext()) {

			VOMSGroup g = (VOMSGroup) groups.next();
			List roles;
			
			try{
				
				roles = (List) FindUnassignedRoles.instance(u.getId(),
						g.getId()).execute();
			
			}catch(VOMSAuthorizationException e){
				roles= Collections.EMPTY_LIST;
			}

			unassignedRoles.put(g.getId(), roles);
		}

		pageContext.setAttribute(var, unassignedRoles);

		return SKIP_BODY;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getVar() {
		return var;
	}

	public void setVar(String var) {
		this.var = var;
	}

}
