package org.glite.security.voms.admin.apiv2;

import org.glite.security.voms.admin.persistence.model.Certificate;

public class CertificateJSON {
	
	String subjectString;
	String issuerString;
	
	Boolean suspended;
	String suspensionReason;
	
	public Boolean getSuspended() {
		return suspended;
	}

	public void setSuspended(Boolean suspended) {
		this.suspended = suspended;
	}

	public String getSuspensionReason() {
		return suspensionReason;
	}

	public void setSuspensionReason(String suspensionReason) {
		this.suspensionReason = suspensionReason;
	}

	public String getSubjectString() {
		return subjectString;
	}
	
	public void setSubjectString(String subjectString) {
		this.subjectString = subjectString;
	}
	public String getIssuerString() {
		return issuerString;
	}
	public void setIssuerString(String issuerString) {
		this.issuerString = issuerString;
	}
	
	public static CertificateJSON fromCertificate(Certificate cert){
		
		CertificateJSON c = new CertificateJSON();
		c.setSubjectString(cert.getSubjectString());
		c.setIssuerString(cert.getCa().getSubjectString());
		c.setSuspended(cert.isSuspended());
		c.setSuspensionReason(cert.getSuspensionReason());
		
		return c;
		
	}
}
