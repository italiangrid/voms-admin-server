/**
 * Copyright (c) Istituto Nazionale di Fisica Nucleare (INFN). 2006-2016
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
package org.glite.security.voms.admin.util.migration;

public interface MigrateVoConstants {
  String DEFAULT_SSL_CERT_FILE = "/etc/grid-security/hostcert.pem";
  String DEFAULT_SSL_KEY = "/etc/grid-security/hostkey.pem";
  String AXIS_SOCKET_FACTORY_PROPERTY = "axis.socketSecureFactory";
  
  String ORIGIN_SERVER_ENV ="VA_MIGRATE_ORIGIN_SERVER";
  String ORIGIN_VO_ENV ="VA_MIGRATE_ORIGIN_VO";
  
  String DESTINATION_SERVER_ENV ="VA_MIGRATE_DESTINATION_SERVER";
  String DESTINATION_VO_ENV ="VA_MIGRATE_DESTINATION_VO";
  String X509_USER_PROXY_ENV ="X509_USER_PROXY";
}
