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
package org.glite.security.voms.admin.integration.cern.dto;

public class ErrorDTO {

  final String error;
  final String errorMessage;

  private ErrorDTO(String error, String errorMessage) {
    this.error = error;
    this.errorMessage = errorMessage;
  }

  private ErrorDTO(String error) {
    this(error, null);
  }

  public String getError() {
    return error;
  }

  public String getErrorMessage() {
    return errorMessage;
  }

  public static ErrorDTO newError(String error, String errorMessage) {
    return new ErrorDTO(error, errorMessage);
  }

  public static ErrorDTO newError(String error) {
    return new ErrorDTO(error);
  }


}
