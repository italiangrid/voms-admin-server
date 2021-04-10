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

import java.util.Collection;
import java.util.regex.Pattern;

import org.apache.logging.log4j.util.Strings;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.json.annotations.JSON;
import org.glite.security.voms.admin.operations.users.SuspendUserOperation;
import org.glite.security.voms.admin.persistence.model.VOMSUser.SuspensionReason;
import org.glite.security.voms.admin.view.actions.BaseAction;

@ParentPackage("json")
@Results({ @Result(name = BaseAction.SUCCESS, type = "json") })
public class SuspendUserAction extends RestUserAction {

  private static final Pattern REGEX = Pattern.compile("^[^<>&;]*$");

  @Override
  public void validate() {

    // Workaround for annotation validation not working anymore
    // due to OGNL not finding the right value in the OGNL stack

    if (Strings.isBlank(suspensionReason)) {
      addFieldError("suspensionReason", "Please provide a reason for the suspension.");
    } else {

      if (!REGEX.matcher(suspensionReason).matches()) {
        addFieldError("suspensionReason", "The reason contains illegal characters.");
      }

    }
  }
  /**
	 * 
	 */
  private static final long serialVersionUID = 1L;

  String suspensionReason;

  @Override
  public String execute() throws Exception {

    SuspensionReason r = SuspensionReason.OTHER;
    r.setMessage(getSuspensionReason());

    SuspendUserOperation.instance(getModel(), r).execute();

    addActionMessage(String.format("User %s is now suspended", getModel()
      .getShortName()));
    return SUCCESS;
  }


  public String getSuspensionReason() {
    return suspensionReason;
  }

  public void setSuspensionReason(String suspensionReason) {

    this.suspensionReason = suspensionReason;
  }

  @JSON(serialize = true)
  @Override
  public Collection<String> getActionMessages() {

    return super.getActionMessages();
  }
}
