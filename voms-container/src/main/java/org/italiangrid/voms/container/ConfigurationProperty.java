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

  BIND_ADDRESS("bind_address", null),
  
  TLS_EXCLUDE_PROTOCOLS("tls_exclude_protocols", null),
  TLS_INCLUDE_PROTOCOLS("tls_include_protocols", null),
  
  TLS_EXCLUDE_CIPHER_SUITES("tls_exclude_cipher_suites", null),
  TLS_INCLUDE_CIPHER_SUITES("tls_include_cipher_suites", null);

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
