package org.glite.security.voms.admin.operations.users;

import org.glite.security.voms.admin.common.NullArgumentException;
import org.glite.security.voms.admin.common.VOMSException;
import org.glite.security.voms.admin.model.Certificate;
import org.glite.security.voms.admin.model.VOMSUser;
import org.glite.security.voms.admin.model.VOMSUser.SuspensionReason;
import org.glite.security.voms.admin.operations.BaseVomsOperation;
import org.glite.security.voms.admin.operations.VOMSContext;
import org.glite.security.voms.admin.operations.VOMSPermission;

public class SuspendUserCertificateOperation extends BaseVomsOperation {

	VOMSUser user;
	Certificate certificate;
	SuspensionReason reason;
	
	private SuspendUserCertificateOperation(VOMSUser u, Certificate c, SuspensionReason r) {
		user = u;
		certificate = c;
		reason = r;
		
	}
	
	public static SuspendUserCertificateOperation instance(VOMSUser u, Certificate c, SuspensionReason r){
		
		return new SuspendUserCertificateOperation(u,c,r);
	}
	@Override
	protected Object doExecute() {
		
		if (user == null)
			throw new NullArgumentException("user cannot be null");
		
		if (certificate == null)
			throw new NullArgumentException("certificate cannot be null");
		
		if (reason == null )
			throw new NullArgumentException("reason cannot be null");
		
		if (!user.hasCertificate(certificate))
			throw new VOMSException("Certificate '"+certificate+"' is not bound to user '"+user+"'.");
		
		certificate.suspend(reason);
		return null;
	}

	@Override
	protected void setupPermissions() {
		addRequiredPermission(VOMSContext.getVoContext(), VOMSPermission.getContainerReadPermission().setMembershipReadPermission().setSuspendPermission());
	}

}
