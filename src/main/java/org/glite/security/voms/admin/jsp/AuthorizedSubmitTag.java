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

import java.util.HashMap;
import java.util.Map;

import javax.servlet.jsp.JspException;

import org.apache.struts.taglib.html.SubmitTag;

public class AuthorizedSubmitTag extends SubmitTag implements AuthorizableTag{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	Map permissionMap = new HashMap();
	
	private String context;
	
	private String permission;

	
	public int doStartTag() throws JspException {
		
		if (context != null && permission != null){
			
			setDisabled(!TagUtils.isAuthorized(pageContext,context,permission));
			
			return super.doStartTag();
		}
		
		return EVAL_BODY_INCLUDE;
		
	}
	
	
	
	public int doEndTag() throws JspException {
		
		if (context != null && permission != null)
			return super.doEndTag();
		
		setDisabled(!TagUtils.hasPermissions(pageContext, permissionMap));
		
		return EVAL_PAGE;
	}
	
	public String getContext() {
		return context;
	}

	public void setContext(String context) {
		this.context = context;
	}

	public String getPermission() {
		return permission;
	}

	public void setPermission(String permission) {
		this.permission = permission;
	}



	public Map getPermissionMap() {
		return permissionMap;
	}
	
	
}
