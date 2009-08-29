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

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.PageContext;

import org.glite.security.voms.admin.common.VOMSSyntaxException;
import org.glite.security.voms.admin.operations.CurrentAdmin;
import org.glite.security.voms.admin.operations.VOMSContext;
import org.glite.security.voms.admin.operations.VOMSPermission;

public class TagUtils {

	// CONSTANTS
	public static final String CONTAINER_RW_PERMISSIONS = "rw";

	public static final String CONTAINER_R_PERMISSIONS = "r";

	public static final String CONTAINER_W_PERMISSIONS = "w";

	public static boolean hasPermissions(PageContext pageContext,
			Map permissionMap) throws JspException {

		if (permissionMap.isEmpty())
			return false;

		Iterator i = permissionMap.entrySet().iterator();

		while (i.hasNext()) {

			Map.Entry entry = (Entry) i.next();
			if (!isAuthorized(pageContext, (String) entry.getKey(),
					(String) entry.getValue()))
				return false;
		}

		return true;

	}

	public static boolean isAuthorized(PageContext pageContext,
			String vomsContext, String permissions) throws JspException {

		CurrentAdmin admin = (CurrentAdmin) CurrentAdmin.instance();

		if (admin == null)
			throw new JspTagException(
					"No admin defined in the org.glite.security.voms.admin.request context!");

		return admin.hasPermissions(buildContext(vomsContext),
				buildPermissions(permissions));

	}

	public static VOMSContext buildContext(String context) throws JspException {

		if (context.equals("vo"))
			return VOMSContext.getVoContext();

		else {

			if (context.matches("^vo/.*$"))
				context = context.replace("vo", VOMSContext.getVoContext()
						.getGroup().getName());
			try {

				return VOMSContext.instance(context);

			} catch (VOMSSyntaxException e) {

				throw new JspTagException(
						"VOMS security context creation error (context: "
								+ context + "): " + e.getMessage());

			} catch (IllegalArgumentException e) {

				throw new JspTagException(
						"VOMS security context creation error (context: "
								+ context + "): " + e.getMessage());

			}
		}
	}

	public static VOMSPermission buildPermissions(String permission)
			throws JspException {

		try {

			if (permission.equals(CONTAINER_R_PERMISSIONS))
				return VOMSPermission
						.fromString("CONTAINER_READ|MEMBERSHIP_READ");

			if (permission.equals(CONTAINER_W_PERMISSIONS))
				return VOMSPermission
						.fromString("CONTAINER_WRITE|MEMBERSHIP_WRITE");

			if (permission.equals(CONTAINER_RW_PERMISSIONS))
				return VOMSPermission
						.fromString("CONTAINER_READ|CONTAINER_WRITE|MEMBERSHIP_READ|MEMBERSHIP_WRITE");

			return VOMSPermission.fromString(permission);

		} catch (IllegalArgumentException e) {

			throw new JspTagException(
					"VOMS permission parse error (permission: " + permission
							+ "): " + e.getMessage());

		}
	}

}
