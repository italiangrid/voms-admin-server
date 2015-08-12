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
package org.glite.security.voms.admin.core.validation;

import org.glite.security.voms.admin.core.validation.strategies.AUPFailingMembersLookupStrategy;
import org.glite.security.voms.admin.core.validation.strategies.ExpiredMembersLookupStrategy;
import org.glite.security.voms.admin.core.validation.strategies.ExpiringMembersLookupStrategy;
import org.glite.security.voms.admin.core.validation.strategies.HandleAUPFailingMembersStrategy;
import org.glite.security.voms.admin.core.validation.strategies.HandleExpiredMembersStrategy;
import org.glite.security.voms.admin.core.validation.strategies.HandleExpiringMembersStrategy;

public interface MembershipValidationContext {

  AUPFailingMembersLookupStrategy getAupFailingMembersLookupStrategy();

  ExpiredMembersLookupStrategy getExpiredMembersLookupStrategy();

  ExpiringMembersLookupStrategy getExpiringMembersLookupStrategy();

  HandleAUPFailingMembersStrategy getHandleAUPFailingMembersStrategy();

  HandleExpiredMembersStrategy getHandleExpiredMembersStrategy();

  HandleExpiringMembersStrategy getHandleExpiringMembersStrategy();

}
