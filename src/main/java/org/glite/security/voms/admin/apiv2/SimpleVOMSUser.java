package org.glite.security.voms.admin.apiv2;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.glite.security.voms.admin.persistence.model.Certificate;
import org.glite.security.voms.admin.persistence.model.VOMSUser;
import org.glite.security.voms.admin.persistence.model.VOMSUser.SuspensionReason;

public class SimpleVOMSUser {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	Long id;
	
	String name;

	String surname;

	String institution;

	String address;

	String phoneNumber;
	
	String emailAddress;
	
	Date creationTime;

	Date endTime;

	Boolean suspended = false;

	SuspensionReason suspensionReasonCode;

	String suspensionReason;

	List<SimpleCertificate> certificates;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public Date getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public Boolean getSuspended() {
		return suspended;
	}

	public void setSuspended(Boolean suspended) {
		this.suspended = suspended;
	}

	public SuspensionReason getSuspensionReasonCode() {
		return suspensionReasonCode;
	}

	public void setSuspensionReasonCode(SuspensionReason suspensionReasonCode) {
		this.suspensionReasonCode = suspensionReasonCode;
	}

	public String getSuspensionReason() {
		return suspensionReason;
	}

	public void setSuspensionReason(String suspensionReason) {
		this.suspensionReason = suspensionReason;
	}
	
	
	public List<SimpleCertificate> getCertificates() {
		return certificates;
	}

	public void setCertificates(List<SimpleCertificate> certificates) {
		this.certificates = certificates;
	}

	public static SimpleVOMSUser fromVOMSUser(VOMSUser user){
		
		SimpleVOMSUser u = new SimpleVOMSUser();
		u.setId(user.getId());
		u.setName(user.getName());
		u.setSurname(user.getSurname());
		u.setAddress(user.getAddress());
		u.setPhoneNumber(user.getPhoneNumber());
		u.setInstitution(user.getInstitution());
		u.setSuspended(user.getSuspended());
		u.setSuspensionReason(user.getSuspensionReason());
		u.setSuspensionReasonCode(user.getSuspensionReasonCode());
		u.setEmailAddress(user.getEmailAddress());
		u.setCreationTime(user.getCreationTime());
		u.setEndTime(user.getEndTime());
		
		List<SimpleCertificate> certs = new ArrayList<SimpleCertificate>();
		for (Certificate c: user.getCertificates())
			certs.add(SimpleCertificate.fromCertificate(c));
		
		u.setCertificates(certs);
		return u;
	}

}
