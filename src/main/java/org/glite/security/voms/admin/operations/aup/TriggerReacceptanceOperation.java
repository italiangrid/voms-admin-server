package org.glite.security.voms.admin.operations.aup;

import java.util.Date;

import org.glite.security.voms.admin.model.AUP;
import org.glite.security.voms.admin.operations.BaseVomsOperation;
import org.glite.security.voms.admin.operations.VOMSContext;
import org.glite.security.voms.admin.operations.VOMSPermission;

public class TriggerReacceptanceOperation extends BaseVomsOperation {
	
	AUP aup;
	
	public TriggerReacceptanceOperation(AUP a) {
		this.aup = a;
	}

	@Override
	protected Object doExecute() {
		aup.getActiveVersion().setLastForcedReacceptanceTime(new Date());
		return aup;
	}

	@Override
	protected void setupPermissions() {
		addRequiredPermission(VOMSContext.getVoContext(), VOMSPermission.getContainerRWPermissions().setMembershipRWPermission());

	}

}
