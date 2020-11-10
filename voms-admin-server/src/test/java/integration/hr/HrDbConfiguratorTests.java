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

import static java.time.Clock.fixed;
import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.time.ZoneId;
import java.util.concurrent.TimeUnit;

import org.glite.security.voms.admin.configuration.VOMSConfiguration;
import org.glite.security.voms.admin.core.tasks.DatabaseTransactionTaskWrapper;
import org.glite.security.voms.admin.core.tasks.VOMSExecutorService;
import org.glite.security.voms.admin.core.validation.ValidationManager;
import org.glite.security.voms.admin.integration.VOMSPluginConfigurationException;
import org.glite.security.voms.admin.integration.cern.HrDbApiService;
import org.glite.security.voms.admin.integration.cern.HrDbApiServiceFactory;
import org.glite.security.voms.admin.integration.cern.HrDbConfigurator;
import org.glite.security.voms.admin.integration.cern.HrDbProperties;
import org.glite.security.voms.admin.integration.cern.HrDbRequestValidator;
import org.glite.security.voms.admin.integration.cern.HrDbRequestValidatorFactory;
import org.glite.security.voms.admin.integration.cern.HrDbSyncTask;
import org.glite.security.voms.admin.integration.cern.HrDbSyncTaskFactory;
import org.glite.security.voms.admin.persistence.dao.VOMSUserDAO;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class HrDbConfiguratorTests extends HrDbTestSupport {

  @Mock
  VOMSConfiguration vomsConfig;

  @Mock
  ValidationManager manager;

  @Mock
  VOMSExecutorService executorService;

  @Mock
  VOMSUserDAO dao;

  @Mock
  HrDbApiServiceFactory apiFactory;

  @Mock
  HrDbApiService api;

  @Mock
  HrDbSyncTaskFactory syncTaskFactory;

  @Mock
  HrDbSyncTask syncTask;

  @Mock
  HrDbRequestValidatorFactory validatorFactory;

  @Mock
  HrDbRequestValidator validator;

  HrDbConfigurator configurator;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    configurator = new HrDbConfigurator(vomsConfig);
    configurator.setPluginName("orgdb");
    configurator.setApiServiceFactory(apiFactory);
    configurator.setExecutorService(executorService);
    configurator.setRequestValidatorFactory(validatorFactory);
    configurator.setValidationManager(manager);
    configurator.setSyncTaskFactory(syncTaskFactory);
    configurator.setUserDao(dao);
    configurator.setClock(CLOCK);

    when(vomsConfig.getConfigurationDirectoryPath()).thenReturn("src/test/resources/cern/config");
    when(vomsConfig.getExternalValidatorProperty(eq("orgdb"), anyString(), anyString()))
      .thenReturn("src/test/resources/cern/config/orgdb.properties");
    when(apiFactory.newHrDbApiService(any())).thenReturn(api);
    when(validatorFactory.newHrDbRequestValidator(any(), any())).thenReturn(validator);
    when(syncTaskFactory.buildSyncTask(any(), any(), any(), any())).thenReturn(syncTask);
  }

  @Test
  public void testConfigurationFileIsFoundAndParsed() throws VOMSPluginConfigurationException {
    configurator.configure();
    verifyZeroInteractions(executorService);
    verifyZeroInteractions(syncTaskFactory);
    verify(manager).setRequestValidationContext(Mockito.eq(validator));
  }

  @Test(expected = VOMSPluginConfigurationException.class)
  public void testConfigurationNotFoundPrintsAppropriateErrorMessage()
      throws VOMSPluginConfigurationException {
    when(vomsConfig.getExternalValidatorProperty(eq("orgdb"), anyString(), anyString()))
      .thenReturn("does/not/exist/orgdb.properties");
    try {
      configurator.configure();
    } catch (VOMSPluginConfigurationException e) {
      assertThat(e.getMessage(),
          containsString("'does/not/exist/orgdb.properties' for plugin 'orgdb' does not exist!"));
      throw e;
    }
  }

  @Test
  public void testMembershipCheckTaskScheduling() throws VOMSPluginConfigurationException {
    HrDbProperties props = new HrDbProperties();

    configurator.setHrConfig(props);
    configurator.configure();

    verify(executorService).scheduleAtFixedRate(isA(DatabaseTransactionTaskWrapper.class),
        Mockito.anyLong(), Mockito.eq(props.getMembershipCheck().getPeriodInSeconds()),
        Mockito.eq(TimeUnit.SECONDS));

    verify(executorService).schedule(Mockito.isA(DatabaseTransactionTaskWrapper.class),
        Mockito.eq(300L), Mockito.eq(TimeUnit.SECONDS));
  }

  @Test
  public void testNoStartupTaskScheduling() throws VOMSPluginConfigurationException {
    HrDbProperties props = new HrDbProperties();

    props.getMembershipCheck().setRunAtStartup(false);

    configurator.setHrConfig(props);
    configurator.configure();

    verify(executorService).scheduleAtFixedRate(isA(DatabaseTransactionTaskWrapper.class), anyLong(),
        Mockito.eq(props.getMembershipCheck().getPeriodInSeconds()), eq(TimeUnit.SECONDS));

    verify(executorService, never()).schedule(isA(DatabaseTransactionTaskWrapper.class), anyLong(), any());
  }

  @Test
  public void testStartupTaskSchedulingCanceled() throws VOMSPluginConfigurationException {
    HrDbProperties props = new HrDbProperties();

    props.getMembershipCheck().setRunAtStartup(true);

    configurator.setHrConfig(props);
    configurator.setClock(fixed(Instant.parse("2019-01-01T21:45:00.00Z"), ZoneId.systemDefault()));
    configurator.configure();


    verify(executorService).scheduleAtFixedRate(isA(DatabaseTransactionTaskWrapper.class), anyLong(),
        eq(props.getMembershipCheck().getPeriodInSeconds()), eq(TimeUnit.SECONDS));

    verify(executorService, never()).schedule(isA(DatabaseTransactionTaskWrapper.class), anyLong(), any());
  }
  
  @Test
  public void testStartupTaskScheduledTheNextDay() throws VOMSPluginConfigurationException {
    HrDbProperties props = new HrDbProperties();

    props.getMembershipCheck().setRunAtStartup(false);
    props.getMembershipCheck().setStartHour(22);

    configurator.setHrConfig(props);
    configurator.setClock(fixed(Instant.parse("2019-01-01T21:01:00.00Z"), ZoneId.systemDefault()));
    configurator.configure();


    verify(executorService).scheduleAtFixedRate(isA(DatabaseTransactionTaskWrapper.class), anyLong(),
        eq(props.getMembershipCheck().getPeriodInSeconds()), eq(TimeUnit.SECONDS));

    verify(executorService, never()).schedule(isA(DatabaseTransactionTaskWrapper.class), anyLong(), any());
  }
  



}
