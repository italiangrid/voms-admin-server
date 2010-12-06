package org.glite.security.voms.admin.operations.aa;

import it.infn.cnaf.voms.aa.VOMSAA;
import it.infn.cnaf.voms.aa.VOMSAttributeAuthority;
import it.infn.cnaf.voms.aa.VOMSAttributes;

import org.glite.security.voms.admin.operations.BaseVomsOperation;
import org.glite.security.voms.admin.operations.VOMSContext;
import org.glite.security.voms.admin.operations.VOMSPermission;

public class GetAllUserAttributesOperation extends BaseVomsOperation {

    String voMemberCertSubject;
    
    
    
    
    public GetAllUserAttributesOperation(String certSubject) {
	voMemberCertSubject = certSubject;
    }
    
    @Override
    protected Object doExecute() {
	
	VOMSAttributeAuthority aa = VOMSAA.getVOMSAttributeAuthority();
	
	VOMSAttributes attrs = aa.getAllVOMSAttributes(voMemberCertSubject);
	
	return attrs;
    }

    @Override
    protected void setupPermissions() {
	addRequiredPermission(VOMSContext.getVoContext(), VOMSPermission.getContainerReadPermission().setMembershipReadPermission().setAttributesReadPermission());

    }

}
