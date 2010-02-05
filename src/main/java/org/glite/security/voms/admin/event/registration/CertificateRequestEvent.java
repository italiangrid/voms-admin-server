package org.glite.security.voms.admin.event.registration;

import org.glite.security.voms.admin.event.EventType;
import org.glite.security.voms.admin.event.GenericEvent;
import org.glite.security.voms.admin.model.request.CertificateRequest;

public class CertificateRequestEvent extends GenericEvent {

	
	CertificateRequest request;
	
	public CertificateRequestEvent(CertificateRequest req) {
		super(EventType.CertificateRequestEvent);
		setRequest(req);
		
	}

	public CertificateRequest getRequest() {
		return request;
	}

	public void setRequest(CertificateRequest request) {
		this.request = request;
	}

	
}
