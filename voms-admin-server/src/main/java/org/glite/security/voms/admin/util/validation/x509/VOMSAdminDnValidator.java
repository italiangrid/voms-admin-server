package org.glite.security.voms.admin.util.validation.x509;

import org.glite.security.voms.admin.error.IllegalStateException;

public enum VOMSAdminDnValidator {

  INSTANCE;

  DnValidator theValidator;

  public void initialize(String trustAnchorsDir, boolean openssl1Mode) {

    if (theValidator == null) {
      theValidator = new CanlDNValidator(trustAnchorsDir, openssl1Mode);
    }

  }

  public DnValidator getValidator() {

    if (theValidator != null) {
      return theValidator;
    }

    throw new IllegalStateException("Dn Validator not initialized!");
  }

}
