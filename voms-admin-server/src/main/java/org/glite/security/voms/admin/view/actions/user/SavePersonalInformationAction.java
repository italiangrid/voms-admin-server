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
package org.glite.security.voms.admin.view.actions.user;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.interceptor.TokenInterceptor;
import org.glite.security.voms.admin.configuration.VOMSConfiguration;
import org.glite.security.voms.admin.error.IllegalStateException;
import org.glite.security.voms.admin.integration.PluginManager;
import org.glite.security.voms.admin.integration.orgdb.OrgDBConfigurator;
import org.glite.security.voms.admin.integration.orgdb.strategies.OrgDBEmailValidationResult;
import org.glite.security.voms.admin.operations.users.SaveUserPersonalInfoOperation;

import com.google.common.base.Strings;
import com.opensymphony.xwork2.validator.annotations.EmailValidator;
import com.opensymphony.xwork2.validator.annotations.RegexFieldValidator;
import com.opensymphony.xwork2.validator.annotations.RequiredStringValidator;
import com.opensymphony.xwork2.validator.annotations.ValidatorType;

@Results({@Result(name = UserActionSupport.SUCCESS, location = "personalInfo.jsp"),
    @Result(name = UserActionSupport.INPUT, location = "personalInfo.jsp"),
    @Result(name = TokenInterceptor.INVALID_TOKEN_CODE, location = "personalInfo.jsp")})
@InterceptorRef(value = "authenticatedStack", params = {"token.includeMethods", "execute"})
public class SavePersonalInformationAction extends UserActionSupport {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  String theName;
  String theSurname;
  String theInstitution;
  String theAddress;
  String thePhoneNumber;
  String theEmailAddress;

  private static final String[] ORGDB_VALIDATED_FIELDS = {"theAddress", "thePhoneNumber"};

  private boolean isOrgDBPluginEnabled() {

    return VOMSConfiguration.instance()
      .getRegistrationType()
      .equals(OrgDBConfigurator.ORGDB_REGISTRATION_TYPE);
  }

  protected void validateRequiredFields() {
    
    if (requiredFields.contains("institution") && Strings.isNullOrEmpty(theInstitution)) {
      addFieldError("theInstitution", "Please provide a value for the institution");
    }
    
    if (requiredFields.contains("address") && Strings.isNullOrEmpty(theAddress)) {
      addFieldError("theAddress", "Please provide a value for the address");
    }
    
    if (requiredFields.contains("phoneNumber") && Strings.isNullOrEmpty(thePhoneNumber)) {
      addFieldError("thePhoneNumber", "Please provide a value for the phoneNumber");
    }
  }

  @Override
  public void validate() {

    // Run default checks before orgdb checks
    super.validate();
    
    validateRequiredFields();

    if (hasFieldErrors()) {

      if (!isOrgDBPluginEnabled()) {
        return;
      }

      Map<String, List<String>> fieldErrors = getFieldErrors();

      clearFieldErrors();

      // Retain only errors in the given fields
      fieldErrors.keySet().retainAll(Arrays.asList(ORGDB_VALIDATED_FIELDS));

      // Add those errors back to the set of field errors
      for (Map.Entry<String, List<String>> e : fieldErrors.entrySet()) {
        for (String msg : e.getValue()) {
          addFieldError(e.getKey(), msg);
        }
      }

      if (hasFieldErrors()) {
        return;
      }
    }

    if (isOrgDBPluginEnabled()) {

      OrgDBConfigurator conf = (OrgDBConfigurator) PluginManager.instance()
        .getConfiguredPlugin(OrgDBConfigurator.class.getName());

      if (conf == null) {
        throw new IllegalStateException("OrgDB plugin configured but configurator is null!");
      }

      OrgDBEmailValidationResult validationResult =
          conf.getEmailValidator().validateEmailAddress(getModel(), getTheEmailAddress());

      if (!validationResult.isValid()) {
        addActionError(validationResult.getValidationError());
      }
    }
  }

  @Override
  public String execute() throws Exception {

    SaveUserPersonalInfoOperation op = new SaveUserPersonalInfoOperation(getModel(), theName,
        theSurname, theInstitution, theAddress, thePhoneNumber, theEmailAddress);

    op.setAuthorizedUser(getModel());

    op.execute();

    addActionMessage("Personal information updated.");
    return SUCCESS;
  }

  @RequiredStringValidator(type = ValidatorType.FIELD, message = "Please provide a value.")
  @RegexFieldValidator(type = ValidatorType.FIELD,
      message = "The name field contains illegal characters!", regex = "^[^<>&=;]*$")
  public String getTheName() {

    return theName;
  }

  public void setTheName(String theName) {

    this.theName = theName;
  }

  @RequiredStringValidator(type = ValidatorType.FIELD, message = "Please provide a value.")
  @RegexFieldValidator(type = ValidatorType.FIELD,
      message = "The surname field contains illegal characters!", regex = "^[^<>&=;]*$")
  public String getTheSurname() {

    return theSurname;
  }

  public void setTheSurname(String theSurname) {

    this.theSurname = theSurname;
  }

  @RegexFieldValidator(type = ValidatorType.FIELD,
      message = "The institution field contains illegal characters!", regex = "^[^<>=;]*$")
  public String getTheInstitution() {

    return theInstitution;
  }

  public void setTheInstitution(String theInstitution) {

    this.theInstitution = theInstitution;
  }

  @RegexFieldValidator(type = ValidatorType.FIELD,
      message = "The address field contains illegal characters!", regex = "^[^<>&=;]*$")
  public String getTheAddress() {

    return theAddress;
  }

  public void setTheAddress(String theAddress) {

    this.theAddress = theAddress;
  }

  @RegexFieldValidator(type = ValidatorType.FIELD,
      message = "The phoneNumber field contains illegal characters!", regex = "^[^<>&=;]*$")
  public String getThePhoneNumber() {

    return thePhoneNumber;
  }

  public void setThePhoneNumber(String thePhoneNumber) {

    this.thePhoneNumber = thePhoneNumber;
  }

  @RequiredStringValidator(type = ValidatorType.FIELD, message = "Please enter an email address.")
  @EmailValidator(type = ValidatorType.FIELD, message = "Please enter a valid email address.")
  @RegexFieldValidator(type = ValidatorType.FIELD,
      message = "The email field name contains illegal characters!", regex = "^[^<>&=;]*$")
  public String getTheEmailAddress() {

    return theEmailAddress;
  }

  public void setTheEmailAddress(String theEmailAddress) {

    this.theEmailAddress = theEmailAddress;
  }

}
