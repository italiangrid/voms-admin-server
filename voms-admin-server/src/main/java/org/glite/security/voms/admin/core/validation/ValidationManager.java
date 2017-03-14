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
package org.glite.security.voms.admin.core.validation;

import org.glite.security.voms.admin.configuration.VOMSConfigurationConstants;
import org.glite.security.voms.admin.core.tasks.VOMSExecutorService;
import org.glite.security.voms.admin.core.validation.strategies.RequestValidationStrategy;
import org.glite.security.voms.admin.core.validation.strategies.RestoreUserStrategy;
import org.glite.security.voms.admin.core.validation.strategies.SuspendUserStrategy;
import org.glite.security.voms.admin.core.validation.strategies.impl.DefaultMembershipCheckBehaviour;
import org.glite.security.voms.admin.core.validation.strategies.impl.DefaultRequestValidationBehaviour;
import org.glite.security.voms.admin.core.validation.strategies.impl.DefaultUserSuspensionManagementBehaviour;
import org.glite.security.voms.admin.persistence.model.VOMSUser;
import org.glite.security.voms.admin.persistence.model.VOMSUser.SuspensionReason;
import org.glite.security.voms.admin.persistence.model.request.NewVOMembershipRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ValidationManager implements SuspendUserStrategy,
  RestoreUserStrategy, RequestValidationStrategy<NewVOMembershipRequest> {

  public static Logger log = LoggerFactory.getLogger(ValidationManager.class);

  private static volatile ValidationManager INSTANCE = null;

  protected RequestValidationContext requestValidationContext;

  protected MembershipValidationContext membershipValidationContext;

  protected UserSuspensionManagementContext userSuspensionManagementContext;

  private ValidationManager() {

    membershipValidationContext = new DefaultMembershipCheckBehaviour();
    requestValidationContext = new DefaultRequestValidationBehaviour();
    userSuspensionManagementContext = new DefaultUserSuspensionManagementBehaviour();

  }

  public synchronized static ValidationManager instance() {

    if (INSTANCE == null)
      INSTANCE = new ValidationManager();

    return INSTANCE;
  }

  public void startMembershipChecker() {

    log.debug("Starting membership checker task.");

    MembershipCheckerTask checker = new MembershipCheckerTask(
      membershipValidationContext);
    VOMSExecutorService.instance().startBackgroundTask(checker,
      VOMSConfigurationConstants.MEMBERSHIP_CHECK_PERIOD, 300L);

    log.debug("Membership validation context: {}", membershipValidationContext);
    log.debug("Request validation context: {}", requestValidationContext);
    log.debug("User suspension management context: {}",
      userSuspensionManagementContext);

  }

  /**
   * @return the requestValidationContext
   */
  public RequestValidationContext getRequestValidationContext() {

    return requestValidationContext;
  }

  /**
   * @param requestValidationContext
   *          the requestValidationContext to set
   */
  public void setRequestValidationContext(
    RequestValidationContext requestValidationContext) {

    this.requestValidationContext = requestValidationContext;
  }

  /**
   * @return the membershipValidationContext
   */
  public MembershipValidationContext getMembershipValidationContext() {

    return membershipValidationContext;
  }

  /**
   * @param membershipValidationContext
   *          the membershipValidationContext to set
   */
  public void setMembershipValidationContext(
    MembershipValidationContext membershipValidationContext) {

    this.membershipValidationContext = membershipValidationContext;
  }

  /**
   * @return the userSuspensionManagementContext
   */
  public UserSuspensionManagementContext getUserSuspensionManagementContext() {

    return userSuspensionManagementContext;
  }

  /**
   * @param userSuspensionManagementContext
   *          the userSuspensionManagementContext to set
   */
  public void setUserSuspensionManagementContext(
    UserSuspensionManagementContext userSuspensionManagementContext) {

    this.userSuspensionManagementContext = userSuspensionManagementContext;
  }

  public void suspendUser(VOMSUser user, SuspensionReason suspensionReason) {

    getUserSuspensionManagementContext().getSuspendUserStrategy().suspendUser(
      user, suspensionReason);

  }

  public void restoreUser(VOMSUser user) {

    getUserSuspensionManagementContext().getRestoreUserStrategy().restoreUser(
      user);

  }

  public RequestValidationResult validateRequest(NewVOMembershipRequest r) {

    return getRequestValidationContext()
      .getVOMembershipRequestValidationStrategy().validateRequest(r);

  }

}
