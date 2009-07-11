package org.glite.security.voms.admin.event.registration;

import org.glite.security.voms.admin.model.request.NewVOMembershipRequest;

public class VOMembershipRequestSubmittedEvent extends VOMembershipRequestEvent {

	String confirmURL;
	String cancelURL;
	
	public VOMembershipRequestSubmittedEvent(NewVOMembershipRequest r, String confirmURL, String cancelURL){
		super(r);
		
		request = r;
		this.confirmURL = confirmURL;
		this.cancelURL = cancelURL;
		
	}

	public String getConfirmURL() {
		return confirmURL;
	}

	public void setConfirmURL(String confirmURL) {
		this.confirmURL = confirmURL;
	}

	public String getCancelURL() {
		return cancelURL;
	}

	public void setCancelURL(String cancelURL) {
		this.cancelURL = cancelURL;
	}

	

	
}
