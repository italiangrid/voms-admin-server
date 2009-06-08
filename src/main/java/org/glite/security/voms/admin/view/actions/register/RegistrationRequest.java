package org.glite.security.voms.admin.view.actions.register;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.glite.security.voms.admin.view.actions.user.User;

public class RegistrationRequest {
	
	User userInfo;
	
	Boolean acceptsAUP;
	
	String confirmationURL;
	
	public RegistrationRequest() {
		// TODO Auto-generated constructor stub
	}

	
	public User getUserInfo() {
		return userInfo;
	}

	public void setUserInfo(User userInfo) {
		this.userInfo = userInfo;
	}


	public Boolean getAcceptsAUP() {
		return acceptsAUP;
	}


	public void setAcceptsAUP(Boolean acceptsAUP) {
		this.acceptsAUP = acceptsAUP;
	}


	public String getConfirmationURL() {
		return confirmationURL;
	}


	public void setConfirmationURL(String confirmationURL) {
		this.confirmationURL = confirmationURL;
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
