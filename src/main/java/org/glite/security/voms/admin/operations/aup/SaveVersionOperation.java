package org.glite.security.voms.admin.operations.aup;

import org.glite.security.voms.admin.operations.GenericSaveOrUpdateOperation;
import org.glite.security.voms.admin.operations.VOMSContext;
import org.glite.security.voms.admin.operations.VOMSPermission;
import org.glite.security.voms.admin.persistence.model.AUPVersion;

public class SaveVersionOperation extends
	GenericSaveOrUpdateOperation<AUPVersion> {

    
    public SaveVersionOperation(AUPVersion theModel) {
	super(theModel);
    }

    @Override
    protected void setupPermissions() {
	addRequiredPermission(VOMSContext.getVoContext(), VOMSPermission.getContainerRWPermissions().setMembershipRWPermission());
    }

}
