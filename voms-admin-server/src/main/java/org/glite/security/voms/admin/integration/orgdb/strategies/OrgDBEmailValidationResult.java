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
package org.glite.security.voms.admin.integration.orgdb.strategies;

public class OrgDBEmailValidationResult {

  private boolean valid;
  private String validationError;

  private OrgDBEmailValidationResult(boolean valid, String validationError) {

    this.valid = valid;
    this.validationError = validationError;
  }

  public static OrgDBEmailValidationResult valid() {

    return new OrgDBEmailValidationResult(true, null);
  }

  public static OrgDBEmailValidationResult invalid(String error) {

    return new OrgDBEmailValidationResult(false, error);
  }

  public boolean isValid() {

    return valid;
  }

  public String getValidationError() {

    return validationError;
  }

}
