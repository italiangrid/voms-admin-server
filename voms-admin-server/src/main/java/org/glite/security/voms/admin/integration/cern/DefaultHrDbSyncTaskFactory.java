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

import org.glite.security.voms.admin.configuration.VOMSConfiguration;
import org.glite.security.voms.admin.core.tasks.DatabaseTransactionTaskWrapper;
import org.glite.security.voms.admin.core.validation.ValidationManager;
import org.glite.security.voms.admin.persistence.dao.VOMSUserDAO;

public class DefaultHrDbSyncTaskFactory implements HrDbSyncTaskFactory {

  private final Clock clock;
  private final ValidationManager validationManager;

  public DefaultHrDbSyncTaskFactory() {
    clock = Clock.systemDefaultZone();
    validationManager = ValidationManager.instance();
  }

  public DefaultHrDbSyncTaskFactory(Clock c, ValidationManager vm) {
    clock = c;
    validationManager = vm;
  }

  @Override
  public Runnable buildSyncTask(HrDbProperties properties, HrDbApiService api, VOMSUserDAO dao,
      VOMSConfiguration config) {

    HrDefaultHandler handler = new HrDefaultHandler(clock, properties, validationManager);
    HrDbSyncTask syncTask = new HrDbSyncTask(properties, api, dao, config);

    syncTask.setMissingRecordHandler(handler);
    syncTask.setSyncHandler(handler);

    return new DatabaseTransactionTaskWrapper(syncTask, true, true,
        properties.getMembershipCheck().getPeriodInSeconds());
  }
}
