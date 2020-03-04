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

import static java.util.Optional.empty;
import static org.glite.security.voms.admin.core.validation.RequestValidationResult.Outcome.FAILURE;
import static org.glite.security.voms.admin.core.validation.RequestValidationResult.Outcome.SUCCESS;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.glite.security.voms.admin.core.validation.RequestValidationResult;
import org.glite.security.voms.admin.integration.cern.HrDbApiService;
import org.glite.security.voms.admin.integration.cern.HrDbProperties;
import org.glite.security.voms.admin.integration.cern.HrDbRequestValidator;
import org.glite.security.voms.admin.integration.cern.dto.ParticipationDTO;
import org.glite.security.voms.admin.integration.cern.dto.VOPersonDTO;
import org.glite.security.voms.admin.persistence.model.request.NewVOMembershipRequest;
import org.glite.security.voms.admin.persistence.model.request.RequesterInfo;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class HrDbRequestValidatorTest extends HrDbTestSupport {

  @Mock
  HrDbApiService apiService;

  @Mock
  NewVOMembershipRequest request;

  @Mock
  RequesterInfo requestor;

  @Mock
  VOPersonDTO person;
  
  @Mock
  ParticipationDTO participation;

  HrDbProperties hrConfig;
  HrDbRequestValidator validator;


  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    when(request.getRequesterInfo()).thenReturn(requestor);

    hrConfig = new HrDbProperties();
    validator = new HrDbRequestValidator(CLOCK, hrConfig, apiService);
  }

  @Test
  public void testNoRecordFound() {
    when(requestor.getEmailAddress()).thenReturn("test@example");
    when(apiService.getVoPersonRecordByEmail(anyString())).thenReturn(Optional.empty());

    RequestValidationResult result = validator.validateRequest(request);
    assertThat(result.getOutcome(), is(FAILURE));
    assertThat(result.getMessage(), containsString("No HR db participation found"));
  }

  @Test
  public void testNoValidParticipation() {

    when(requestor.getEmailAddress()).thenReturn("test@example");
    when(requestor.getName()).thenReturn("Adrienne");
    when(requestor.getSurname()).thenReturn("Lenker");

    when(apiService.getVoPersonRecordByEmail(anyString())).thenReturn(Optional.of(person));
    when(person.getEmail()).thenReturn("test@example");
    when(person.getPhysicalEmail()).thenReturn("test@cern.ch");
    when(person.getFirstName()).thenReturn("TEST");
    when(person.getName()).thenReturn("USER");
    when(person.findValidParticipationForExperiment(any(), anyString()))
      .thenReturn(empty());


    RequestValidationResult result = validator.validateRequest(request);

    assertThat(result.getOutcome(), is(FAILURE));
    assertThat(result.getMessage(), containsString("No HR db participation found"));
  }
  
  @Test
  public void testDataMismatch() {
    when(requestor.getEmailAddress()).thenReturn("test@example");
    when(requestor.getName()).thenReturn("Adrienne");
    when(requestor.getSurname()).thenReturn("Lenker");

    when(apiService.getVoPersonRecordByEmail(anyString())).thenReturn(Optional.of(person));
    when(person.getEmail()).thenReturn("test@example");
    when(person.getPhysicalEmail()).thenReturn("test@cern.ch");
    when(person.getFirstName()).thenReturn("TEST");
    when(person.getName()).thenReturn("USER");
    when(person.findValidParticipationForExperiment(any(), anyString()))
      .thenReturn(Optional.of(participation));
    
    RequestValidationResult result = validator.validateRequest(request);
    assertThat(result.getOutcome(), is(FAILURE));
    assertThat(result.getMessage(), containsString("did not match the data you entered"));
    
  }
  
  @Test
  public void testValidationSuccess() {
    when(requestor.getEmailAddress()).thenReturn("test@example");
    when(requestor.getName()).thenReturn("Adrienne");
    when(requestor.getSurname()).thenReturn("Lenker");

    when(apiService.getVoPersonRecordByEmail(anyString())).thenReturn(Optional.of(person));
    when(person.getEmail()).thenReturn("test@example");
    when(person.getPhysicalEmail()).thenReturn("test@cern.ch");
    when(person.getFirstName()).thenReturn("ADRIENNE");
    when(person.getName()).thenReturn("LENKER");
    when(person.findValidParticipationForExperiment(any(), anyString()))
      .thenReturn(Optional.of(participation));
    
    RequestValidationResult result = validator.validateRequest(request);
    assertThat(result.getOutcome(), is(SUCCESS));
    
  }

}
