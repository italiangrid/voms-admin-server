/**
 * Copyright (c) Members of the EGEE Collaboration. 2006-2009.
 * See http://www.eu-egee.org/partners/ for details on the copyright holders.
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
 *
 * Authors:
 * 	Andrea Ceccanti (INFN)
 */

package org.glite.security.voms.admin.core.validation.strategies.impl;

import java.util.List;

import org.glite.security.voms.admin.configuration.VOMSConfiguration;
import org.glite.security.voms.admin.configuration.VOMSConfigurationConstants;
import org.glite.security.voms.admin.core.validation.AbstractMembershipCheckBehaviour;
import org.glite.security.voms.admin.core.validation.strategies.AUPFailingMembersLookupStrategy;
import org.glite.security.voms.admin.core.validation.strategies.ExpiredMembersLookupStrategy;
import org.glite.security.voms.admin.core.validation.strategies.ExpiringMembersLookupStrategy;
import org.glite.security.voms.admin.core.validation.strategies.HandleAUPFailingMembersStrategy;
import org.glite.security.voms.admin.core.validation.strategies.HandleExpiredMembersStrategy;
import org.glite.security.voms.admin.core.validation.strategies.HandleExpiringMembersStrategy;
import org.glite.security.voms.admin.persistence.model.VOMSUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultMembershipCheckBehaviour extends
  AbstractMembershipCheckBehaviour {

  public static final Logger log = LoggerFactory
    .getLogger(DefaultMembershipCheckBehaviour.class);

  protected final AUPFailingMembersLookupStrategy aupFMLookupStrategy;
  protected final ExpiredMembersLookupStrategy expiredMembersLookupStrategy;
  protected final ExpiringMembersLookupStrategy expiringMembersLookupStrategy;

  protected final HandleAUPFailingMembersStrategy aupFailingMembersStrategy;
  protected final HandleExpiredMembersStrategy expiredMembersStrategy;
  protected final HandleExpiringMembersStrategy expiringMembersStrategy;

  protected void validateMembershipCheckConfiguration() {

    VOMSConfiguration conf = VOMSConfiguration.instance();

    boolean disableMembershipEndTime = conf.getBoolean(
      VOMSConfigurationConstants.DISABLE_MEMBERSHIP_END_TIME, false);
    boolean preserveExpiredMembers = conf.getBoolean(
      VOMSConfigurationConstants.PRESERVE_EXPIRED_MEMBERS, false);

    if (disableMembershipEndTime && preserveExpiredMembers) {
      log
        .error(
          "The {} and {} configuration properties cannot be true at the same time",
          new String[] {
            VOMSConfigurationConstants.DISABLE_MEMBERSHIP_END_TIME,
            VOMSConfigurationConstants.PRESERVE_EXPIRED_MEMBERS });

      log.warn("Setting {} to false",
        VOMSConfigurationConstants.DISABLE_MEMBERSHIP_END_TIME);
      conf.setProperty(VOMSConfigurationConstants.DISABLE_MEMBERSHIP_END_TIME,
        false);
    }

  }

  public DefaultMembershipCheckBehaviour() {

    validateMembershipCheckConfiguration();

    VOMSConfiguration conf = VOMSConfiguration.instance();

    // AUP handling strategy is not configurable
    aupFMLookupStrategy = new DefaultAUPFailingMembersLookupStrategy();
    aupFailingMembersStrategy = new SuspendAUPFailingMembersStrategy();

    boolean disableMembershipEndTime = conf.getBoolean(
      VOMSConfigurationConstants.DISABLE_MEMBERSHIP_END_TIME, false);
    boolean preserveExpiredMembers = conf.getBoolean(
      VOMSConfigurationConstants.PRESERVE_EXPIRED_MEMBERS, false);

    int notificationInterval = VOMSConfiguration.instance().getInt(
      VOMSConfigurationConstants.NOTIFICATION_WARNING_RESEND_PERIOD, 1);

    if (disableMembershipEndTime) {

      IgnoreMembershipEndTimeStrategy s = new IgnoreMembershipEndTimeStrategy();

      log
        .warn("The membership end time will be IGNORED by the VOMS membership check behaviour as requested by configuration.");

      expiredMembersLookupStrategy = s;
      expiredMembersStrategy = s;
      expiringMembersStrategy = s;
      expiringMembersLookupStrategy = s;

    } else if (preserveExpiredMembers) {

      log
        .warn("Expired members will NOT be suspended as requested. Administrators will be notified of expired members via email.");
      expiredMembersStrategy = new PreserveExpiredMembersStrategy(
        notificationInterval);

      expiredMembersLookupStrategy = new DefaultExpiredMembersLookupStrategy();
      expiringMembersLookupStrategy = new DefaultExpiringMembersLookupStrategy();

      expiringMembersStrategy = new SendWarningAboutExpiringMembersStrategy();

    } else {

      expiredMembersLookupStrategy = new DefaultExpiredMembersLookupStrategy();
      expiringMembersLookupStrategy = new DefaultExpiringMembersLookupStrategy();

      long gracePeriodInDays = VOMSConfiguration.instance().getLong(
        VOMSConfigurationConstants.MEMBERSHIP_EXPIRATION_GRACE_PERIOD, 7L);
      if (gracePeriodInDays <= 0)
        gracePeriodInDays = 0;

      log.info(
        "Expired users will be suspended after a grace period of {} days.",
        gracePeriodInDays);

      expiredMembersStrategy = new GracePeriodExpiredMembersStrategy(
        gracePeriodInDays, notificationInterval);

      expiringMembersStrategy = new SendWarningAboutExpiringMembersStrategy();
    }

  }

  public List<VOMSUser> findAUPFailingMembers() {

    return aupFMLookupStrategy.findAUPFailingMembers();

  }

  public List<VOMSUser> findExpiredMembers() {

    return expiredMembersLookupStrategy.findExpiredMembers();

  }

  public void handleExpiredMembers(List<VOMSUser> expiredMembers) {

    expiredMembersStrategy.handleExpiredMembers(expiredMembers);

  }

  public List<VOMSUser> findExpiringMembers() {

    return expiringMembersLookupStrategy.findExpiringMembers();
  }

  public void handleMembersAboutToExpire(List<VOMSUser> expiringMembers) {

    expiringMembersStrategy.handleMembersAboutToExpire(expiringMembers);
  }

  public void handleAUPFailingMembers(List<VOMSUser> aupFailingMembers) {

    aupFailingMembersStrategy.handleAUPFailingMembers(aupFailingMembers);

  }

}
