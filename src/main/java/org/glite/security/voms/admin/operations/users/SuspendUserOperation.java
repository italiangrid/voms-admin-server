package org.glite.security.voms.admin.operations.users;

import org.glite.security.voms.admin.common.NullArgumentException;
import org.glite.security.voms.admin.model.VOMSUser;
import org.glite.security.voms.admin.model.VOMSUser.SuspensionReason;
import org.glite.security.voms.admin.operations.BaseVomsOperation;
import org.glite.security.voms.admin.operations.VOMSContext;
import org.glite.security.voms.admin.operations.VOMSPermission;

public class SuspendUserOperation extends BaseVomsOperation {

	VOMSUser user;

	SuspensionReason reason;

	private SuspendUserOperation(VOMSUser u, SuspensionReason r) {
		this.user = u;
		reason = r;

	}

	public static SuspendUserOperation instance(VOMSUser u, SuspensionReason r) {
		return new SuspendUserOperation(u, r);
	}

	@Override
	protected Object doExecute() {

		if (user == null)
			throw new NullArgumentException("User cannot be null!");
		if (reason == null)
			throw new NullArgumentException("Reason cannot be null!");

		user.suspend(reason);
		return null;
	}

	@Override
	protected void setupPermissions() {

		addRequiredPermission(VOMSContext.getVoContext(), VOMSPermission
				.getContainerReadPermission().setMembershipReadPermission()
				.setSuspendPermission());

	}

}
