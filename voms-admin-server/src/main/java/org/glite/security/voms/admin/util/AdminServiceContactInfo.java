/**
 * Copyright (c) Members of the EGEE Collaboration. 2006-2009.
 * See http://www.eu-egee.org/partners/ for details on the copyright holders.
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
 *
 * Authors:
 * 	Andrea Ceccanti (INFN)
 */

package org.glite.security.voms.admin.util;

public class AdminServiceContactInfo implements
  Comparable<AdminServiceContactInfo> {

  String voName;
  String host;
  int port;

  /**
   * @return the voName
   */
  public synchronized String getVoName() {

    return voName;
  }

  /**
   * @param voName
   *          the voName to set
   */
  public synchronized void setVoName(String voName) {

    this.voName = voName;
  }

  /**
   * @return the host
   */
  public synchronized String getHost() {

    return host;
  }

  /**
   * @param host
   *          the host to set
   */
  public synchronized void setHost(String host) {

    this.host = host;
  }

  /**
   * @return the port
   */
  public synchronized int getPort() {

    return port;
  }

  public String getURL() {

    return String.format("https://%s:%d/voms/%s", host, port, voName);
  }

  public String getBaseURL() {

    return String.format("https://%s:%d", host, port);
  }

  /**
   * @param port
   *          the port to set
   */
  public synchronized void setPort(int port) {

    this.port = port;
  }

  public AdminServiceContactInfo(String vo, String host, int port) {

    this.voName = vo;
    this.host = host;
    this.port = port;
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {

    return "AdminServiceContactInfo [voName=" + voName + ", host=" + host
      + ", port=" + port + "]";
  }

  @Override
  public int compareTo(AdminServiceContactInfo o) {

    return this.getVoName().compareTo(o.getVoName());
  }
}
