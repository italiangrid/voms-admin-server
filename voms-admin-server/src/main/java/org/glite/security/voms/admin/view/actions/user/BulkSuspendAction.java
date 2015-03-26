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
package org.glite.security.voms.admin.view.actions.user;

import java.util.Collections;
import java.util.List;

import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.glite.security.voms.admin.operations.DoubleArgumentOperationCollection;
import org.glite.security.voms.admin.operations.users.SuspendUserOperation;
import org.glite.security.voms.admin.view.actions.BaseAction;

import com.opensymphony.xwork2.validator.annotations.RegexFieldValidator;
import com.opensymphony.xwork2.validator.annotations.ValidatorType;

@Results({

  @Result(name = BaseAction.SUCCESS, location = "search",
    type = "redirectAction"),
  @Result(name = BaseAction.INPUT, location = "users") })
@InterceptorRef(value = "authenticatedStack", params = {
  "token.includeMethods", "execute" })
public class BulkSuspendAction extends UserBulkActionSupport {

  /**
	 * 
	 */
  private static final long serialVersionUID = 1L;

  String suspensionReason;

  @RegexFieldValidator(type = ValidatorType.FIELD, regex = "^[^<>&=;]*$",
    message = "You entered invalid characters in the suspension reason field!")
  public String getSuspensionReason() {

    return suspensionReason;
  }

  public void setSuspensionReason(String suspensionReason) {

    this.suspensionReason = suspensionReason;
  }

  @Override
  public String execute() throws Exception {

    List<String> reasonsList = Collections.nCopies(userIds.size(),
      suspensionReason);
    DoubleArgumentOperationCollection<Long, String> op = new DoubleArgumentOperationCollection<Long, String>(
      userIds, reasonsList, SuspendUserOperation.class);

    op.execute();
    return SUCCESS;
  }
}
