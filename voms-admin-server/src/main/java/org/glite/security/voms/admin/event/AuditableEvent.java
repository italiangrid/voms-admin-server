/**
 * Copyright (c) Istituto Nazionale di Fisica Nucleare (INFN). 2006-2015
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
package org.glite.security.voms.admin.event;

import java.security.Principal;
import java.util.Date;

import org.apache.commons.lang.Validate;
import org.glite.security.voms.admin.persistence.model.audit.AuditEvent;

public abstract class AuditableEvent extends AbstractEvent {

  private volatile AuditEvent auditEvent;

  private Principal principal;

  protected AuditableEvent(EventCategory type) {

    super(type);

  }

  private AuditEvent buildAuditEvent() {

    if (auditEvent == null) {

      Validate.notNull(principal);
      Validate.notEmpty(principal.getName());

      auditEvent = new AuditEvent();
      auditEvent.setPrincipal(principal.getName());

      auditEvent.setTimestamp(new Date());
      auditEvent.setType(getClass().getName());

      decorateAuditEvent(auditEvent);
    }

    return auditEvent;
  }

  protected abstract void decorateAuditEvent(AuditEvent e);

  public AuditEvent getAuditEvent() {

    buildAuditEvent();

    return auditEvent;
  }

  public Principal getPrincipal() {

    return principal;
  }

  public void setPrincipal(Principal principal) {

    this.principal = principal;
  }

}
