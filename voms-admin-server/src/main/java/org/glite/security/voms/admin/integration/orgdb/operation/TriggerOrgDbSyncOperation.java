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
package org.glite.security.voms.admin.integration.orgdb.operation;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

import org.glite.security.voms.admin.configuration.VOMSConfiguration;
import org.glite.security.voms.admin.core.tasks.VOMSExecutorService;
import org.glite.security.voms.admin.integration.orgdb.OrgDBConfigurator;
import org.glite.security.voms.admin.integration.orgdb.OrgDBMembershipSynchronizationTask;
import org.glite.security.voms.admin.integration.orgdb.OrgDBSyncTaskContainer;
import org.glite.security.voms.admin.operations.VOAdminOperation;

public class TriggerOrgDbSyncOperation implements Callable<Future<?>> {

  private TriggerOrgDbSyncOperation(){
  }

  @Override
  public Future<?> call() throws Exception {
    
    if (!VOMSConfiguration.instance().getRegistrationType().equals(
        OrgDBConfigurator.ORGDB_REGISTRATION_TYPE)){
      return null;
    }
    OrgDBMembershipSynchronizationTask syncTask = OrgDBSyncTaskContainer.INSTANCE.getTask();
    if (syncTask == null){
      return null;
    }
    
    Future<?> handle = VOMSExecutorService.instance().wrapAndSubmit(syncTask);
    return handle;
  }
  
  public static VOAdminOperation<Future<?>> instance() {
    return new VOAdminOperation<>(new TriggerOrgDbSyncOperation());
  }

}
