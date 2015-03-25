package org.glite.security.voms.admin.event.vo.attribute;

import org.glite.security.voms.admin.event.vo.VOEvent;
import org.glite.security.voms.admin.persistence.model.VOMSAttributeDescription;
import org.glite.security.voms.admin.persistence.model.audit.AuditEvent;

public class AttributeDescriptionEvent extends
  VOEvent<VOMSAttributeDescription> {

  public AttributeDescriptionEvent(VOMSAttributeDescription ad) {

    super(ad);
  }

  @Override
  protected void decorateAuditEvent(AuditEvent e) {

    e.addDataPoint("attributeClassName", getPayload().getName());
  }
}
