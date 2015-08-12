package org.glite.security.voms.admin.persistence.model.audit;

import java.util.Comparator;

public enum AuditEventDataNameComparator implements Comparator<AuditEventData> {

  INSTANCE;

  @Override
  public int compare(AuditEventData arg0, AuditEventData arg1) {

    return arg0.getName().compareTo(arg1.getName());

  }

}
