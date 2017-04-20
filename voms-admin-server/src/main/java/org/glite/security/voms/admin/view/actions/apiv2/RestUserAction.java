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
package org.glite.security.voms.admin.view.actions.apiv2;

import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.json.annotations.JSON;
import org.glite.security.voms.admin.persistence.dao.VOMSUserDAO;
import org.glite.security.voms.admin.persistence.model.VOMSUser;
import org.glite.security.voms.admin.view.actions.BaseAction;

import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import com.opensymphony.xwork2.validator.annotations.RequiredStringValidator;
import com.opensymphony.xwork2.validator.annotations.ValidatorType;

@ParentPackage("json")
@Results({ @Result(name = BaseAction.SUCCESS, type = "json") })
public class RestUserAction extends BaseAction implements Preparable,
  ModelDriven<VOMSUser> {

  /**
	 * 
	 */
  private static final long serialVersionUID = 1L;

  VOMSUser user;

  Long userId;

  String certificateSubject;
  String caSubject;

  public VOMSUser getModel() {

    return user;
  }

  public void prepare() throws Exception {

    if (getModel() == null) {

      if ((certificateSubject == null) || (caSubject == null)) {
        return;
      }

      user = VOMSUserDAO.instance().lookup(certificateSubject,
        caSubject);
    }
  }

  @RequiredStringValidator(type = ValidatorType.FIELD,
      message = "Please provide a certificate subject for the user.")
  public String getCertificateSubject() {

    return certificateSubject;
  }

  @RequiredStringValidator(type = ValidatorType.FIELD,
      message = "Please provide a certificate issuer subject for the user.")
  public String getCaSubject() {

    return caSubject;
  }

  public void setUserId(Long userId) {

    this.userId = userId;
  }

  public void setCertificateSubject(String certificateSubject) {

    this.certificateSubject = certificateSubject;
  }

  public void setCaSubject(String caSubject) {

    this.caSubject = caSubject;
  }

  @JSON(serialize = false)
  public Long getUserId() {

    return userId;
  }

}
