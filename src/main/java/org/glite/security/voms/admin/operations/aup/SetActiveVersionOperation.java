package org.glite.security.voms.admin.operations.aup;

import org.glite.security.voms.admin.dao.generic.AUPDAO;
import org.glite.security.voms.admin.dao.generic.DAOFactory;
import org.glite.security.voms.admin.model.AUP;
import org.glite.security.voms.admin.operations.BaseVomsOperation;
import org.glite.security.voms.admin.operations.VOMSContext;
import org.glite.security.voms.admin.operations.VOMSPermission;

public class SetActiveVersionOperation extends BaseVomsOperation {

	AUP aup;
	
	String version;
	
	public SetActiveVersionOperation(AUP a, String v) {
		this.aup = a;
		this.version = v;
		
	}
	@Override
	protected Object doExecute() {
		AUPDAO dao = DAOFactory.instance().getAUPDAO();

		dao.setActiveVersion(aup, version);
		return null;
	}

	
	

	@Override
	protected void setupPermissions() {
		addRequiredPermission(VOMSContext.getVoContext(), VOMSPermission.getContainerRWPermissions().setMembershipRWPermission());

	}

}
