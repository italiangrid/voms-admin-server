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
package it.infn.cnaf.voms.aa;

import org.glite.security.voms.admin.persistence.model.VOMSGroup;
import org.glite.security.voms.admin.persistence.model.VOMSMapping;
import org.glite.security.voms.admin.util.PathNamingScheme;

/**
 * 
 * @author Andrea Ceccanti
 *
 */
public class VOMSFQAN {

  String FQAN;

  private VOMSFQAN(String fqan) {

    FQAN = fqan;
  }

  public String getFQAN() {

    return FQAN;
  }

  public void setFQAN(String fqan) {

    FQAN = fqan;
  }

  public String toString() {

    return FQAN;
  }

  public boolean isGroup() {

    return PathNamingScheme.isGroup(FQAN);

  }

  public boolean isRole() {

    return PathNamingScheme.isQualifiedRole(FQAN);
  }

  public String getGroupPartAsString() {

    return PathNamingScheme.getGroupName(FQAN);
  }

  public VOMSFQAN getGroupPartAsVOMSFQAN() {

    return VOMSFQAN.fromString(getGroupPartAsString());
  }

  public static VOMSFQAN fromModel(VOMSMapping m) {

    return new VOMSFQAN(m.getFQAN());

  }

  public static VOMSFQAN fromModel(VOMSGroup m) {

    return new VOMSFQAN(m.getName());

  }

  public static VOMSFQAN fromString(String fqan) {

    assert (PathNamingScheme.isGroup(fqan) || PathNamingScheme
      .isQualifiedRole(fqan)) : "Illegal FQAN passed as argument: " + fqan;

    return new VOMSFQAN(fqan);

  }

  public boolean equals(Object obj) {

    if (FQAN == null)
      return super.equals(obj);

    VOMSFQAN that = (VOMSFQAN) obj;

    return this.FQAN.equals(that.FQAN);

  }

  public int hashCode() {

    if (FQAN == null)
      return super.hashCode();

    return FQAN.hashCode();
  }

}
