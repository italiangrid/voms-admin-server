package org.glite.security.voms.admin.operations;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.glite.security.voms.admin.model.VOMSUser;

public abstract class BaseUserAdministrativeOperation extends BaseVomsOperation {

	public static final Log log = LogFactory
			.getLog(BaseUserAdministrativeOperation.class);

	VOMSUser authorizedUser;

	@Override
	final AuthorizationResponse isAllowed() {

		CurrentAdmin admin = CurrentAdmin.instance();

		if (!admin.isVoUser()) {
			log
					.debug("Current admin has no corresponding VO user, falling back to base authorization method.");
			return super.isAllowed();
		}

		boolean usersMatch = admin.getVoUser().equals(authorizedUser);
		log.debug("Admin's user match with authorized user: " + usersMatch);
		return AuthorizationResponse.permit();

	}

	public VOMSUser getAuthorizedUser() {
		return authorizedUser;
	}

	public void setAuthorizedUser(VOMSUser authorizedUser) {
		this.authorizedUser = authorizedUser;
	}

	@Override
	protected void setupPermissions() {

		addRequiredPermission(VOMSContext.getVoContext(), VOMSPermission
				.getAllPermissions());

	}
}
