package org.glite.security.voms.admin.operations.users;

import java.util.Date;

import org.glite.security.voms.admin.model.VOMSUser;
import org.glite.security.voms.admin.operations.BaseVomsOperation;
import org.glite.security.voms.admin.operations.VOMSContext;
import org.glite.security.voms.admin.operations.VOMSPermission;

public class SetMembershipExpirationOperation extends BaseVomsOperation {

	VOMSUser user;
	Date expirationDate;
	
	public SetMembershipExpirationOperation(VOMSUser u, Date d) {
		this.user = u;
		this.expirationDate = d;
	}
	
	@Override
	protected Object doExecute() {
		
		user.setEndTime(expirationDate);
		return user;
	}

	@Override
	protected void setupPermissions() {
		addRequiredPermission(VOMSContext.getVoContext(), VOMSPermission.getContainerRWPermissions().setMembershipRWPermission());

	}

}
