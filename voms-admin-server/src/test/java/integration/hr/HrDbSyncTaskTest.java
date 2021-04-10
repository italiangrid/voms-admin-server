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
package integration.hr;

import static org.junit.Assert.fail;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.glite.security.voms.admin.configuration.VOMSConfiguration;
import org.glite.security.voms.admin.integration.cern.HrDbApiService;
import org.glite.security.voms.admin.integration.cern.HrDbError;
import org.glite.security.voms.admin.integration.cern.HrDbProperties;
import org.glite.security.voms.admin.integration.cern.HrDbSyncTask;
import org.glite.security.voms.admin.integration.cern.HrDefaultHandler;
import org.glite.security.voms.admin.integration.cern.dto.VOPersonDTO;
import org.glite.security.voms.admin.persistence.dao.VOMSUserDAO;
import org.glite.security.voms.admin.persistence.model.VOMSUser;
import org.hibernate.ScrollableResults;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;



public class HrDbSyncTaskTest {
  
  @Spy
  HrDbProperties config;
  
  @Mock
  HrDbApiService hrDbApi;
  
  @Mock
  VOMSConfiguration vomsConfig;
  
  @Mock
  VOMSUserDAO dao;

  @Mock
  ScrollableResults results;
  
  @Spy
  VOMSUser user1;
  
  @Spy
  VOMSUser user2;
  
  @Spy
  VOPersonDTO person1;
  
  @Mock
  HrDefaultHandler handler;
  
  HrDbSyncTask task;
  
  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    
    task = new HrDbSyncTask(config, hrDbApi, dao, vomsConfig);
    
    task.setMissingRecordHandler(handler);
    task.setSyncHandler(handler);
    
    config.setExperimentName("experiment");
    
    when(dao.findAllWithCursor()).thenReturn(results);
    when(results.next()).thenReturn(true, true, false);
    when(results.get(0)).thenReturn(user1, user2);
    
    user1.setId(1L);
    user1.setName("test");
    user1.setSurname("user");
    user1.setEmailAddress("test.user@example");
    
    user2.setId(2L);
    user2.setName("tost");
    user2.setSurname("aser");
    user2.setEmailAddress("tost.aser@example");
    
    when(hrDbApi.lookupVomsUser(user1)).thenReturn(Optional.of(person1));
    when(hrDbApi.lookupVomsUser(user2)).thenReturn(Optional.empty());
  }
  
  
  @Test
  public void testSync() {
    
    task.run();
    
    verify(handler).synchronizeMembershipInformation(user1, person1);
    verify(handler).handleMissingHrRecord(user2);
    
    
  }
  
  @Test
  public void testApiError() {
    
    when(hrDbApi.lookupVomsUser(user1)).thenThrow(new HrDbError("Sync error"));
    
    task.run();
    
    verify(handler).handleMissingHrRecord(user2);
    
  }
  
  @Test
  public void testOtherError() {
    
    when(hrDbApi.lookupVomsUser(user1)).thenThrow(new NullPointerException());
    
    try {
      task.run();
      fail("Expected exception caught!");
    } catch (NullPointerException e) {
      
    }
    
    verify(handler, Mockito.times(0)).synchronizeMembershipInformation(user1, person1);
    verify(handler, Mockito.times(0)).handleMissingHrRecord(user2);
    
  }

}
