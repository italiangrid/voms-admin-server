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
