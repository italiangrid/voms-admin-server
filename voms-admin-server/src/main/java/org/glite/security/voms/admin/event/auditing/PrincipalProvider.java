package org.glite.security.voms.admin.event.auditing;

import java.security.Principal;

public interface PrincipalProvider {

  public Principal getCurrentPrincipal();
}
