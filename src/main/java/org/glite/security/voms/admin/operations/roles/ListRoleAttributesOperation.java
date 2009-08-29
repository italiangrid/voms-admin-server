package org.glite.security.voms.admin.operations.roles;

import org.glite.security.voms.admin.model.VOMSGroup;
import org.glite.security.voms.admin.model.VOMSRole;
import org.glite.security.voms.admin.operations.BaseVomsOperation;
import org.glite.security.voms.admin.operations.VOMSContext;
import org.glite.security.voms.admin.operations.VOMSPermission;

public class ListRoleAttributesOperation extends BaseVomsOperation {

	protected VOMSGroup _vomsGroup;
	protected VOMSRole _vomsRole;

	private ListRoleAttributesOperation(VOMSGroup g, VOMSRole r) {

		this._vomsGroup = g;
		this._vomsRole = r;
	}

	protected Object doExecute() {

		return _vomsRole.getAttributesInGroup(_vomsGroup);

	}

	protected void setupPermissions() {

		addRequiredPermissionOnPath(_vomsGroup.getParent(), VOMSPermission
				.getContainerReadPermission());

		addRequiredPermission(VOMSContext.getVoContext(), VOMSPermission
				.getEmptyPermissions().setAttributesReadPermission());

		addRequiredPermission(VOMSContext.instance(_vomsGroup, _vomsRole),
				VOMSPermission.getEmptyPermissions()
						.setAttributesReadPermission());

	}

	public static ListRoleAttributesOperation instance(VOMSGroup g, VOMSRole r) {

		return new ListRoleAttributesOperation(g, r);
	}

}
