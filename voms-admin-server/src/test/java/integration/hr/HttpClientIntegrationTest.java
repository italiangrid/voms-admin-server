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

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;
import static org.mockserver.integration.ClientAndServer.startClientAndServer;
import static org.mockserver.matchers.Times.exactly;
import static org.mockserver.matchers.Times.once;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;
import static utils.TestSocketUtils.findAvailableTcpPort;

import java.util.Optional;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.glite.security.voms.admin.integration.cern.HrDbError;
import org.glite.security.voms.admin.integration.cern.HrDbProperties;
import org.glite.security.voms.admin.integration.cern.HttpClientHrDbApiService;
import org.glite.security.voms.admin.integration.cern.dto.ParticipationDTO;
import org.glite.security.voms.admin.integration.cern.dto.VOPersonDTO;
import org.glite.security.voms.admin.persistence.model.VOMSUser;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.model.HttpError;
import org.mockserver.model.HttpResponse;

public class HttpClientIntegrationTest extends HrDbTestSupport{

  private static int port;
  private static ClientAndServer mockServer;

  HrDbProperties properties;
  HttpClientHrDbApiService apiService;

  @BeforeClass
  public static void startMockServer() {
    port = findAvailableTcpPort();
    mockServer = startClientAndServer(port);
  }

  @AfterClass
  public static void stopMockServer() {
    mockServer.stop();
  }

  @Before
  public void setup() {
    properties = new HrDbProperties();
    properties.setExperimentName("cms");
    properties.getApi().setEndpoint("http://localhost:" + port);
    properties.getApi().setUsername("user");
    properties.getApi().setPassword("password");
    properties.getApi().setTimeoutInSeconds(1L);

    apiService = new HttpClientHrDbApiService(CLOCK, properties);
  }

  @After
  public void teardown() {
    mockServer.reset();
  }


  @Test(expected = HrDbError.class)
  public void testVoPersonAuthNErrorHandling() {
    mockServer.when(request("/api/VOPersons/1").withMethod("GET"), once())
      .respond(HttpResponse.response().withStatusCode(403));

    try {
      apiService.getVoPersonRecord(1);

    } catch (HrDbError e) {

      assertThat(e.getMessage(), containsString("403"));
      mockServer.verify(request("/api/VOPersons/1").withMethod("GET")
        .withHeader("Authorization", "basic dXNlcjpwYXNzd29yZA=="));
      throw e;
    }
  }

  @Test(expected = HrDbError.class)
  public void testTimeoutEnforced() {
    mockServer.when(request("/api/VOPersons/1").withMethod("GET"), once())
      .respond(HttpResponse.response().withStatusCode(404).withDelay(TimeUnit.SECONDS, 2));

    try {
      apiService.getVoPersonRecord(1);

    } catch (HrDbError e) {

      assertThat(e.getMessage(), containsString("request timeout exceeded"));
      throw e;
    }
  }

  @Test(expected = HrDbError.class)
  public void testDropConnectionError() {
    mockServer.when(request("/api/VOPersons/1").withMethod("GET"), once())
      .error(HttpError.error().withDropConnection(true));;

    try {
      apiService.getVoPersonRecord(1);

    } catch (HrDbError e) {

      assertThat(e.getMessage(), is("Error contacting the HR DB api"));
      throw e;
    }
  }


  @Test(expected = HrDbError.class)
  public void testRandomBytesResponse() {

    byte[] randomByteArray = new byte[25];
    new Random().nextBytes(randomByteArray);

    mockServer.when(request("/api/VOPersons/1").withMethod("GET"), once())
      .error(HttpError.error().withResponseBytes(randomByteArray));;

    try {
      apiService.getVoPersonRecord(1);

    } catch (HrDbError e) {

      assertThat(e.getMessage(), is("Error contacting the HR DB api"));
      throw e;
    }
  }

  @Test
  public void testGetPersonSuccess() {

    mockServer.when(request("/api/VOPersons/1").withMethod("GET"), once())
      .respond(response(VO_PERSON_1_JSON));

    Optional<VOPersonDTO> voPerson = apiService.getVoPersonRecord(1);

    mockServer.verify(request("/api/VOPersons/1").withMethod("GET")
      .withHeader("Authorization", "basic dXNlcjpwYXNzd29yZA=="));

    assertThat(voPerson.isPresent(), is(true));
    assertThat(voPerson.get().getId(), is(1L));
    assertThat(voPerson.get().getName(), is("MARPLE"));
    assertThat(voPerson.get().getFirstName(), is("IRWIN"));
    assertThat(voPerson.get().getEmail(), is("IRWIN.MARPLE.1@mail.example"));
    assertThat(voPerson.get().getPhysicalEmail(), is("MARPLE.1@cern.ch"));
    assertThat(voPerson.get().getParticipations(), hasSize(1));

    ParticipationDTO participation = voPerson.get().getParticipations().iterator().next();

    assertThat(participation.getExperiment(), is("ATLAS"));
    assertThat(participation.getStartDate(), notNullValue());
    assertThat(participation.getEndDate(), notNullValue());
    assertThat(participation.isValidAtInstant(NOW), is(true));

    assertThat(participation.getInstitute(), notNullValue());
    assertThat(participation.getInstitute().getName(), is("Institute 35"));
    assertThat(participation.getInstitute().getId(), is("000035"));
    assertThat(participation.getInstitute().getTown(), is("Some place 35"));
    assertThat(participation.getInstitute().getCountry(), is("IT"));
  }

