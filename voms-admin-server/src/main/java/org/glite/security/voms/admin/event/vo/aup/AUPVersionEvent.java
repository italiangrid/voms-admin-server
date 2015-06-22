package org.glite.security.voms.admin.event.vo.aup;

import org.apache.commons.lang.xwork.Validate;
import org.glite.security.voms.admin.event.auditing.NullHelper;
import org.glite.security.voms.admin.persistence.model.AUP;
import org.glite.security.voms.admin.persistence.model.AUPVersion;
import org.glite.security.voms.admin.persistence.model.audit.AuditEvent;

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

    e.addDataPoint("aupVersionCreationTime", version.getCreationTime()
      .toString());

    e.addDataPoint("aupVersionIsActive", version.getActive().toString());

    e.addDataPoint("aupVersionLastForcedReacceptanceTime",
      NullHelper.nullSafeValue(version.getLastForcedReacceptanceTime()));

    e.addDataPoint("aupVersionLastUpdateTime",
      NullHelper.nullSafeValue(version.getLastUpdateTime()));

  }
}
