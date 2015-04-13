package org.glite.security.voms.admin.operations;

import java.security.Principal;

import org.glite.security.voms.admin.event.auditing.PrincipalProvider;

public class DefaultPrincipalProvider implements PrincipalProvider {

  public DefaultPrincipalProvider() {

  }

  @Override
  public Principal getCurrentPrincipal() {

    CurrentAdmin admin = CurrentAdmin.instance();

    return new CurrentAdminPrincipal(admin);

  }

}
