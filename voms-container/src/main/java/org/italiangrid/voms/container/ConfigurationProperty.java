package org.italiangrid.voms.container;

public enum ConfigurationProperty {

  HOST("host", "localhost"),

  PORT("port", "8443"),

  CERT("cert", "/etc/grid-security/hostcert.pem"),

  KEY("key", "/etc/grid-security/hostkey.pem"),

  TRUST_ANCHORS_DIR("trust_anchors.dir", "/etc/grid-security/certificates"),

  // In seconds
  TRUST_ANCHORS_REFRESH_PERIOD("trust_anchors.refresh_period", "21600"),

  MAX_CONNECTIONS("max_connections", "100"),

  MAX_REQUEST_QUEUE_SIZE("max_request_queue_size", "200"),

  BIND_ADDRESS("bind_address", null);

  private ConfigurationProperty(String propertyName, String defaultValue) {

    this.propertyName = propertyName;
    this.defaultValue = defaultValue;

  }

  private String propertyName;
  private String defaultValue;

  public String getPropertyName() {

    return propertyName;
  }

  public String getDefaultValue() {

    return defaultValue;
  }

  @Override
  public String toString() {

    return propertyName;
  }
}
