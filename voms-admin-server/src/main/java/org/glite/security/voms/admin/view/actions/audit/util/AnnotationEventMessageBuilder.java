/**
 * Copyright (c) Istituto Nazionale di Fisica Nucleare (INFN). 2006-2016
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
