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

import java.util.Optional;

import org.glite.security.voms.admin.configuration.VOMSConfiguration;
import org.glite.security.voms.admin.integration.cern.dto.VOPersonDTO;
import org.glite.security.voms.admin.integration.cern.strategies.MembershipSynchronizationStrategy;
import org.glite.security.voms.admin.integration.cern.strategies.MissingMembershipRecordStrategy;
import org.glite.security.voms.admin.persistence.dao.VOMSUserDAO;
import org.glite.security.voms.admin.persistence.model.VOMSUser;
import org.hibernate.ScrollableResults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HrDbSyncTask implements Runnable {

  public static final Logger LOG = LoggerFactory.getLogger(HrDbSyncTask.class);

  final HrDbProperties hrConfig;
  final HrDbApiService hrDbApi;
  final VOMSConfiguration vomsConfig;
  final VOMSUserDAO dao;

  private MembershipSynchronizationStrategy syncHandler = (u, p) -> {
  };

  private MissingMembershipRecordStrategy missingRecordHandler = u -> {
  };

  public HrDbSyncTask(HrDbProperties properties, HrDbApiService api, VOMSUserDAO dao,
      VOMSConfiguration vomsConfiguration) {
    this.hrConfig = properties;
    this.vomsConfig = vomsConfiguration;
    this.dao = dao;
    this.hrDbApi = api;
  }


  @Override
  public void run() {
    ScrollableResults userCursor = dao.findAllWithCursor();

    while (userCursor.next()) {
      VOMSUser user = (VOMSUser) userCursor.get(0);

      try {

        Optional<VOPersonDTO> voPerson = hrDbApi.lookupVomsUser(user);

        if (voPerson.isPresent()) {
          syncHandler.synchronizeMembershipInformation(user, voPerson.get());
        } else {
          missingRecordHandler.handleMissingHrRecord(user);
        }
        
      } catch (HrDbError apiError) {
        LOG.error("Error querying HR Db API for user {}: {}", user.getShortName(),
            apiError.getMessage());

        if (LOG.isDebugEnabled()) {
          LOG.error(apiError.getMessage(), apiError);
        }
      }
    }

  }

  public void setMissingRecordHandler(MissingMembershipRecordStrategy missingRecordHandler) {
    this.missingRecordHandler = missingRecordHandler;
  }

  public void setSyncHandler(MembershipSynchronizationStrategy syncHandler) {
    this.syncHandler = syncHandler;
  }

}
