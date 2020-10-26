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

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;

import java.util.Date;

import org.glite.security.voms.admin.core.validation.ValidationManager;
import org.glite.security.voms.admin.integration.cern.HrDbProperties;
import org.glite.security.voms.admin.integration.cern.HrDefaultHandler;
import org.glite.security.voms.admin.integration.cern.dto.InstituteDTO;
import org.glite.security.voms.admin.integration.cern.dto.ParticipationDTO;
import org.glite.security.voms.admin.integration.cern.dto.VOPersonDTO;
import org.glite.security.voms.admin.persistence.model.VOMSUser;
import org.glite.security.voms.admin.persistence.model.VOMSUser.SuspensionReason;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import com.google.common.collect.Sets;

public class HrDefaultHandlerTest extends HrDbTestSupport {
  
  @Spy
  HrDbProperties properties = new HrDbProperties();

  @Mock
  ValidationManager manager;

  @Spy
  VOMSUser user = new VOMSUser();

  @Spy
  VOPersonDTO voPerson = new VOPersonDTO();

  @Spy
  ParticipationDTO oneParticipation = new ParticipationDTO();

  @Spy
  ParticipationDTO anotherParticipation = new ParticipationDTO();

  @Spy
  ParticipationDTO aThirdParticipation = new ParticipationDTO();

  @Spy
  InstituteDTO institute = new InstituteDTO();

  HrDefaultHandler handler;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    handler = new HrDefaultHandler(CLOCK, properties, manager);
    
    properties.setExperimentName("experiment");
    
    institute.setId("institute-id");
    institute.setName("Institute");
    
    user.setId(1L);
    user.setName("test");
    user.setSurname("user");
    user.setOrgDbId(-1L);
    
    voPerson.setId(1L);
    
    voPerson.setFirstName("TEST");
    voPerson.setName("USER");
    voPerson.setEmail("TEST.USER@CNAF.INFN.IT");
 
    oneParticipation.setExperiment("experiment");
    oneParticipation.setStartDate(Date.from(ONE_WEEK_AGO));
    
    voPerson.setParticipations(Sets.newHashSet(oneParticipation));

  }


  @Test
  public void testMissingHrRecordLeadsToUserSuspension() {

    handler.handleMissingHrRecord(user);

    SuspensionReason reason = SuspensionReason.HR_DB_VALIDATION;
    reason.setMessage("OrgDB: No record found");

    verify(manager).suspendUser(Mockito.eq(user), Mockito.eq(reason));

  }

  @Test
  public void testSynchStrategyMultipleParticipations() {
    
    oneParticipation.setExperiment("experiment");
    oneParticipation.setStartDate(Date.from(TWO_YEARS_AGO));
    oneParticipation.setEndDate(Date.from(ONE_YEAR_AGO));

    anotherParticipation.setExperiment("experiment");
    anotherParticipation.setStartDate(Date.from(ONE_WEEK_AGO));

    aThirdParticipation.setExperiment("another");
    oneParticipation.setStartDate(Date.from(TWO_YEARS_AGO));


    voPerson.setParticipations(
        Sets.newHashSet(oneParticipation, anotherParticipation, aThirdParticipation));

    handler.synchronizeMembershipInformation(user, voPerson);
    
    assertThat(user.getName(), is("TEST"));
    assertThat(user.getSurname(), is("USER"));
    assertThat(user.getEmailAddress(), is("TEST.USER@CNAF.INFN.IT".toLowerCase()));
    assertThat(user.getEndTime(), nullValue());
    
  }
  

  @Test
  public void testSynchStrategy() {

    oneParticipation.setExperiment("experiment");
    oneParticipation.setStartDate(Date.from(ONE_WEEK_AGO));


    voPerson.setParticipations(Sets.newHashSet(oneParticipation));

    handler.synchronizeMembershipInformation(user, voPerson);

    assertThat(user.getName(), is("TEST"));
    assertThat(user.getSurname(), is("USER"));
    assertThat(user.getEmailAddress(), is("TEST.USER@CNAF.INFN.IT".toLowerCase()));
    assertThat(user.getEndTime(), nullValue());


  }

  @Test
  public void testSynchInvalidParticipation() {
    
    oneParticipation.setExperiment("experiment");
    oneParticipation.setStartDate(Date.from(ONE_YEAR_AGO));
    oneParticipation.setEndDate(Date.from(ONE_WEEK_AGO));
    
    voPerson.setParticipations(Sets.newHashSet(oneParticipation));

    handler.synchronizeMembershipInformation(user, voPerson);
    
    assertThat(user.getName(), is("TEST"));
    assertThat(user.getSurname(), is("USER"));
    assertThat(user.getEmailAddress(), is("TEST.USER@CNAF.INFN.IT".toLowerCase()));
    assertThat(user.getEndTime(), is(Date.from(NOW)));
  }
  
  @Test
  public void testDoNotRestoreSuspendedUserForOtherReason() {
    
    oneParticipation.setExperiment("experiment");
    oneParticipation.setStartDate(Date.from(ONE_YEAR_AGO));
    
    user.setSuspended(true);
    user.setSuspensionReason(SuspensionReason.FAILED_TO_SIGN_AUP.getMessage());
    
    voPerson.setParticipations(Sets.newHashSet(oneParticipation));

    handler.synchronizeMembershipInformation(user, voPerson);
    
    assertThat(user.getName(), is("TEST"));
    assertThat(user.getSurname(), is("USER"));
    assertThat(user.getEmailAddress(), is("TEST.USER@CNAF.INFN.IT".toLowerCase()));
    
    assertThat(user.isSuspended(), is(true));
  }
  
  @Test
  public void testRestoreSuspendedUserDueToValidationError() {
    
    oneParticipation.setExperiment("experiment");
    oneParticipation.setStartDate(Date.from(ONE_YEAR_AGO));
    
    user.setSuspended(true);
    user.setSuspensionReasonCode(SuspensionReason.HR_DB_VALIDATION);
    user.setSuspensionReason(SuspensionReason.HR_DB_VALIDATION.getMessage());
    
    voPerson.setParticipations(Sets.newHashSet(oneParticipation));

    handler.synchronizeMembershipInformation(user, voPerson);
    
    assertThat(user.getName(), is("TEST"));
    assertThat(user.getSurname(), is("USER"));
    assertThat(user.getEmailAddress(), is("TEST.USER@CNAF.INFN.IT".toLowerCase()));
    
    verify(manager).restoreUser(Mockito.eq(user));
    
    
  }
  
  @Test
  public void testRestoreSuspendedUserDueToMembershipExpiration() {
    
    oneParticipation.setExperiment("experiment");
    oneParticipation.setStartDate(Date.from(ONE_YEAR_AGO));
    
    user.setSuspended(true);
    user.setSuspensionReasonCode(SuspensionReason.MEMBERSHIP_EXPIRATION);
    
    user.setSuspensionReason(SuspensionReason.MEMBERSHIP_EXPIRATION.getMessage());
    
    voPerson.setParticipations(Sets.newHashSet(oneParticipation));

    handler.synchronizeMembershipInformation(user, voPerson);
    
    assertThat(user.getName(), is("TEST"));
    assertThat(user.getSurname(), is("USER"));
    assertThat(user.getEmailAddress(), is("TEST.USER@CNAF.INFN.IT".toLowerCase()));
    
    verify(manager).restoreUser(Mockito.eq(user));
    
    
  }


}
