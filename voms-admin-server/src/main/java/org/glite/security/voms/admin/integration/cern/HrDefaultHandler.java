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
package org.glite.security.voms.admin.integration.cern;

import java.time.Clock;
import java.time.Instant;
import java.util.Date;
import java.util.Optional;

import org.glite.security.voms.admin.core.validation.ValidationManager;
import org.glite.security.voms.admin.integration.cern.dto.ParticipationDTO;
import org.glite.security.voms.admin.integration.cern.dto.VOPersonDTO;
import org.glite.security.voms.admin.integration.cern.strategies.ExpiredParticipationStrategy;
import org.glite.security.voms.admin.integration.cern.strategies.MembershipSynchronizationStrategy;
import org.glite.security.voms.admin.integration.cern.strategies.MissingMembershipRecordStrategy;
import org.glite.security.voms.admin.persistence.model.VOMSUser;
import org.glite.security.voms.admin.persistence.model.VOMSUser.SuspensionReason;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HrDefaultHandler implements ExpiredParticipationStrategy,
    MembershipSynchronizationStrategy, MissingMembershipRecordStrategy {

  public static final Logger LOG = LoggerFactory.getLogger(HrDefaultHandler.class);

  private final Clock clock;
  private final ValidationManager manager;
  private final HrDbProperties config;

  public HrDefaultHandler(Clock clock, HrDbProperties hrConfig,
      ValidationManager validationManager) {
    this.clock = clock;
    this.manager = validationManager;
    this.config = hrConfig;
  }

  @Override
  public void handleMissingHrRecord(VOMSUser user) {
    LOG.info("No HR record found for user: {}", user.getShortName());

    SuspensionReason reason = SuspensionReason.HR_DB_VALIDATION;
    reason.setMessage("OrgDB: No record found");

    manager.suspendUser(user, reason);
  }

  protected void restoreUserIfNeeded(VOMSUser u) {
    if (u.isSuspended()) {

      if (u.getSuspensionReasonCode() == SuspensionReason.MEMBERSHIP_EXPIRATION
          || u.getSuspensionReason().startsWith("OrgDB: ")) {
        manager.restoreUser(u);
      }
    }

  }


  @Override
  public void synchronizeMembershipInformation(VOMSUser user, VOPersonDTO voPerson) {

    LOG.info("Syncing user {} against record {}", user.getShortName(), voPerson.getId());
    Instant now = clock.instant();

    user.setOrgDbId(voPerson.getId());

    user.setName(voPerson.getFirstName());
    user.setSurname(voPerson.getName());

    String orgDbEmailAddress =
        Optional.ofNullable(voPerson.getPhysicalEmail()).orElse(voPerson.getEmail());
    
    user.setEmailAddress(orgDbEmailAddress.toLowerCase());

    Optional<ParticipationDTO> participation =
        voPerson.findValidParticipationsForExperiment(now, config.getExperimentName());

    if (participation.isPresent()) {
      user.setEndTime(participation.get().getEndDate());
      restoreUserIfNeeded(user);
    } else {
      handleExpiredParticipation(user, voPerson);
    }
  }

  @Override
  public void handleExpiredParticipation(VOMSUser u, VOPersonDTO voPerson) {
    Instant now = clock.instant();

    u.setEndTime(Date.from(now));

    LOG.info(
        "User {} HR experiment participation is no longer valid, setting VOMS membership end date to current time, i.e.: {}",
        u.getShortName(), now);
  }

}
