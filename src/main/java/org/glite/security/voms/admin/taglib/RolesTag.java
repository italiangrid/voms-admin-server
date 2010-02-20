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

import java.util.Set;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.TagSupport;

import org.glite.security.voms.admin.model.VOMSGroup;
import org.glite.security.voms.admin.model.VOMSUser;

public class RolesTag extends TagSupport {

	/**
     * 
     */
	private static final long serialVersionUID = 1L;

	String user;

	String group;

	String var;

	public RolesTag() {

		super();
		// TODO Auto-generated constructor stub
	}

	public String getGroup() {

		return group;
	}

	public void setGroup(String group) {

		this.group = group;
	}

	public String getUser() {

		return user;
	}

	public void setUser(String user) {

		this.user = user;
	}

	public String getVar() {

		return var;
	}

	public void setVar(String var) {

		this.var = var;
	}

	public int doStartTag() throws JspException {

		VOMSUser vomsUser = (VOMSUser) pageContext.getAttribute(user,
				PageContext.REQUEST_SCOPE);

		VOMSGroup vomsGroup = (VOMSGroup) pageContext.getAttribute(group);

		if (vomsUser == null)
			throw new JspTagException(
					"No user found in org.glite.security.voms.admin.request context!");

		if ((vomsGroup == null))
			throw new JspTagException("No group found in pageContext!");

		Set roles = vomsUser.getRoles(vomsGroup);

		pageContext.setAttribute(var, roles);

		return SKIP_BODY;
	}

}
