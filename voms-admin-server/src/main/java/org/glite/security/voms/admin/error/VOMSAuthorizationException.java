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
package org.glite.security.voms.admin.error;

import org.glite.security.voms.admin.operations.AuthorizationResponse;
import org.glite.security.voms.admin.operations.VOMSOperation;
import org.glite.security.voms.admin.persistence.model.VOMSAdmin;

public class VOMSAuthorizationException extends VOMSSecurityException {

  private static final long serialVersionUID = 1L;

  private VOMSAdmin admin;

  private VOMSOperation operation;

  private AuthorizationResponse authorizationResponse;

  public VOMSAdmin getAdmin() {

    return admin;
  }

  public void setAdmin(VOMSAdmin admin) {

    this.admin = admin;
  }

  public VOMSOperation getOperation() {

    return operation;
  }

  public void setOperation(VOMSOperation operation) {

    this.operation = operation;
  }

  public VOMSAuthorizationException(VOMSAdmin a, VOMSOperation o,
    AuthorizationResponse response) {

    this.admin = a;
    this.operation = o;
    this.authorizationResponse = response;

  }

  @Override
  public String getMessage() {

    return String.format("Insufficient privileges to execute '%s'.",
      operation.getName());
  }

  public AuthorizationResponse getAuthorizationResponse() {

    return authorizationResponse;
  }

  public void setAuthorizationResponse(
    AuthorizationResponse authorizationResponse) {

    this.authorizationResponse = authorizationResponse;
  }

  @Override
  public String toString() {

    return getClass().getName() + ":" + getMessage();
  }

}
