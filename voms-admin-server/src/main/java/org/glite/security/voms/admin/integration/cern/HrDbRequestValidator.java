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

import static org.glite.security.voms.admin.core.validation.RequestValidationResult.success;

import java.time.Clock;
import java.util.List;
import java.util.Optional;

import org.glite.security.voms.admin.core.validation.RequestValidationContext;
import org.glite.security.voms.admin.core.validation.RequestValidationResult;
import org.glite.security.voms.admin.core.validation.strategies.RequestValidationStrategy;
import org.glite.security.voms.admin.integration.cern.dto.ParticipationDTO;
import org.glite.security.voms.admin.integration.cern.dto.VOPersonDTO;
import org.glite.security.voms.admin.persistence.model.request.NewVOMembershipRequest;

import com.google.common.collect.Lists;

public class HrDbRequestValidator
    implements RequestValidationStrategy<NewVOMembershipRequest>, RequestValidationContext {

  final Clock clock;
  final HrDbProperties properties;
  final HrDbApiService apiService;

  public HrDbRequestValidator(Clock clock, HrDbProperties properties, HrDbApiService apiService) {
    this.clock = clock;
    this.properties = properties;
    this.apiService = apiService;
  }

  @Override
  public RequestValidationStrategy<NewVOMembershipRequest> getVOMembershipRequestValidationStrategy() {
    return this;
  }

  RequestValidationResult dataMismatch(String email, List<String> errors) {
    RequestValidationResult result = RequestValidationResult
      .failure("HR DB validation failed. The HR DB VOMS person record linked to email address '"
          + email + "' did not match the data you entered.");
    result.setErrorMessages(errors);

    return result;
  }


  RequestValidationResult noParticipationFound(String email) {
    return RequestValidationResult.failure("No HR db participation found matching email '" + email
        + "' for experiment '" + properties.getExperimentName() + "'.");
  }

  protected void propertyEqualsIgnoreCase(String value1, String value2, String propertyName,
      List<String> errors) {

    if (!value1.equalsIgnoreCase(value2)) {

      String errorMessage = String.format("Property '" + propertyName
          + "' does not match (ignoring case) the OrgDB VOMS person record. You entered  '%s', while  '%s' was expected.",
          value1, value2);
      errors.add(errorMessage);
    }

  }

  RequestValidationResult validateRequestData(NewVOMembershipRequest request, VOPersonDTO person) {
    Optional<ParticipationDTO> participation = person
      .findValidParticipationsForExperiment(clock.instant(), properties.getExperimentName());

    if (participation.isPresent()) {

      List<String> validationErrors = Lists.newArrayList();

      propertyEqualsIgnoreCase(request.getRequesterInfo().getName(), person.getFirstName(), "name",
          validationErrors);
      propertyEqualsIgnoreCase(request.getRequesterInfo().getSurname(), person.getName(), "surname",
          validationErrors);


      if (!validationErrors.isEmpty()) {
        return dataMismatch(request.getRequesterInfo().getEmailAddress(), validationErrors);
      }

      return success();

    } else {
      return noParticipationFound(request.getRequesterInfo().getEmailAddress());
    }
  }

  @Override
  public RequestValidationResult validateRequest(NewVOMembershipRequest r) {

    String requestorEmail = r.getRequesterInfo().getEmailAddress();

    Optional<VOPersonDTO> voPerson = apiService.getVoPersonRecordByEmail(requestorEmail);

    if (voPerson.isPresent()) {

      return validateRequestData(r, voPerson.get());

    } else {
      return noParticipationFound(requestorEmail);
    }

  }

}
