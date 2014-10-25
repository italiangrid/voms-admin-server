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
package org.glite.security.voms.admin.view.actions.aup;

import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.glite.security.voms.admin.operations.aup.AddVersionOperation;
import org.glite.security.voms.admin.persistence.model.AUPVersion;
import org.glite.security.voms.admin.view.actions.BaseAction;

import com.opensymphony.xwork2.validator.annotations.RegexFieldValidator;
import com.opensymphony.xwork2.validator.annotations.RequiredStringValidator;
import com.opensymphony.xwork2.validator.annotations.ValidatorType;

@Results({
  @Result(name = BaseAction.INPUT, location = "addAupVersion"),
  @Result(name = BaseAction.SUCCESS, location = "/aup/load.action",
    type = "redirect") })
@InterceptorRef(value = "authenticatedStack", params = {
  "token.includeMethods", "execute" })
public class AddVersionAction extends AUPVersionActions {

  /**
	 * 
	 */
  private static final long serialVersionUID = 1L;
  String url;

  @Override
  public void validate() {

    for (AUPVersion existingVersion : getModel().getVersions())
      if (existingVersion.getVersion().equals(getVersion()))
        addFieldError("version", "Version '" + getVersion()
          + "' already exists for this aup!");

    AUPVersion candidateVersion = new AUPVersion();
    candidateVersion.setUrl(url);
    if (candidateVersion.getURLContent() == null) {
      addFieldError(
        "url",
        "Error fetching the content of the specified URL! Please provide a valid URL pointing to a text file!");
    }

  }

  @Override
  public String execute() throws Exception {

    AddVersionOperation op = new AddVersionOperation(getModel(), getVersion(),
      url);
    op.execute();

    return SUCCESS;

  }

  @RequiredStringValidator(type = ValidatorType.FIELD,
    message = "The url field is required!")
  @RegexFieldValidator(type = ValidatorType.FIELD,
    message = "The version field contains illegal characters!",
    expression = "^[^<>&=;]*$")
  public String getUrl() {

    return url;
  }

  public void setUrl(String url) {

    this.url = url;
  }
}
