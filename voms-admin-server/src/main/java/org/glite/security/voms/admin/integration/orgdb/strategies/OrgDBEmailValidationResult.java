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
