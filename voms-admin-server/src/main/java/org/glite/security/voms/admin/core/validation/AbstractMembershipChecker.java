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

package org.glite.security.voms.admin.core.validation;

import org.glite.security.voms.admin.core.validation.strategies.AUPFailingMembersLookupStrategy;
import org.glite.security.voms.admin.core.validation.strategies.ExpiredMembersLookupStrategy;
import org.glite.security.voms.admin.core.validation.strategies.ExpiringMembersLookupStrategy;
import org.glite.security.voms.admin.core.validation.strategies.HandleAUPFailingMembersStrategy;
import org.glite.security.voms.admin.core.validation.strategies.HandleExpiredMembersStrategy;
import org.glite.security.voms.admin.core.validation.strategies.HandleExpiringMembersStrategy;
import org.glite.security.voms.admin.core.validation.strategies.VOMembershipCheckStrategy;

public abstract class AbstractMembershipChecker implements
  VOMembershipCheckStrategy {

  MembershipValidationContext validationContext;

  /**
   * @return the validationContext
   */
  public MembershipValidationContext getValidationContext() {

    return validationContext;
  }

  /**
   * @param validationContext
   *          the validationContext to set
   */
  public void setValidationContext(MembershipValidationContext validationContext) {

    this.validationContext = validationContext;
  }

  /**
   * @return
   * @see org.glite.security.voms.admin.core.validation.MembershipValidationContext#getAupFailingMembersLookupStrategy()
   */
  public AUPFailingMembersLookupStrategy getAupFailingMembersLookupStrategy() {

    return validationContext.getAupFailingMembersLookupStrategy();
  }

  /**
   * @return
   * @see org.glite.security.voms.admin.core.validation.MembershipValidationContext#getExpiredMembersLookupStrategy()
   */
  public ExpiredMembersLookupStrategy getExpiredMembersLookupStrategy() {

    return validationContext.getExpiredMembersLookupStrategy();
  }

  /**
   * @return
   * @see org.glite.security.voms.admin.core.validation.MembershipValidationContext#getExpiringMembersLookupStrategy()
   */
  public ExpiringMembersLookupStrategy getExpiringMembersLookupStrategy() {

    return validationContext.getExpiringMembersLookupStrategy();
  }

  /**
   * @return
   * @see org.glite.security.voms.admin.core.validation.MembershipValidationContext#getHandleExpiredMembersStrategy()
   */
  public HandleExpiredMembersStrategy getHandleExpiredMembersStrategy() {

    return validationContext.getHandleExpiredMembersStrategy();
  }

  /**
   * @return
   * @see org.glite.security.voms.admin.core.validation.MembershipValidationContext#getHandleExpiringMembersStrategy()
   */
  public HandleExpiringMembersStrategy getHandleExpiringMembersStrategy() {

    return validationContext.getHandleExpiringMembersStrategy();
  }

  /**
   * @return
   * @see org.glite.security.voms.admin.core.validation.MembershipValidationContext#getHandleAUPFailingMembersStrategy()
   */
  public HandleAUPFailingMembersStrategy getHandleAUPFailingMembersStrategy() {

    return validationContext.getHandleAUPFailingMembersStrategy();
  }

}
