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
package org.glite.security.voms.admin.view.actions.register;

import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.glite.security.voms.admin.configuration.VOMSConfiguration;
import org.glite.security.voms.admin.configuration.VOMSConfigurationConstants;
import org.glite.security.voms.admin.operations.CurrentAdmin;
import org.glite.security.voms.admin.view.actions.BaseAction;

@Results({
  @Result(name = BaseAction.SUCCESS, location = "register"),
  @Result(name = RegisterActionSupport.CONFIRMATION_NEEDED,
    location = "registerWaitsConfirmation"),
  @Result(name = RegisterActionSupport.PLEASE_WAIT, location = "registerLimbo"),
  @Result(name = RegisterActionSupport.ALREADY_MEMBER,
    location = "/user/home.action", type = "redirect"),
  @Result(name = RegisterActionSupport.REGISTRATION_DISABLED,
    location = "registrationDisabled"),
  @Result(name = RegisterActionSupport.UNAUTHENTICATED_CLIENT,
    location = "guest") })
public class StartAction extends RegisterActionSupport {

  /**
	 * 
	 */
  private static final long serialVersionUID = 1L;

  @Override
  public String execute() throws Exception {

    if (!registrationEnabled())
      return REGISTRATION_DISABLED;

    if (VOMSConfiguration.instance().getBoolean(
      VOMSConfigurationConstants.READONLY, false))
      return REGISTRATION_DISABLED;

    if (CurrentAdmin.instance().isUnauthenticated())
      return UNAUTHENTICATED_CLIENT;

    if (CurrentAdmin.instance().getVoUser() != null)
      return ALREADY_MEMBER;

    String result = checkExistingPendingRequests();

    if (result != null)
      return result;

    return SUCCESS;
  }
}
