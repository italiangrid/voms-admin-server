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
package org.glite.security.voms.admin.integration.cern.dto;

import static java.util.Objects.isNull;

import java.time.Instant;
import java.util.Date;
import java.util.Map;
import java.util.Objects;

import org.glite.security.voms.admin.integration.cern.DateUtils;


public class ParticipationDTO {

  InstituteDTO institute;
  String experiment;

  Date startDate;
  Date endDate;

  public ParticipationDTO() {
    // empty ctor
  }

  public InstituteDTO getInstitute() {
    return institute;
  }

  public void setInstitute(InstituteDTO institute) {
    this.institute = institute;
  }

  public Date getStartDate() {
    return startDate;
  }

  public void setStartDate(Date startDate) {
    this.startDate = startDate;
  }

  public Date getEndDate() {
    return endDate;
  }

  public void setEndDate(Date endDate) {
    this.endDate = endDate;
  }

  public String getExperiment() {
    return experiment;
  }

  public void setExperiment(String experiment) {
    this.experiment = experiment;
  }

  public boolean isValidAtInstant(Instant instant) {

    Date now = Date.from(instant);
    
    return getStartDate().before(Date.from(instant))
        && (isNull(getEndDate()) || getEndDate().after(now));
  }

  @Override
  public String toString() {
    return "ParticipationDTO [institute=" + institute + ", experiment=" + experiment
        + ", startDate=" + startDate + ", endDate=" + endDate + "]";
  }


  @SuppressWarnings("unchecked")
  public static ParticipationDTO fromJsonMap(Map<String, Object> json) {
    ParticipationDTO participation = new ParticipationDTO();
    participation.setExperiment((String) json.get("experiment"));
    participation.setStartDate(DateUtils.parseDate((String) json.get("startDate")));
    String endDate = (String) json.get("endDate");
    if (!Objects.isNull(endDate)) {
      participation.setEndDate(DateUtils.parseDate(endDate));
    }

    participation.setInstitute(InstituteDTO.fromJson((Map<String, Object>) json.get("institute")));
    return participation;
  }
}
