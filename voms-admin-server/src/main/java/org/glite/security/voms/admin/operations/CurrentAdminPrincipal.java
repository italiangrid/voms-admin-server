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
package org.glite.security.voms.admin.operations;

import java.security.Principal;

import org.glite.security.voms.admin.core.VOMSServiceConstants;

public class CurrentAdminPrincipal implements Principal {

  public static final CurrentAdminPrincipal LOCAL_DB_PRINCIPAL = 
    new CurrentAdminPrincipal(VOMSServiceConstants.INTERNAL_ADMIN);

  final String name;

  public CurrentAdminPrincipal(CurrentAdmin admin) {

    this.name = admin.getName();
  }

  public CurrentAdminPrincipal(String name) {

    this.name = name;
  }

  @Override
  public String getName() {

    return name;
  }

  @Override
  public int hashCode() {

    final int prime = 31;
    int result = 1;
    result = prime * result + ((name == null) ? 0 : name.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {

    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;

    CurrentAdminPrincipal other = (CurrentAdminPrincipal) obj;
    if (name == null) {
      if (other.name != null)
        return false;
    } else if (!name.equals(other.name))
      return false;
    return true;
  }

}
