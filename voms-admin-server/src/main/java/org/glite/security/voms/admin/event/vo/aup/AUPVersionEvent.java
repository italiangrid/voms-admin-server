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
package org.glite.security.voms.admin.event.vo.aup;


import org.apache.commons.lang.Validate;
import org.glite.security.voms.admin.event.MainEventDataPoints;
import org.glite.security.voms.admin.persistence.model.AUP;
import org.glite.security.voms.admin.persistence.model.AUPVersion;
import org.glite.security.voms.admin.persistence.model.audit.AuditEvent;

@MainEventDataPoints({"aupName", "aupVersion", "aupVersionURL", "aupVersionIsActive"})
public class AUPVersionEvent extends AUPEvent {

  final AUPVersion version;

  public AUPVersionEvent(AUP payload, AUPVersion v) {

    super(payload);
    Validate.notNull(v);
    this.version = v;
  }

  public AUPVersion getVersion() {

    return version;
  }

  @Override
  protected void decorateAuditEvent(AuditEvent e) {

    super.decorateAuditEvent(e);
    e.addDataPoint("aupVersion", version.getVersion());

    e.addDataPoint("aupVersionURL", version.getUrl());

    e.addDataPoint("aupVersionCreationTime", version.getCreationTime().toString());

    e.addDataPoint("aupVersionIsActive", version.getActive().toString());

    e.addDataPoint("aupVersionLastForcedReacceptanceTime", version.getLastForcedReacceptanceTime());

    e.addDataPoint("aupVersionLastUpdateTime", version.getLastUpdateTime());

  }
}
