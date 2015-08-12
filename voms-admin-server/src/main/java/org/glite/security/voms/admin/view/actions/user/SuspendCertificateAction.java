/**
 * Copyright (c) Istituto Nazionale di Fisica Nucleare (INFN). 2006-2015
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

import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.interceptor.TokenInterceptor;
import org.glite.security.voms.admin.operations.users.SuspendUserCertificateOperation;
import org.glite.security.voms.admin.persistence.model.VOMSUser.SuspensionReason;
import org.glite.security.voms.admin.view.actions.BaseAction;

import com.opensymphony.xwork2.validator.annotations.RegexFieldValidator;
import com.opensymphony.xwork2.validator.annotations.ValidatorType;

@Results({
  @Result(name = BaseAction.SUCCESS, location = "certificates.jsp"),
  @Result(name = BaseAction.INPUT, location = "certificates.jsp"),
  @Result(name = TokenInterceptor.INVALID_TOKEN_CODE,
    location = "certificates.jsp")

})
@InterceptorRef(value = "authenticatedStack", params = {
  "token.includeMethods", "execute" })
public class SuspendCertificateAction extends CertificateActionSupport {

  /**
	 * 
	 */
  private static final long serialVersionUID = 1L;

  String suspensionReason;

  @Override
  public String execute() throws Exception {

    SuspensionReason r = SuspensionReason.OTHER;
    r.setMessage(suspensionReason);

    SuspendUserCertificateOperation.instance(getModel(), certificate, r)
      .execute();

    return SUCCESS;
  }

  @RegexFieldValidator(type = ValidatorType.FIELD, expression = "^[^<>&=;]*$",
    message = "You entered invalid characters in the suspension reason field!")
  public String getSuspensionReason() {

    return suspensionReason;
  }

  public void setSuspensionReason(String suspensionReason) {

    this.suspensionReason = suspensionReason;
  }

  @Override
  public void validate() {

    // Actually this is needed for this action since the field is in a dialog
    // that will be closed when the validation
    // returns...
    if (!getFieldErrors().isEmpty()) {
      addActionError("You entered invalid characters in the suspension reason field!");
    }
  }

}
