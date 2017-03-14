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
package it.infn.cnaf.voms.aa;

/**
 * 
 * @author Andrea Ceccanti
 * @author Valerio Venturi
 *
 */
public class VOMSUser {

  String dn;
  String ca;

  public String getCa() {

    return ca;
  }

  public void setCa(String ca) {

    this.ca = ca;
  }

  public String getDn() {

    return dn;
  }

  public void setDn(String dn) {

    this.dn = dn;
  }

  public static VOMSUser fromModel(
    org.glite.security.voms.admin.persistence.model.VOMSUser user) {

    VOMSUser u = new VOMSUser();

    u.setDn(user.getDn());
    u.setCa(user.getCa().getDn());

    return u;
  }

}
