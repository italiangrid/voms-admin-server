package org.glite.security.voms.admin.event.registration;

import org.glite.security.voms.admin.model.request.CertificateRequest;

public class CertificateRequestSubmittedEvent extends CertificateRequestEvent {

	private String managementURL;
	
	public CertificateRequestSubmittedEvent(CertificateRequest req, String managementURL) {
		super(req);
		setManagementURL(managementURL);
	}

	public String getManagementURL() {
		return managementURL;
	}

	public void setManagementURL(String managementURL) {
		this.managementURL = managementURL;
	}

	
	
}
