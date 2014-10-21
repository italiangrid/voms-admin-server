package org.italiangrid.voms.container.legacy;

public enum LegacyHTTPHeader {

  LEGACY_REQUEST_HEADER("X-VOMS-LEGACY", "true"), USER_AGENT("User-Agent",
    "VOMS legacy client"), ACCEPT("Accept", "*/*");

  private String name;
  private String value;

  private LegacyHTTPHeader(String name, String value) {

    this.name = name;
    this.value = value;
  }

  public String getHeaderName() {

    return name;
  }

  public String getHeaderValue() {

    return value;
  }
}
