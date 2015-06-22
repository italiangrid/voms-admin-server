package org.glite.security.voms.admin.event.vo.admin;

import org.glite.security.voms.admin.event.vo.VOEvent;
import org.glite.security.voms.admin.persistence.model.VOMSAdmin;
import org.glite.security.voms.admin.persistence.model.audit.AuditEvent;


public class AdminEvent extends VOEvent<VOMSAdmin>{
  
  public AdminEvent(VOMSAdmin admin) {
    super(admin);
  }

  @Override
  protected void decorateAuditEvent(AuditEvent e) {
    e.addDataPoint("dn", getPayload().getDn());
    e.addDataPoint("ca", getPayload().getCa().getSubjectString());
  }

}
