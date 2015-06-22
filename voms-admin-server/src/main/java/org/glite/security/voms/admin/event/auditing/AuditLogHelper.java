package org.glite.security.voms.admin.event.auditing;

import java.lang.reflect.Constructor;
import java.security.Principal;

import org.glite.security.voms.admin.error.VOMSException;
import org.glite.security.voms.admin.event.SinglePayloadAuditableEvent;
import org.glite.security.voms.admin.persistence.dao.generic.AuditDAO;
import org.glite.security.voms.admin.persistence.dao.generic.DAOFactory;

public class AuditLogHelper {

  private final Principal principal;

  public AuditLogHelper(Principal p) {

    this.principal = p;
  }

  public <A, T extends SinglePayloadAuditableEvent<A>> void saveAuditEvent(
    Class<T> eventClass, A eventArgument) {

    try {

      Constructor<T> c = eventClass.getDeclaredConstructor(eventArgument
        .getClass());

      T event = c.newInstance(eventArgument);

      event.setPrincipal(principal);
      AuditDAO dao = DAOFactory.instance().getAuditDAO();
      dao.makePersistent(event.getAuditEvent());

    } catch (Throwable e) {

      throw new VOMSException("Error creating audit event: " + e.getMessage(),
        e);
    }

  }

}
