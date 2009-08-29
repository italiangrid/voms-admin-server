package org.glite.security.voms.admin.operations.users;

import org.glite.security.voms.admin.model.VOMSUser;
import org.glite.security.voms.admin.operations.BaseVomsOperation;
import org.glite.security.voms.admin.operations.VOMSContext;
import org.glite.security.voms.admin.operations.VOMSPermission;

public class RestoreUserOperation extends BaseVomsOperation {

	VOMSUser user;

	private RestoreUserOperation(VOMSUser u) {
		user = u;
	}

	public static RestoreUserOperation instance(VOMSUser u) {

		return new RestoreUserOperation(u);
	}

	@Override
	protected Object doExecute() {

		user.restore();

		return null;
	}

	@Override
	protected void setupPermissions() {
		addRequiredPermission(VOMSContext.getVoContext(), VOMSPermission
				.getContainerReadPermission().setMembershipReadPermission()
				.setSuspendPermission());
	}

}
