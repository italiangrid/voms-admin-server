package org.glite.security.voms.admin.event.vo.aup;

import org.glite.security.voms.admin.event.vo.VOEvent;
import org.glite.security.voms.admin.persistence.model.AUP;
import org.glite.security.voms.admin.persistence.model.audit.AuditEvent;

public class AUPEvent extends VOEvent<AUP> {

  protected AUPEvent(AUP payload) {

    super(payload);

  }
  
  @Override
  protected void decorateAuditEvent(AuditEvent e) {
  
    AUP aup = getPayload();
    
    e.addDataPoint("aupName", aup.getName());
    e.addDataPoint("aupReacceptancePeriod", aup.getReacceptancePeriod().toString());
    e.addDataPoint("aupActiveVersion", aup.getActiveVersion().getVersion());
    
  }

}
