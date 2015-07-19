package org.glite.security.voms.admin.event.user;

import static org.glite.security.voms.admin.event.auditing.NullHelper.nullSafeValue;

import org.glite.security.voms.admin.event.EventDescription;
import org.glite.security.voms.admin.persistence.model.VOMSUser;
import org.glite.security.voms.admin.persistence.model.audit.AuditEvent;

@EventDescription(message = "updated user '%s %s' membership expiration date",
  params = { "userName", "userSurname" })
public class UserMembershipExpirationDateUpdated extends UserLifecycleEvent {

  public UserMembershipExpirationDateUpdated(VOMSUser payload) {

    super(payload);
  }

  @Override
  protected void decorateAuditEvent(AuditEvent e) {

    super.decorateAuditEvent(e);
    e.addDataPoint("newMembershipExpirationDate", nullSafeValue(getPayload()
      .getEndTime().toString()));
  }

}
