package org.glite.security.voms.admin.operations.users;

import org.glite.security.voms.admin.common.NullArgumentException;
import org.glite.security.voms.admin.model.Certificate;
import org.glite.security.voms.admin.model.VOMSUser;
import org.glite.security.voms.admin.operations.BaseVomsOperation;
import org.glite.security.voms.admin.operations.VOMSContext;
import org.glite.security.voms.admin.operations.VOMSPermission;

public class RestoreUserCertificateOperation extends BaseVomsOperation {

	VOMSUser user;
	Certificate certificate;
	
	private RestoreUserCertificateOperation(VOMSUser u, Certificate c) {
		user = u;
		certificate = c;
	}
	
	
	public static RestoreUserCertificateOperation instance(VOMSUser u, Certificate c){
		return new RestoreUserCertificateOperation(u,c);
	}
	@Override
	protected Object doExecute() {
		if (user == null)
			throw new NullArgumentException("user cannot be null");
		
		if (certificate == null)
			throw new NullArgumentException("certificate cannot be null");
		
		certificate.restore();
		return null;
	}

	@Override
	protected void setupPermissions() {
		addRequiredPermission(VOMSContext.getVoContext(), VOMSPermission.getContainerReadPermission().setMembershipReadPermission().setSuspendPermission());

	}

}
