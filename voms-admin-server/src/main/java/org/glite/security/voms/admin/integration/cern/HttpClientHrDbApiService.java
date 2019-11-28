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

import static java.util.Objects.isNull;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.time.Clock;
import java.time.Instant;
import java.util.Base64;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.eclipse.jetty.client.ContentExchange;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.HttpExchange;
import org.eclipse.jetty.util.ajax.JSON;
import org.glite.security.voms.admin.integration.cern.dto.VOPersonDTO;
import org.glite.security.voms.admin.persistence.model.VOMSUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpClientHrDbApiService implements HrDbApiService {

  private static final Logger LOG = LoggerFactory.getLogger(HttpClientHrDbApiService.class);

  private final HttpClient client;

  final String experimentName;
  final String voPersonRecordEndpoint;
  final String voPersonRecordByEmailEndpoint;
  final String validParticipationEndpoint;
  final String basicAuthHeaderContent;
  final Clock clock;

  public HttpClientHrDbApiService(Clock clock, HrDbProperties properties) {
    client = new HttpClient();
    experimentName = properties.getExperimentName();
    this.clock = clock;

    basicAuthHeaderContent = Base64.getEncoder()
      .encodeToString(String
        .format("%s:%s", properties.getApi().getUsername(), properties.getApi().getPassword())
        .getBytes());


    voPersonRecordEndpoint = String.format("%s/api/VOPersons", properties.getApi().getEndpoint());
    voPersonRecordByEmailEndpoint =
        String.format("%s/api/VOPersons/email", properties.getApi().getEndpoint());

    validParticipationEndpoint = String.format("%s/api/VOPersons/participation/%s/valid",
        properties.getApi().getEndpoint(), properties.getExperimentName());


    try {
      client.setMaxRetries(3);
      client.setTimeout(TimeUnit.SECONDS.toMillis(properties.getApi().getTimeoutInSeconds()));
      client.start();
    } catch (Exception e) {
      throw new HrDbError("Error initializing HTTP client", e);
    }
  }

  private String voPersonByEmailUrl(String email) {
    return String.format("%s/%s", voPersonRecordByEmailEndpoint, email);
  }

  private String voPersonRecordUrl(long cernPersonId) {
    return String.format("%s/%d", voPersonRecordEndpoint, cernPersonId);
  }

  private String validParticipationUrl(long cernPersonId) {
    return String.format("%s/%d", validParticipationEndpoint, cernPersonId);
  }

  private ContentExchange prepareHttpExchange(String url) {
    ContentExchange ce = new ContentExchange();
    ce.setRetryStatus(true);
    ce.setRequestHeader("Authorization", String.format("Basic %s", basicAuthHeaderContent));
    ce.setRequestHeader("Accept", "application/json");

    ce.setURL(url);

    return ce;
  }

  protected void handleExchange(ContentExchange ce) {
    try {

      client.send(ce);
      int exchangeStatus = ce.waitForDone();

      if (HttpExchange.STATUS_COMPLETED == exchangeStatus) {
        if (ce.getResponseStatus() != 200) {

          final String errorMsg =
              String.format("HR Db Api Http request %s yelded response status: %d ",
                  ce.getRequestURI(), ce.getResponseStatus());
          throw new HrDbError(errorMsg);
        }
      } else if (HttpExchange.STATUS_EXPIRED == exchangeStatus){
        throw new HrDbError("Error contacting the HR DB api: request timeout exceeded");
      } else {
        throw new HrDbError("Error contacting the HR DB api");
      }

    } catch (IOException e) {
      throw new HrDbError("Error contacting the HR DB api: " + e.getMessage(), e);
    } catch (InterruptedException e) {
      LOG.warn(e.getMessage(), e);
    }
  }

  protected Optional<VOPersonDTO> voPersonExchange(ContentExchange ce) {
    handleExchange(ce);

    try {

      @SuppressWarnings("unchecked")
      Map<String, Object> jsonMap = (Map<String, Object>) JSON.parse(ce.getResponseContent());

      return Optional.of(VOPersonDTO.fromJsonMap(jsonMap));

    } catch (UnsupportedEncodingException e) {
      throw new HrDbError(e.getMessage(), e);
    }
  }

  @Override
  public Optional<VOPersonDTO> getVoPersonRecord(long cernPersonId) {
    return voPersonExchange(prepareHttpExchange(voPersonRecordUrl(cernPersonId)));
  }

  @Override
  public boolean hasValidExperimentParticipation(long cernPersonId) {

    ContentExchange ce = prepareHttpExchange(validParticipationUrl(cernPersonId));

    handleExchange(ce);

    try {
      return Boolean.parseBoolean(ce.getResponseContent());
    } catch (UnsupportedEncodingException e) {
      throw new HrDbError(e.getMessage(), e);
    }


  }

  @Override
  public Optional<VOPersonDTO> lookupVomsUser(VOMSUser user) {

    Long orgDbId = user.getOrgDbId();

    if (!isNull(orgDbId)) {
      return getVoPersonRecord(orgDbId);
    }
    return Optional.empty();
  }

  @Override
  public boolean hasValidExperimentParticipation(VOMSUser user) {
    return lookupVomsUser(user).map(p -> hasValidExperimentParticipation(p.getId())).orElse(false);
  }

  @Override
  public Optional<VOPersonDTO> getVoPersonRecordByEmail(String email) {
    return voPersonExchange(prepareHttpExchange(voPersonByEmailUrl(email)));
  }

  @Override
  public boolean hasValidExperimentParticipationByEmail(String email) {

    Instant now = clock.instant();

    return getVoPersonRecordByEmail(email)
      .map(p -> p.findValidParticipationsForExperiment(now, experimentName))
      .isPresent();
  }

}
