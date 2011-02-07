package org.glite.security.voms.admin.operations.aup;

import org.glite.security.voms.admin.operations.BaseVomsOperation;
import org.glite.security.voms.admin.operations.VOMSContext;
import org.glite.security.voms.admin.operations.VOMSPermission;
import org.glite.security.voms.admin.persistence.dao.VOMSUserDAO;
import org.glite.security.voms.admin.persistence.model.AUP;
import org.glite.security.voms.admin.persistence.model.VOMSUser;

public class CreateAcceptanceRecordOperation extends BaseVomsOperation {

    AUP aup;
    VOMSUser user;
        
    public CreateAcceptanceRecordOperation(AUP a, VOMSUser u) {
	
	this.aup = a;
	this.user = u;
	
    }
    
    
    @Override
    protected void setupPermissions() {
	addRequiredPermission(VOMSContext.getVoContext(), VOMSPermission.getContainerRWPermissions().setMembershipRWPermission().setSuspendPermission());

    }

    @Override
    protected Object doExecute() {
	
	if (aup == null)
	    throw new IllegalArgumentException("AUP cannot be null!");
	
	if (user == null)
	    throw new IllegalArgumentException("User cannot be null!");
	
	VOMSUserDAO.instance().signAUP(user, aup);
	return null;	
	
    }

}
