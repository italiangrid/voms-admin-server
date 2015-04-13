package org.glite.security.voms.admin.event.user.attribute;

import org.apache.commons.lang.Validate;
import org.glite.security.voms.admin.event.EventCategory;
import org.glite.security.voms.admin.event.user.UserEvent;
import org.glite.security.voms.admin.persistence.model.VOMSUser;
import org.glite.security.voms.admin.persistence.model.VOMSUserAttribute;
import org.glite.security.voms.admin.persistence.model.audit.AuditEvent;

public class UserAttributeEvent extends UserEvent {

  final VOMSUserAttribute attribute;

  public UserAttributeEvent(VOMSUser payload,
    VOMSUserAttribute attribute) {

    super(EventCategory.UserAttributeEvent, payload);
    Validate.notNull(attribute);
    this.attribute = attribute;
  }

  @Override
  protected void decorateAuditEvent(AuditEvent e) {

    super.decorateAuditEvent(e);

    e.addDataPoint("attributeName", attribute.getName());
    e.addDataPoint("attributeValue", attribute.getValue());
  }

  public VOMSUserAttribute getAttribute() {

    return attribute;
  }
}
