package org.glite.security.voms.admin.integration.cern;

public class HrDbExchangeError extends HrDbError {

  private static final long serialVersionUID = 1L;

  private final int responseStatus;

  public HrDbExchangeError(int responseStatus, String responseMessage) {
    super(responseMessage);
    this.responseStatus = responseStatus;
  }

  public int getResponseStatus() {
    return responseStatus;
  }

}
