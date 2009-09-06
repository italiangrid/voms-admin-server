package org.glite.security.voms.admin.api.certificates;

import java.io.Serializable;

public class X509Certificate implements Serializable {

	/**
     * 
     */
	private static final long serialVersionUID = 1L;

	long id;

	String subject;
	String issuer;
	String notAfter;
	
	byte[] bytes;

	public byte[] getBytes() {

		return bytes;
	}

	public void setBytes(byte[] bytes) {

		this.bytes = bytes;
	}

	public String getIssuer() {

		return issuer;
	}

	public void setIssuer(String issuer) {

		this.issuer = issuer;
	}

	public String getNotAfter() {

		return notAfter;
	}

	public void setNotAfter(String notAfter) {

		this.notAfter = notAfter;
	}

	public String getSubject() {

		return subject;
	}

	public void setSubject(String subject) {

		this.subject = subject;
	}

	public long getId() {

		return id;
	}

	public void setId(long id) {

		this.id = id;
	}

}
