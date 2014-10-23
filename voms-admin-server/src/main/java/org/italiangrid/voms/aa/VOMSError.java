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

package org.italiangrid.voms.aa;

import org.glite.security.voms.admin.error.VOMSException;
import org.glite.security.voms.admin.error.VOMSSyntaxException;
import org.glite.security.voms.admin.persistence.error.NoSuchCertificateException;
import org.glite.security.voms.admin.persistence.error.NoSuchUserException;
import org.glite.security.voms.admin.persistence.error.SuspendedCertificateException;
import org.glite.security.voms.admin.persistence.error.SuspendedUserException;

public enum VOMSError {

  NoSuchUser(1001, 403, "No such user."), NoSuchAttribute(1007, 400,
    "Cannot issue a user requested attribtue."), SuspendedUser(1004, 403,
    "The user is supended."), SuspendedCertificate(1001, 403,
    "The user certificate is suspended."), BadRequest(1006, 400, "Bad request."), InternalError(
    1006, 500, "Internal server error."), EndpointDisabled(1006, 500,
    "VOMS endpoint is currently not enabled."), UnauthenticatedClient(1006,
    400, "Client is not authenticated.");

  private VOMSError(int legacyCode, int httpStatus, String message) {

    this.legacyErrorCode = legacyCode;
    this.httpStatus = httpStatus;
    this.defaultMessage = message;
  }

  private int legacyErrorCode;
  private int httpStatus;
  private String defaultMessage;

  public int getHttpStatus() {

    return httpStatus;
  }

  public int getLegacyErrorCode() {

    return legacyErrorCode;
  }

  public String getDefaultMessage() {

    return defaultMessage;
  }

  public void setDefaultMessage(String message) {

    this.defaultMessage = message;
  }

  public static VOMSError fromException(VOMSException e) {

    VOMSError m;

    if (e instanceof NoSuchUserException
      || e instanceof NoSuchCertificateException) {
      m = NoSuchUser;
      m.setDefaultMessage(e.getMessage());
    } else if (e instanceof SuspendedUserException) {
      m = SuspendedUser;
      m.setDefaultMessage(e.getMessage());
    } else if (e instanceof SuspendedCertificateException) {
      m = SuspendedCertificate;
      m.setDefaultMessage(e.getMessage());
    } else if (e instanceof VOMSSyntaxException) {
      m = BadRequest;
      m.setDefaultMessage(e.getMessage());
    } else {
      m = InternalError;
      m.setDefaultMessage(e.getMessage());
    }

    return m;
  }
}
