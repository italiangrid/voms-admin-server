/**
 * Copyright (c) Members of the EGEE Collaboration. 2006-2009.
 * See http://www.eu-egee.org/partners/ for details on the copyright holders.
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
 *
 * Authors:
 * 	Andrea Ceccanti (INFN)
 */

package org.glite.security.voms.admin.view.actions.register.orgdb;

import java.util.Calendar;
import java.util.Date;

import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.glite.security.voms.admin.configuration.VOMSConfiguration;
import org.glite.security.voms.admin.core.validation.RequestValidationResult;
import org.glite.security.voms.admin.core.validation.RequestValidationResult.Outcome;
import org.glite.security.voms.admin.core.validation.ValidationManager;
import org.glite.security.voms.admin.event.EventManager;
import org.glite.security.voms.admin.event.request.VOMembershipRequestSubmittedEvent;
import org.glite.security.voms.admin.persistence.dao.generic.DAOFactory;
import org.glite.security.voms.admin.util.URLBuilder;
import org.glite.security.voms.admin.view.actions.BaseAction;
import org.glite.security.voms.admin.view.actions.register.RegisterActionSupport;

import com.opensymphony.xwork2.validator.annotations.EmailValidator;
import com.opensymphony.xwork2.validator.annotations.RegexFieldValidator;
import com.opensymphony.xwork2.validator.annotations.RequiredFieldValidator;
import com.opensymphony.xwork2.validator.annotations.RequiredStringValidator;
import com.opensymphony.xwork2.validator.annotations.ValidatorType;

@Results({
  @Result(name = BaseAction.INPUT, location = "orgdbRegister"),
  @Result(name = BaseAction.SUCCESS, location = "registerConfirmation"),
  @Result(name = RegisterActionSupport.CONFIRMATION_NEEDED,
    location = "registerConfirmation"),
  @Result(name = RegisterActionSupport.REGISTRATION_DISABLED,
    location = "registrationDisabled"),
  @Result(name = RegisterActionSupport.PLUGIN_VALIDATION_ERROR,
    location = "pluginValidationError") })
@InterceptorRef(value = "authenticatedStack", params = {
  "token.includeMethods", "execute" })
public class SubmitRequestAction extends OrgDbRegisterActionSupport {

  /**
	 * 
	 */
  private static final long serialVersionUID = 1L;

  String name;
  String surname;

  String institution;
  String address;

  String phoneNumber;

  String emailAddress;

  String aupAccepted;

  RequestValidationResult validationResult;

  protected void populateRequestModel() {

    long requestLifetime = VOMSConfiguration.instance().getLong(
      "voms.request.vo_membership.lifetime", 300);

    Date expirationDate = getFutureDate(new Date(), Calendar.SECOND,
      (int) requestLifetime);

    requester.setName(name);
    requester.setSurname(surname);
    requester.setInstitution(institution);
    requester.setAddress(address);
    requester.setPhoneNumber(phoneNumber);
    requester.setEmailAddress(emailAddress);

    request = DAOFactory.instance().getRequestDAO()
      .createVOMembershipRequest(requester, expirationDate);

  }

  @Override
  public String execute() throws Exception {

    if (!registrationEnabled())
      return REGISTRATION_DISABLED;

    String result = checkExistingPendingRequests();

    if (result != null)
      return result;

    populateRequestModel();

    // External plugin validation
    validationResult = ValidationManager.instance().validateRequest(request);
    if (!validationResult.getOutcome().equals(Outcome.SUCCESS)) {

      DAOFactory.instance().getRequestDAO().makeTransient(request);
      addActionError(validationResult.getMessage());
      return PLUGIN_VALIDATION_ERROR;
    }

    EventManager.instance().dispatch(new VOMembershipRequestSubmittedEvent(request,
      URLBuilder.buildRequestConfirmURL(getModel()), URLBuilder
        .buildRequestCancelURL(getModel())));

    return SUCCESS;
  }

  /**
   * @return the name
   */
  public String getName() {

    return name;
  }

  /**
   * @param name
   *          the name to set
   */
  public void setName(String name) {

    this.name = name;
  }

  /**
   * @return the surname
   */
  public String getSurname() {

    return surname;
  }

  /**
   * @param surname
   *          the surname to set
   */
  public void setSurname(String surname) {

    this.surname = surname;
  }

  /**
   * @return the institution
   */
  @RequiredStringValidator(type = ValidatorType.FIELD,
    message = "Please enter your institution.")
  public String getInstitution() {

    return institution;
  }

  /**
   * @param institution
   *          the institution to set
   */
  public void setInstitution(String institution) {

    this.institution = institution;
  }

  /**
   * @return the address
   */
  @RequiredStringValidator(type = ValidatorType.FIELD,
    message = "Please enter your address.")
  @RegexFieldValidator(type = ValidatorType.FIELD, expression = "^[^<>&=;]*$",
    message = "You entered invalid characters.")
  public String getAddress() {

    return address;
  }

  /**
   * @param address
   *          the address to set
   */
  public void setAddress(String address) {

    this.address = address;
  }

  /**
   * @return the phoneNumber
   */
  @RequiredStringValidator(type = ValidatorType.FIELD,
    message = "Please enter your phoneNumber.")
  @RegexFieldValidator(type = ValidatorType.FIELD, expression = "^[^<>&=;]*$",
    message = "You entered invalid characters.")
  public String getPhoneNumber() {

    return phoneNumber;
  }

  /**
   * @param phoneNumber
   *          the phoneNumber to set
   */
  public void setPhoneNumber(String phoneNumber) {

    this.phoneNumber = phoneNumber;
  }

  /**
   * @return the emailAddress
   */
  @RequiredStringValidator(type = ValidatorType.FIELD,
    message = "Please enter your email address.")
  @EmailValidator(type = ValidatorType.FIELD,
    message = "Please enter a valid email address.")
  public String getEmailAddress() {

    return emailAddress;
  }

  /**
   * @param emailAddress
   *          the emailAddress to set
   */
  public void setEmailAddress(String emailAddress) {

    this.emailAddress = emailAddress;
  }

  /**
   * @return the aupAccepted
   */
  @RequiredFieldValidator(type = ValidatorType.FIELD,
    message = "You must sign the AUP.")
  @RegexFieldValidator(type = ValidatorType.FIELD, expression = "^true$",
    message = "You must accept the terms of the AUP to proceed")
  public String getAupAccepted() {

    return aupAccepted;
  }

  /**
   * @param aupAccepted
   *          the aupAccepted to set
   */
  public void setAupAccepted(String aupAccepted) {

    this.aupAccepted = aupAccepted;
  }

  /**
   * @return the validationResult
   */
  public RequestValidationResult getValidationResult() {

    return validationResult;
  }

}
