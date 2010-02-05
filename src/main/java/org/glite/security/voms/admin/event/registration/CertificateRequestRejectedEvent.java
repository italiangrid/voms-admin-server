package org.glite.security.voms.admin.event.registration;

import org.glite.security.voms.admin.model.request.CertificateRequest;

public class CertificateRequestRejectedEvent extends CertificateRequestEvent {

	public CertificateRequestRejectedEvent(CertificateRequest req) {
		super(req);
		
	}

}
