package org.glite.security.voms.admin.operations.users;

import org.glite.security.voms.admin.model.VOMSUser;
import org.glite.security.voms.admin.operations.BaseVomsOperation;
import org.glite.security.voms.admin.operations.VOMSContext;
import org.glite.security.voms.admin.operations.VOMSPermission;

public class ListAttributesForUserOperation extends BaseVomsOperation {

	protected VOMSUser _user;

	private ListAttributesForUserOperation(VOMSUser u) {

		_user = u;
	}

	protected Object doExecute() {

		return _user.getAttributes();
	}

	protected void setupPermissions() {

		addRequiredPermission(VOMSContext.getVoContext(), VOMSPermission
				.getEmptyPermissions().setAttributesReadPermission());

	}

	public static ListAttributesForUserOperation instance(VOMSUser u) {

		return new ListAttributesForUserOperation(u);
	}

}
