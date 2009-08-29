package org.glite.security.voms.admin.api.acl;

import java.io.Serializable;

public class ACLEntry implements Serializable {

	/**
     * 
     */
	private static final long serialVersionUID = 1L;

	String adminSubject;
	String adminIssuer;

	int vomsPermissionBits = 0;

	public String getAdminIssuer() {

		return adminIssuer;
	}

	public void setAdminIssuer(String adminIssuer) {

		this.adminIssuer = adminIssuer;
	}

	public String getAdminSubject() {

		return adminSubject;
	}

	public void setAdminSubject(String adminSubject) {

		this.adminSubject = adminSubject;
	}

	public int getVomsPermissionBits() {

		return vomsPermissionBits;
	}

	public void setVomsPermissionBits(int vomsPermissionBits) {

		this.vomsPermissionBits = vomsPermissionBits;
	}

}
