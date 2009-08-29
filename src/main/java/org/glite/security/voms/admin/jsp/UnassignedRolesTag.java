/*******************************************************************************
 *Copyright (c) Members of the EGEE Collaboration. 2006. 
 *See http://www.eu-egee.org/partners/ for details on the copyright
 *holders.  
 *
 *Licensed under the Apache License, Version 2.0 (the "License"); 
 *you may not use this file except in compliance with the License. 
 *You may obtain a copy of the License at 
 *
 *    http://www.apache.org/licenses/LICENSE-2.0 
 *
 *Unless required by applicable law or agreed to in writing, software 
 *distributed under the License is distributed on an "AS IS" BASIS, 
 *WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 *See the License for the specific language governing permissions and 
 *limitations under the License.
 *
 * Authors:
 *     Andrea Ceccanti - andrea.ceccanti@cnaf.infn.it
 *******************************************************************************/
package org.glite.security.voms.admin.jsp;

import java.util.List;
import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.glite.security.voms.admin.common.VOMSServiceConstants;

public class UnassignedRolesTag extends javax.servlet.jsp.tagext.TagSupport {

	public static final Log log = LogFactory.getLog(UnassignedRolesTag.class);

	/**
     * 
     */
	private static final long serialVersionUID = 1L;

	String id;

	String groupId;

	String var;

	public int doStartTag() throws JspException {

		List roles = null;

		Map unassignedRoles = (Map) pageContext
				.findAttribute(VOMSServiceConstants.UNASSIGNED_ROLES_KEY);

		if (unassignedRoles == null)
			throw new JspTagException(
					"Unassigned roles mappings not found in page context!");

		roles = (List) unassignedRoles.get(new Long(groupId));
		pageContext.setAttribute(var, roles);

		return SKIP_BODY;
	}

	public String getVar() {

		return var;
	}

	public void setVar(String var) {

		this.var = var;
	}

	public String getGroupId() {

		return groupId;
	}

	public void setGroupId(String groupId) {

		this.groupId = groupId;
	}

}
