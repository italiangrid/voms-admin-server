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
package org.glite.security.voms.admin.operations;

public class AuthorizationResponse {

  boolean allowed;

  private AuthorizationResponse() {

  }

  private VOMSContext failingContext;
  private VOMSPermission requiredPermissions;
  private String failureReason;

  public static AuthorizationResponse permit() {

    AuthorizationResponse resp = new AuthorizationResponse();
    resp.allowed = true;
    return resp;
  }

  public static AuthorizationResponse deny(VOMSContext failingContext,
    VOMSPermission requiredPermissions) {

    AuthorizationResponse resp = new AuthorizationResponse();
    resp.allowed = false;
    resp.failingContext = failingContext;
    resp.requiredPermissions = requiredPermissions;
    return resp;
  }

  public boolean isAllowed() {

    return allowed;
  }

  public void setAllowed(boolean allowed) {

    this.allowed = allowed;
  }

  public VOMSContext getFailingContext() {

    return failingContext;
  }

  public void setFailingContext(VOMSContext failingContext) {

    this.failingContext = failingContext;
  }

  public VOMSPermission getRequiredPermissions() {

    return requiredPermissions;
  }

  public void setRequiredPermissions(VOMSPermission requiredPermissions) {

    this.requiredPermissions = requiredPermissions;
  }

  public String getFailureReason() {

    return failureReason;
  }

  public void setFailureReason(String failureReason) {

    this.failureReason = failureReason;
  }
}
