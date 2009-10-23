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
package org.glite.security.voms.admin.jsp;

import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.TagSupport;

public class PermissionTag extends TagSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	String context;

	String permission;

	public String getContext() {
		return context;
	}

	public void setContext(String context) {
		this.context = context;
	}

	public String getPermission() {
		return permission;
	}

	public void setPermission(String permissions) {
		this.permission = permissions;
	}

	public int doStartTag() throws JspException {

		AuthorizableTag parentTag = (AuthorizableTag) getParent();

		if (parentTag == null)
			throw new JspTagException(
					"The permission tag may be used only in the context of the link, authorized, or submit tags!");

		Map m = parentTag.getPermissionMap();

		if (m == null)
			throw new JspTagException(
					"Permission map not initialized in parent element!");

		m.put(context, permission);

		return SKIP_BODY;

	}

}
