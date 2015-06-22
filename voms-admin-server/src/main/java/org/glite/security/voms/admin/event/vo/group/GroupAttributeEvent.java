package org.glite.security.voms.admin.event.vo.group;

import org.glite.security.voms.admin.persistence.model.VOMSGroup;
import org.glite.security.voms.admin.persistence.model.VOMSGroupAttribute;
import org.glite.security.voms.admin.persistence.model.audit.AuditEvent;

public class GroupAttributeEvent extends GroupEvent {

  final VOMSGroupAttribute groupAttribute;

  public GroupAttributeEvent(VOMSGroup g, VOMSGroupAttribute groupAttribute) {

    super(g);
    this.groupAttribute = groupAttribute;

  }
  
  @Override
  protected void decorateAuditEvent(AuditEvent e) {
  
    super.decorateAuditEvent(e);
    
    e.addDataPoint("groupAttributeContext", groupAttribute.getContext());
    e.addDataPoint("groupAttributeName", groupAttribute.getName());
    e.addDataPoint("groupAttributeValue", groupAttribute.getValue());
  }

}
