package org.glite.security.voms.admin.view.actions.audit.util;

import org.glite.security.voms.admin.event.EventDescription;
import org.glite.security.voms.admin.persistence.model.audit.AuditEvent;

public class AnnotationEventMessageBuilder implements EventDescriptor {

  public AnnotationEventMessageBuilder() {

  }

  public String buildParametrizedMessage(EventDescription ed, AuditEvent event) {

    String[] paramValues = new String[ed.params().length];
    for (int i = 0; i < ed.params().length; i++) {
      String paramValue = event.getDataPoint(ed.params()[i]);
      if (paramValue == null) {
        throw new NullPointerException(
          "Check your event annotations dude! paramValue cannot be null!");
      }
      paramValues[i] = paramValue;
    }

    return String.format(ed.message(), (Object[]) paramValues);
  }

  @Override
  public String buildEventDescription(AuditEvent event) {

    try {

      Class<?> eventClass = Class.forName(event.getType());

      if (!eventClass.isAnnotationPresent(EventDescription.class)) {
        return null;
      }

      EventDescription ed = eventClass.getAnnotation(EventDescription.class);

      if (ed.params().length > 0) {
        return buildParametrizedMessage(ed, event);
      }

      return ed.message();

    } catch (ClassNotFoundException e) {
      return null;
    }
  }

}
