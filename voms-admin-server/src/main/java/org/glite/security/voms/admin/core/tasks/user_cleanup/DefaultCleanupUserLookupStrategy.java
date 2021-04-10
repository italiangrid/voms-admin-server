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
package org.glite.security.voms.admin.core.tasks.user_cleanup;

import static java.util.Objects.isNull;
import static org.glite.security.voms.admin.configuration.VOMSConfigurationConstants.EXPIRED_USER_CLEANUP_TASK_CLEANUP_AFTER_DAYS;

import org.glite.security.voms.admin.configuration.VOMSConfiguration;
import org.glite.security.voms.admin.persistence.dao.VOMSUserDAO;
import org.hibernate.ScrollableResults;

public class DefaultCleanupUserLookupStrategy implements CleanupUserLookupStrategy {

  VOMSConfiguration config;
  VOMSUserDAO dao;

  public DefaultCleanupUserLookupStrategy(VOMSConfiguration config) {
    this.config = config;
  }

  @Override
  public ScrollableResults findUsersEligibleForCleanup() {

    if (isNull(dao)) {
      dao = VOMSUserDAO.instance();
    }
    int numberOfDays = config.getInt(EXPIRED_USER_CLEANUP_TASK_CLEANUP_AFTER_DAYS, 30);

    return dao.findUsersExpiredSinceDays(numberOfDays);
  }

  public void setDao(VOMSUserDAO dao) {
    this.dao = dao;
  }
}
