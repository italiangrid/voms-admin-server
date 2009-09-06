package org.glite.security.voms.admin.operations.aup;

import org.glite.security.voms.admin.dao.generic.AUPDAO;
import org.glite.security.voms.admin.dao.generic.DAOFactory;
import org.glite.security.voms.admin.model.AUP;
import org.glite.security.voms.admin.operations.BaseVomsOperation;
import org.glite.security.voms.admin.operations.VOMSContext;
import org.glite.security.voms.admin.operations.VOMSPermission;

public class RemoveVersionOperation extends BaseVomsOperation {

	AUP aup;
	
	String version;
	
	public RemoveVersionOperation(AUP a, String v) {
		this.aup = a;
		this.version = v;
	}
	
	@Override
	protected Object doExecute() {
		
		AUPDAO dao = DAOFactory.instance().getAUPDAO();
		dao.removeVersion(aup, version);
		
		return aup;
	}

	
	
	@Override
	protected void setupPermissions() {
		addRequiredPermission(VOMSContext.getVoContext(), VOMSPermission.getContainerRWPermissions().setMembershipRWPermission());
	}

}
