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
package org.glite.security.voms.admin.configuration;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.glite.security.voms.admin.error.VOMSException;


public class ServiceID {

  private ServiceID() {
  }

  public static String getServiceID(){
    try {
      String hostname = InetAddress.getLocalHost().getHostName();
      return String.format("%s:8443", hostname);
    } catch (UnknownHostException e) {
      throw new VOMSException("Error resolving local hostname", e);
    }
  }
}
