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
package org.glite.security.voms.admin.operations.acls;

import java.util.Map;

import org.glite.security.voms.admin.error.NullArgumentException;
import org.glite.security.voms.admin.error.VOMSAuthorizationException;
import org.glite.security.voms.admin.error.VOMSException;
import org.glite.security.voms.admin.model.ACL;
import org.glite.security.voms.admin.model.VOMSAdmin;
import org.glite.security.voms.admin.operations.BaseVomsOperation;
import org.glite.security.voms.admin.operations.VOMSPermission;

public class SetACLOperation extends BaseVomsOperation {

	Map<VOMSAdmin, VOMSPermission> permissions;
	ACL theACL;

	private SetACLOperation(ACL acl, Map<VOMSAdmin, VOMSPermission> perms) {

		if (acl == null)
			throw new NullArgumentException("acl cannot be null!");

		if (perms == null)
			throw new NullArgumentException("perms cannot be null!");

		theACL = acl;
		permissions = perms;
	}

	public static SetACLOperation instance(ACL acl,
			Map<VOMSAdmin, VOMSPermission> perms) {

		return new SetACLOperation(acl, perms);
	}

	@Override
	protected Object doExecute() {

		if (permissions.isEmpty() && !theACL.isDefautlACL())
			throw new VOMSException(
					"Will not replace the current ACL with an empty one.");

		// Drop old permissions
		theACL.getPermissions().clear();

		// Set the new permission set
		theACL.getPermissions().putAll(permissions);

		return null;
	}

	@Override
	protected void setupPermissions() {

		VOMSPermission perms = VOMSPermission.getEmptyPermissions()
				.setACLReadPermission().setACLWritePermission();

		if (theACL.isDefautlACL())
			perms.setACLDefaultPermission();

		addRequiredPermission(theACL.getContext(), perms);

	}

}
