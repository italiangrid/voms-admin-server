package org.glite.security.voms.admin.util.validation.x509;

import org.glite.security.voms.admin.error.VOMSException;

public class DnValidationError extends VOMSException {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  public DnValidationError(String message) {
    super(message);

  }

  public DnValidationError(String message, Throwable t) {
    super(message, t);

  }

  public DnValidationError(Throwable t) {
    super(t);

  }

}
