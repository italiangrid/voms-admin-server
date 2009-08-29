package org.glite.security.voms.admin.operations;

public class AuthorizationResponse {
	
	boolean allowed;
	
	private AuthorizationResponse() {
		
	}
	
	private VOMSContext failingContext;
	private VOMSPermission requiredPermissions;
	private String failureReason;
	
	public static AuthorizationResponse permit(){
		AuthorizationResponse resp = new AuthorizationResponse();
		resp.allowed = true;
		return resp;
	}
	
	public static AuthorizationResponse deny(VOMSContext failingContext, VOMSPermission requiredPermissions){
		AuthorizationResponse resp = new AuthorizationResponse();
		resp.allowed = false;
		resp.failingContext = failingContext;
		resp.requiredPermissions = requiredPermissions;
		return resp;
	}

	public boolean isAllowed() {
		return allowed;
	}

	public void setAllowed(boolean allowed) {
		this.allowed = allowed;
	}

	public VOMSContext getFailingContext() {
		return failingContext;
	}

	public void setFailingContext(VOMSContext failingContext) {
		this.failingContext = failingContext;
	}

	public VOMSPermission getRequiredPermissions() {
		return requiredPermissions;
	}

	public void setRequiredPermissions(VOMSPermission requiredPermissions) {
		this.requiredPermissions = requiredPermissions;
	}

	public String getFailureReason() {
		return failureReason;
	}

	public void setFailureReason(String failureReason) {
		this.failureReason = failureReason;
	}	
}
