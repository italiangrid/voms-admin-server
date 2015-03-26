/**
 * Copyright (c) Members of the EGEE Collaboration. 2006-2009. See
 * http://www.eu-egee.org/partners/ for details on the copyright holders.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 * 
 * Authors: Andrea Ceccanti (INFN)
 */
package org.glite.security.voms.admin.view.actions.user;

import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.interceptor.TokenInterceptor;
import org.glite.security.voms.admin.configuration.VOMSConfiguration;
import org.glite.security.voms.admin.configuration.VOMSConfigurationConstants;
import org.glite.security.voms.admin.error.IllegalStateException;
import org.glite.security.voms.admin.integration.PluginManager;
import org.glite.security.voms.admin.integration.orgdb.OrgDBConfigurator;
import org.glite.security.voms.admin.integration.orgdb.strategies.OrgDBEmailValidationResult;
import org.glite.security.voms.admin.operations.users.SaveUserPersonalInfoOperation;

import com.opensymphony.xwork2.validator.annotations.EmailValidator;
import com.opensymphony.xwork2.validator.annotations.RegexFieldValidator;
import com.opensymphony.xwork2.validator.annotations.RequiredStringValidator;
import com.opensymphony.xwork2.validator.annotations.ValidatorType;

@Results({
  @Result(name = UserActionSupport.SUCCESS, location = "personalInfo.jsp"),
  @Result(name = UserActionSupport.INPUT, location = "personalInfo.jsp"),
  @Result(name = TokenInterceptor.INVALID_TOKEN_CODE,
    location = "personalInfo.jsp") })
@InterceptorRef(value = "authenticatedStack", params = {
  "token.includeMethods", "execute" })
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

  private boolean isOrgDBPluginEnabled() {

    return VOMSConfiguration.instance().getRegistrationType()
      .equals(OrgDBConfigurator.ORGDB_REGISTRATION_TYPE);
  }

  @Override
  public void validate() {

    // Run default checks before orgdb checks
    super.validate();
    if (hasFieldErrors())
      return;

    if (isOrgDBPluginEnabled()) {

      OrgDBConfigurator conf = (OrgDBConfigurator) PluginManager.instance()
        .getConfiguredPlugin(OrgDBConfigurator.class.getName());

      if (conf == null)
        throw new IllegalStateException(
          "OrgDB plugin configured but configurator is null!");

      OrgDBEmailValidationResult validationResult = conf.getEmailValidator()
        .validateEmailAddress(getModel(), getTheEmailAddress());

      if (!validationResult.isValid()) {
        addActionError(validationResult.getValidationError());
      }
    }
  }

  @Override
  public String execute() throws Exception {

    SaveUserPersonalInfoOperation op = new SaveUserPersonalInfoOperation(
      getModel(), theName, theSurname, theInstitution, theAddress,
      thePhoneNumber, theEmailAddress);

    op.setAuthorizedUser(getModel());

    op.execute();

    addActionMessage("Personal information updated.");
    return SUCCESS;
  }

  @RegexFieldValidator(type = ValidatorType.FIELD,
    message = "The name field contains illegal characters!",
    regex = "^[^<>&=;]*$")
  public String getTheName() {

    return theName;
  }

  public void setTheName(String theName) {

    this.theName = theName;
  }

  @RegexFieldValidator(type = ValidatorType.FIELD,
    message = "The surname field contains illegal characters!",
    regex = "^[^<>&=;]*$")
  public String getTheSurname() {

    return theSurname;
  }

  public void setTheSurname(String theSurname) {

    this.theSurname = theSurname;
  }

  @RegexFieldValidator(type = ValidatorType.FIELD,
    message = "The institution field contains illegal characters!",
    regex = "^[^<>&=;]*$")
  public String getTheInstitution() {

    return theInstitution;
  }

  public void setTheInstitution(String theInstitution) {

    this.theInstitution = theInstitution;
  }

  @RegexFieldValidator(type = ValidatorType.FIELD,
    message = "The address field contains illegal characters!",
    regex = "^[^<>&=;]*$")
  public String getTheAddress() {

    return theAddress;
  }

  public void setTheAddress(String theAddress) {

    this.theAddress = theAddress;
  }

  @RegexFieldValidator(type = ValidatorType.FIELD,
    message = "The phoneNumber field contains illegal characters!",
    regex = "^[^<>&=;]*$")
  public String getThePhoneNumber() {

    return thePhoneNumber;
  }

  public void setThePhoneNumber(String thePhoneNumber) {

    this.thePhoneNumber = thePhoneNumber;
  }

  @RequiredStringValidator(type = ValidatorType.FIELD,
    message = "Please enter an email address.")
  @EmailValidator(type = ValidatorType.FIELD,
    message = "Please enter a valid email address.")
  @RegexFieldValidator(type = ValidatorType.FIELD,
    message = "The email field name contains illegal characters!",
    regex = "^[^<>&=;]*$")
  public String getTheEmailAddress() {

    return theEmailAddress;
  }

  public void setTheEmailAddress(String theEmailAddress) {

    this.theEmailAddress = theEmailAddress;
  }

}
