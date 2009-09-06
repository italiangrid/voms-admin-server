package org.glite.security.voms.admin.operations.aup;

import org.glite.security.voms.admin.model.AUP;
import org.glite.security.voms.admin.operations.BaseVomsOperation;
import org.glite.security.voms.admin.operations.VOMSContext;
import org.glite.security.voms.admin.operations.VOMSPermission;

public class ChangeReacceptancePeriodOperation extends BaseVomsOperation {

	AUP aup;
	int period;
	
	
	public ChangeReacceptancePeriodOperation(AUP aup, int period) {
		this.aup = aup;
		this.period = period;
		
	}
	@Override
	protected Object doExecute() {
		
		aup.setReacceptancePeriod(period);
		return aup;
	}
	
	
	public AUP getAup() {
		return aup;
	}


	public void setAup(AUP aup) {
		this.aup = aup;
	}


	public int getPeriod() {
		return period;
	}


	public void setPeriod(int period) {
		this.period = period;
	}


	@Override
	protected void setupPermissions() {
		
		addRequiredPermission(VOMSContext.getVoContext(), VOMSPermission.getContainerRWPermissions().setMembershipRWPermission());
	}

}
