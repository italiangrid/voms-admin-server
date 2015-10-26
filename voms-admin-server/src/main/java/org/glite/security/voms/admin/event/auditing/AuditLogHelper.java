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
