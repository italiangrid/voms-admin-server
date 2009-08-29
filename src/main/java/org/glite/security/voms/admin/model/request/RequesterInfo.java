package org.glite.security.voms.admin.model.request;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Table;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

@Entity
@Table(name = "requester_info")
public class RequesterInfo implements Serializable {

	/**
     * 
     */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	Long id;

	@Column(nullable = false)
	String certificateSubject;

	@Column(nullable = false)
	String certificateIssuer;

	String name;

	String surname;

	String institution;

	String address;

	String phoneNumber;

	@Column(nullable = false)
	String emailAddress;

	@org.hibernate.annotations.CollectionOfElements
	@JoinTable(name = "requester_personal_info", joinColumns = @JoinColumn(name = "requester_id"))
	@org.hibernate.annotations.MapKey(columns = @Column(name = "pi_key"))
	@Column(name = "pi_value")
	Map<String, String> personalInformation = new HashMap<String, String>();

	Boolean voMember;

	/**
	 * @return the id
	 */
	public Long getId() {

		return id;
	}

	/**
	 * @return the certificateSubject
	 */
	public String getCertificateSubject() {

		return certificateSubject;
	}

	/**
	 * @return the certificateIssuer
	 */
	public String getCertificateIssuer() {

		return certificateIssuer;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(Long id) {

		this.id = id;
	}

	/**
	 * @param certificateSubject
	 *            the certificateSubject to set
	 */
	public void setCertificateSubject(String certificateSubject) {

		this.certificateSubject = certificateSubject;
	}

	/**
	 * @param certificateIssuer
	 *            the certificateIssuer to set
	 */
	public void setCertificateIssuer(String certificateIssuer) {

		this.certificateIssuer = certificateIssuer;
	}

	/**
	 * @return the personalInformation
	 */
	public Map<String, String> getPersonalInformation() {

		return personalInformation;
	}

	/**
	 * @param personalInformation
	 *            the personalInformation to set
	 */
	public void setPersonalInformation(Map<String, String> personalInformation) {

		this.personalInformation = personalInformation;
	}

	/**
	 * @return the emailAddress
	 */
	public String getEmailAddress() {

		return emailAddress;
	}

	/**
	 * @param emailAddress
	 *            the emailAddress to set
	 */
	public void setEmailAddress(String emailAddress) {

		this.emailAddress = emailAddress;
	}

	public boolean isVoMember() {

		return voMember;
	}

	/**
	 * @return the voMember
	 */
	public Boolean getVoMember() {

		return voMember;
	}

	/**
	 * @param voMember
	 *            the voMember to set
	 */
	public void setVoMember(Boolean voMember) {

		this.voMember = voMember;
	}

	@Override
	public boolean equals(Object other) {

		if (this == other)
			return true;

		if (!(other instanceof RequesterInfo))
			return false;

		if (other == null)
			return false;

		RequesterInfo that = (RequesterInfo) other;

		// Implement meaningful checks here

		return new EqualsBuilder().append(certificateSubject,
				that.certificateSubject).append(certificateIssuer,
				that.certificateIssuer).append(emailAddress, that.emailAddress)
				.isEquals();

	}

	public String addInfo(String name, String value) {

		return personalInformation.put(name, value);
	}

	public String getInfo(String name) {

		return personalInformation.get(name);
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

	@Override
	public int hashCode() {

		return new HashCodeBuilder(17, 37).append(certificateSubject).append(
				certificateIssuer).append(emailAddress).toHashCode();

	}

	@Override
	public String toString() {

		return certificateSubject;
	}

}
