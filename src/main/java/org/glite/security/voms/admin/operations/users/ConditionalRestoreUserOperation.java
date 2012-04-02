package org.glite.security.voms.admin.operations.users;

import org.glite.security.voms.admin.persistence.model.VOMSUser;
import org.glite.security.voms.admin.persistence.model.VOMSUser.SuspensionReason;

public class ConditionalRestoreUserOperation extends RestoreUserOperation {

	private SuspensionReason suspensionReason;

	private ConditionalRestoreUserOperation(VOMSUser u,
			SuspensionReason suspensionReason) {
		super(u);
		this.suspensionReason = suspensionReason;
	}

	private ConditionalRestoreUserOperation(Long userId,
			SuspensionReason suspensionReason) {
		super(userId);
		this.suspensionReason = suspensionReason;
	}

	@Override
	protected Object doExecute() {

		boolean userHasBeenRestored = false;

		if (user.isSuspended()
				&& user.getSuspensionReason().equals(suspensionReason)) {
			user.restore();
			userHasBeenRestored = true;
		}

		return userHasBeenRestored;
	}

	public static ConditionalRestoreUserOperation instance(Long userId,
			SuspensionReason reason) {
		return new ConditionalRestoreUserOperation(userId, reason);

	}

	public static ConditionalRestoreUserOperation instance(VOMSUser u,
			SuspensionReason reason) {
		return new ConditionalRestoreUserOperation(u, reason);

	}
}
