package org.glite.security.voms.admin.util.validation.x509;

public interface DnValidator {

  public DnValidationResult validate(String issuerSubject,
    String certificateSubject);

}
