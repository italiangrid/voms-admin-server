package org.glite.security.voms.admin.view.actions.user;

import org.apache.commons.lang.builder.ToStringBuilder;

public class UserPersonalInfo {
	
	String certificateSubject;
	String certificateIssuer;
	
	String name;
    String surname;
    String institution;
    String address;
    String phoneNumber;
    
    String emailAddress;
    

    public UserPersonalInfo() {
		// TODO Auto-generated constructor stub
	}

	public String getCertificateSubject() {
		return certificateSubject;
	}

	public void setCertificateSubject(String certificateSubject) {
		this.certificateSubject = certificateSubject;
	}

	public String getCertificateIssuer() {
		return certificateIssuer;
	}

	public void setCertificateIssuer(String certificateIssuer) {
		this.certificateIssuer = certificateIssuer;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getInstitution() {
		return institution;
	}

	public void setInstitution(String institution) {
		this.institution = institution;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

    
	@Override
	public String toString() {

		return ToStringBuilder.reflectionToString(this);
	}
    
}
