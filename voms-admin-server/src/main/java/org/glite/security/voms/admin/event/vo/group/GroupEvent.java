package org.glite.security.voms.admin.event.vo.group;

import org.glite.security.voms.admin.event.vo.VOEvent;
import org.glite.security.voms.admin.persistence.model.VOMSGroup;
import org.glite.security.voms.admin.persistence.model.audit.AuditEvent;

public class GroupEvent extends VOEvent<VOMSGroup> {

  public static final String GROUP_NAME = "groupName";

  public GroupEvent(VOMSGroup g) {

    super(g);
  }

  @Override
  protected void decorateAuditEvent(AuditEvent e) {

    e.addDataPoint(GROUP_NAME, getPayload().getName());

  }
}