  @Test
  public void testGetPersonByEmailSuccess() {

    mockServer
      .when(request("/api/VOPersons/email/IRWIN.MARPLE.1@mail.example").withMethod("GET"), once())
      .respond(response(VO_PERSON_1_JSON));

    Optional<VOPersonDTO> voPerson =
        apiService.getVoPersonRecordByEmail("IRWIN.MARPLE.1@mail.example");

    mockServer.verify(request("/api/VOPersons/email/IRWIN.MARPLE.1@mail.example").withMethod("GET")
      .withHeader("Authorization", "basic dXNlcjpwYXNzd29yZA=="));

    assertThat(voPerson.isPresent(), is(true));
    assertThat(voPerson.get().getId(), is(1L));
    assertThat(voPerson.get().getName(), is("MARPLE"));
    assertThat(voPerson.get().getFirstName(), is("IRWIN"));
    assertThat(voPerson.get().getEmail(), is("IRWIN.MARPLE.1@mail.example"));
    assertThat(voPerson.get().getPhysicalEmail(), is("MARPLE.1@cern.ch"));
    assertThat(voPerson.get().getParticipations(), hasSize(1));

    ParticipationDTO participation = voPerson.get().getParticipations().iterator().next();

    assertThat(participation.getExperiment(), is("ATLAS"));
    assertThat(participation.getStartDate(), notNullValue());
    assertThat(participation.getEndDate(), notNullValue());
    assertThat(participation.isValidAtInstant(NOW), is(true));

    assertThat(participation.getInstitute(), notNullValue());
    assertThat(participation.getInstitute().getName(), is("Institute 35"));
    assertThat(participation.getInstitute().getId(), is("000035"));
    assertThat(participation.getInstitute().getTown(), is("Some place 35"));
    assertThat(participation.getInstitute().getCountry(), is("IT"));
  }

  @Test
  public void testHasValidExperimentParticipationSuccess() {
    mockServer.when(request("/api/VOPersons/participation/cms/valid/1").withMethod("GET"), once())
      .respond(response("true"));

    assertThat(apiService.hasValidExperimentParticipation(1), is(true));
    mockServer.verify(request("/api/VOPersons/participation/cms/valid/1").withMethod("GET")
      .withHeader("Authorization", "basic dXNlcjpwYXNzd29yZA=="));


  }

  @Test
  public void testHasValidExperimentParticipationByEmailSuccess() {

    mockServer
      .when(request("/api/VOPersons/email/IRWIN.MARPLE.1@mail.example").withMethod("GET"), once())
      .respond(response(VO_PERSON_1_JSON));

    assertThat(apiService.hasValidExperimentParticipationByEmail("IRWIN.MARPLE.1@mail.example"),
        is(true));

    mockServer.verify(request("/api/VOPersons/email/IRWIN.MARPLE.1@mail.example").withMethod("GET")
      .withHeader("Authorization", "basic dXNlcjpwYXNzd29yZA=="));
  }

  @Test
  public void testGetPersonByVomsUser() {

    mockServer.when(request("/api/VOPersons/1").withMethod("GET"), exactly(2))
      .respond(response(VO_PERSON_1_JSON));

    mockServer.when(request("/api/VOPersons/participation/cms/valid/1").withMethod("GET"), once())
      .respond(response("true"));

    VOMSUser user = new VOMSUser();
    user.setId(1L);
    user.setOrgDbId(1L);

    Optional<VOPersonDTO> voPerson = apiService.lookupVomsUser(user);

    assertThat(voPerson.isPresent(), is(true));
    assertThat(voPerson.get().getId(), is(1L));
    assertThat(voPerson.get().getName(), is("MARPLE"));
    assertThat(voPerson.get().getFirstName(), is("IRWIN"));
    assertThat(voPerson.get().getEmail(), is("IRWIN.MARPLE.1@mail.example"));
    assertThat(voPerson.get().getPhysicalEmail(), is("MARPLE.1@cern.ch"));
    assertThat(voPerson.get().getParticipations(), hasSize(1));

    ParticipationDTO participation = voPerson.get().getParticipations().iterator().next();

    assertThat(participation.getExperiment(), is("ATLAS"));
    assertThat(participation.getStartDate(), notNullValue());
    assertThat(participation.getEndDate(), notNullValue());
    assertThat(participation.isValidAtInstant(NOW), is(true));

    assertThat(participation.getInstitute(), notNullValue());
    assertThat(participation.getInstitute().getName(), is("Institute 35"));
    assertThat(participation.getInstitute().getId(), is("000035"));
    assertThat(participation.getInstitute().getTown(), is("Some place 35"));
    assertThat(participation.getInstitute().getCountry(), is("IT"));

    mockServer.verify(request("/api/VOPersons/1").withMethod("GET")
      .withHeader("Authorization", "basic dXNlcjpwYXNzd29yZA=="));

    assertThat(apiService.hasValidExperimentParticipation(user), is(true));

    mockServer.verify(request("/api/VOPersons/participation/cms/valid/1").withMethod("GET")
      .withHeader("Authorization", "basic dXNlcjpwYXNzd29yZA=="));

  }

  @Test
  public void testGetPersonByVomsUserNoOrgId() {

    VOMSUser user = new VOMSUser();
    user.setId(1L);

    assertThat(apiService.lookupVomsUser(user).isPresent(), is(false));
    mockServer.verifyZeroInteractions();
  }

}
