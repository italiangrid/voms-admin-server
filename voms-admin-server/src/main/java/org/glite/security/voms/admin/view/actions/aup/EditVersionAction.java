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
package org.glite.security.voms.admin.view.actions.aup;

import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.glite.security.voms.admin.operations.aup.SaveVersionOperation;
import org.glite.security.voms.admin.persistence.dao.generic.AUPDAO;
import org.glite.security.voms.admin.persistence.dao.generic.DAOFactory;
import org.glite.security.voms.admin.persistence.model.AUPVersion;
import org.glite.security.voms.admin.util.URLContentFetcher;
import org.glite.security.voms.admin.view.actions.BaseAction;

import com.opensymphony.xwork2.validator.annotations.RegexFieldValidator;
import com.opensymphony.xwork2.validator.annotations.RequiredStringValidator;
import com.opensymphony.xwork2.validator.annotations.ValidatorType;

@Results({
  @Result(name = BaseAction.INPUT, location = "editAupVersion"),
  @Result(name = BaseAction.SUCCESS, location = "/aup/load.action",
    type = "redirect") })
@InterceptorRef(value = "authenticatedStack", params = {
  "token.includeMethods", "execute" })
public class EditVersionAction extends AUPVersionActions {

  /**
     * 
     */
  private static final long serialVersionUID = 1L;

  String url;

  @Override
  public void prepare() throws Exception {

    if (aup == null) {

      AUPDAO dao = DAOFactory.instance().getAUPDAO();
      aup = dao.findById(aupId, false);
    }

    if (version != null) {
      url = aup.getVersion(version).getUrl();
    }
  }

  @Override
  public void validate() {

    String newAUPcontent = URLContentFetcher.fetchTextFromURL(url);

    if (newAUPcontent == null) {
      addFieldError(
        "url",
        "Error fetching the content of the specified URL! Please provide a valid URL pointing to a text file!");
    }

  }

  @Override
  public String execute() throws Exception {

    AUPVersion aupVersion = aup.getVersion(version);
    SaveVersionOperation op = new SaveVersionOperation(aupVersion, getUrl());
    op.execute();

    return SUCCESS;
  }

  @RequiredStringValidator(type = ValidatorType.FIELD,
    message = "The url field is required!")
  @RegexFieldValidator(type = ValidatorType.FIELD,
    message = "The version field contains illegal characters!",
    regex = "^[^<>&=;]*$")
  public String getUrl() {

    return url;
  }

  public void setUrl(String url) {

    this.url = url;
  }
}
