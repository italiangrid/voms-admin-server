/**
 * Copyright (c) Istituto Nazionale di Fisica Nucleare (INFN). 2006-2016
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.glite.security.voms.admin.apiv2;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.glite.security.voms.admin.persistence.model.AUPAcceptanceRecord;
import org.glite.security.voms.admin.persistence.model.Certificate;
import org.glite.security.voms.admin.persistence.model.VOMSMapping;
import org.glite.security.voms.admin.persistence.model.VOMSUser;
import org.glite.security.voms.admin.persistence.model.VOMSUser.SuspensionReason;
import org.glite.security.voms.admin.persistence.model.attribute.VOMSUserAttribute;

import com.opensymphony.xwork2.validator.annotations.EmailValidator;
import com.opensymphony.xwork2.validator.annotations.RegexFieldValidator;
import com.opensymphony.xwork2.validator.annotations.RequiredStringValidator;
import com.opensymphony.xwork2.validator.annotations.ValidatorType;

public class VOMSUserJSON {

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
  
  Long cernHrId;

  Date creationTime;

  Date endTime;

  Boolean suspended;

  SuspensionReason suspensionReasonCode;

  String suspensionReason;

  List<CertificateJSON> certificates;

  List<String> fqans;

  List<AttributeJSON> attributes;

  List<AUPAcceptanceRecordJSON> aupAcceptanceRecords;

  SignAUPTaskJSON pendingSignAUPTask;

  public Long getId() {

    return id;
  }

  public void setId(Long id) {

    this.id = id;
  }

  @RequiredStringValidator(type = ValidatorType.FIELD,
    message = "Please provide a name for the user.")
  @RegexFieldValidator(type = ValidatorType.FIELD,
    message = "The name field contains illegal characters!",
    regex = "^[^<>&=;]*$")
  public String getName() {

    return name;
  }

  public void setName(String name) {

    this.name = name;
  }

  @RequiredStringValidator(type = ValidatorType.FIELD,
    message = "Please provide a family name for the user.")
  @RegexFieldValidator(type = ValidatorType.FIELD,
    message = "The family name contains illegal characters!",
    regex = "^[^<>&=;]*$")
  public String getSurname() {

    return surname;
  }

  public void setSurname(String surname) {

    this.surname = surname;
  }

  @RequiredStringValidator(type = ValidatorType.FIELD,
    message = "Please provide an institution for the user.")
  @RegexFieldValidator(type = ValidatorType.FIELD,
    message = "The institution contains illegal characters!",
    regex = "^[^<>=;]*$")
  public String getInstitution() {

    return institution;
  }

  public void setInstitution(String institution) {

    this.institution = institution;
  }

  @RequiredStringValidator(type = ValidatorType.FIELD,
    message = "Please provide an address for the user.")
  @RegexFieldValidator(type = ValidatorType.FIELD,
    message = "The address contains illegal characters!",
    regex = "^[^<>&=;]*$")
  public String getAddress() {

    return address;
  }

  public void setAddress(String address) {

    this.address = address;
  }

  @RequiredStringValidator(type = ValidatorType.FIELD,
    message = "Please provide a phone number for the user.")
  @RegexFieldValidator(type = ValidatorType.FIELD,
    message = "The phone number contains illegal characters!",
    regex = "^[^<>&=;]*$")
  public String getPhoneNumber() {

    return phoneNumber;
  }

  public void setPhoneNumber(String phoneNumber) {

    this.phoneNumber = phoneNumber;
  }

  @RequiredStringValidator(type = ValidatorType.FIELD,
    message = "Please provide an email address for the user.")
  @EmailValidator(type = ValidatorType.FIELD,
    message = "Please enter a valid email address.")
  @RegexFieldValidator(type = ValidatorType.FIELD,
    message = "The email address contains illegal characters!",
    regex = "^[^<>&=;]*$")
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

  public List<CertificateJSON> getCertificates() {

    return certificates;
  }

  public void setCertificates(List<CertificateJSON> certificates) {

    this.certificates = certificates;
  }

  public List<AttributeJSON> getAttributes() {

    return attributes;
  }

  public void setAttributes(List<AttributeJSON> attributes) {

    this.attributes = attributes;
  }

  public void attributesFrom(VOMSUser user) {

    this.attributes = new ArrayList<AttributeJSON>();

    for (VOMSUserAttribute attr : user.getAttributes()) {
      attributes.add(AttributeJSON.fromVOMSUserAttribute(attr));
    }

  }

  public void personalInformationFrom(VOMSUser user) {

    setName(user.getName());
    setSurname(user.getSurname());
    setEmailAddress(user.getEmailAddress());

    setAddress(user.getAddress());
    setPhoneNumber(user.getPhoneNumber());
    setInstitution(user.getInstitution());

    setSuspended(user.getSuspended());
    setSuspensionReason(user.getSuspensionReason());
    setSuspensionReasonCode(user.getSuspensionReasonCode());

    setCreationTime(user.getCreationTime());
    setEndTime(user.getEndTime());

    List<AUPAcceptanceRecordJSON> aupRecords = new ArrayList<AUPAcceptanceRecordJSON>();

    for (AUPAcceptanceRecord rec : user.getAupAcceptanceRecords()) {
      aupRecords.add(AUPAcceptanceRecordJSON.from(rec));
    }

    setAupAcceptanceRecords(aupRecords);

    setPendingSignAUPTask(SignAUPTaskJSON.from(user.getPendingSignAUPTask()));

  }

  public void cernHrIdFrom(VOMSUser user) {
    this.cernHrId = user.getOrgDbId();
  }
  
  public void fqansFrom(VOMSUser user) {

    List<String> fqans = new ArrayList<String>();

    for (VOMSMapping m : user.getMappings()) {
      fqans.add(m.getFQAN());
    }

    setFqans(fqans);
  }

  public List<AUPAcceptanceRecordJSON> getAupAcceptanceRecords() {

    return aupAcceptanceRecords;
  }

  public void setAupAcceptanceRecords(
    List<AUPAcceptanceRecordJSON> aupAcceptanceRecords) {

    this.aupAcceptanceRecords = aupAcceptanceRecords;
  }

  public SignAUPTaskJSON getPendingSignAUPTask() {

    return pendingSignAUPTask;
  }

  public void setPendingSignAUPTask(SignAUPTaskJSON pendingSignAUPTask) {

    this.pendingSignAUPTask = pendingSignAUPTask;
  }

  public List<String> getFqans() {

    return fqans;
  }

  public void setFqans(List<String> fqans) {

    this.fqans = fqans;
  }

  public static VOMSUserJSON fromVOMSUser(VOMSUser user) {

    VOMSUserJSON u = new VOMSUserJSON();
    u.setId(user.getId());

    List<CertificateJSON> certs = new ArrayList<CertificateJSON>();
    for (Certificate c : user.getCertificates()){
      certs.add(CertificateJSON.fromCertificate(c));
    }
    u.setCertificates(certs);

    return u;
  }

  public Long getCernHrId() {
    return cernHrId;
  }

  
  
}
