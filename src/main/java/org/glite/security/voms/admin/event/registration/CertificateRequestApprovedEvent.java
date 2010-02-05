package org.glite.security.voms.admin.event.registration;

import org.glite.security.voms.admin.model.request.CertificateRequest;

public class CertificateRequestApprovedEvent extends CertificateRequestEvent {

	public CertificateRequestApprovedEvent(CertificateRequest req) {
		super(req);
	}

}
