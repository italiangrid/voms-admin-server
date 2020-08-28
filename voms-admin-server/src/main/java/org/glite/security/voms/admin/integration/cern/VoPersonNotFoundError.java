package org.glite.security.voms.admin.integration.cern;

public class VoPersonNotFoundError extends HrDbExchangeError {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  public VoPersonNotFoundError(int responseStatus, String responseMessage) {
    super(responseStatus, responseMessage);
  }

}
