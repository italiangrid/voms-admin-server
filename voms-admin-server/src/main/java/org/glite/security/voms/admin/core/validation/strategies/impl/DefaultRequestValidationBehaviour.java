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
package org.glite.security.voms.admin.core.validation.strategies.impl;

import org.glite.security.voms.admin.core.validation.RequestValidationContext;
import org.glite.security.voms.admin.core.validation.RequestValidationResult;
import org.glite.security.voms.admin.core.validation.strategies.RequestValidationStrategy;
import org.glite.security.voms.admin.persistence.model.request.NewVOMembershipRequest;

public class DefaultRequestValidationBehaviour implements
  RequestValidationStrategy<NewVOMembershipRequest>, RequestValidationContext {

  public RequestValidationResult validateRequest(NewVOMembershipRequest r) {

    return RequestValidationResult.success();
  }

  public RequestValidationStrategy<NewVOMembershipRequest> getVOMembershipRequestValidationStrategy() {

    return this;
  }

}
