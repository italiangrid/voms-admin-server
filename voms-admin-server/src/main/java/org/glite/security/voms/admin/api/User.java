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
package org.glite.security.voms.admin.api;

/**
 * Identifies a user in the VOMS database.
 * 
 * @author <a href="mailto:Akos.Frohner@cern.ch">Akos Frohner</a>
 * @author <a href="mailto:lorentey@elte.hu">Karoly Lorentey</a>
 */
public class User {

  /** Empty default constructor. */
  public User() {

  }

  /** Get the distinguished name of this user. */
  public String getDN() {

    return null;
  }

  /** Set the distinguished name of this user. */
  public void setDN(String dn) {

  }

  /**
   * Get the distinguished name of the CA that issued the certificate of this
   * user.
   */
  public String getCA() {

    return null;
  }

  /**
   * Set the distinguished name of the CA that issued the certificate of this
   * user.
   */
  public void setCA(String ca) {

  }

  /** Get the common name of this user. */
  public String getCN() {

    return null;
  }

  /**
   * Set the common name of this user. public void setCN(String cn) { }
   * 
   * /** Get the URL of the user's certificate.
   */
  public String getCertUri() {

    return null;
  }

  /** Set the URL of the user's certificate. */
  public void setCertUri(String certUri) {

  }

  /** Get the email address of the user. */
  public String getMail() {

    return null;
  }

  /** Set the email address of the user. */
  public void setMail(String mail) {

  }

}

// Please do not change this line.
// arch-tag: 1bfa8b99-381f-4208-8d45-e55d5928c8cf

