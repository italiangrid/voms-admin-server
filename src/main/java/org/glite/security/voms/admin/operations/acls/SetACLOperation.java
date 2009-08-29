package org.glite.security.voms.admin.operations.acls;

import java.util.Map;

import org.glite.security.voms.admin.common.NullArgumentException;
import org.glite.security.voms.admin.common.VOMSAuthorizationException;
import org.glite.security.voms.admin.common.VOMSException;
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
