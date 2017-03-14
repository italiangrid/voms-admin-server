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
package org.glite.security.voms.admin.integration.orgdb.dao;

import java.util.List;

import org.glite.security.voms.admin.integration.orgdb.model.VOMSOrgDBPerson;
import org.glite.security.voms.admin.persistence.dao.generic.GenericDAO;
import org.hibernate.Session;

public interface OrgDBVOMSPersonDAO extends GenericDAO<VOMSOrgDBPerson, Long> {

  public VOMSOrgDBPerson findPersonByEmail(String emailAddress);

  public VOMSOrgDBPerson findPersonById(Long personId);

  public List<VOMSOrgDBPerson> findPersonByName(String firstName, String name);

  public List<VOMSOrgDBPerson> findPersonBySurname(String surname);

  public VOMSOrgDBPerson findPersonWithValidExperimentParticipationById(
    Long personId, String experimentName);

  public VOMSOrgDBPerson findPersonWithValidExperimentParticipationByEmail(
    String emailAddress, String experimentName);

  public List<VOMSOrgDBPerson> findPersonsWithValidExperimentParticipationByName(
    String name, String surname, String experimentName);

  public List<VOMSOrgDBPerson> findPersonsWithExpiredExperimentParticipationById(
    List<Long> personIds, String experimentName);

  public List<VOMSOrgDBPerson> findPersonsWithExpiredExperimentParticipationByEmail(
    List<String> emailAddresses, String experimentName);

  public List<VOMSOrgDBPerson> findPersonsWithValidExperimentParticipation(
    String experimentName);

  public Long countPersonsWithValidExperimentParticipation(String experimentName);

  public List<VOMSOrgDBPerson> findPersonsWithExpiredExperimentParticipation(
    String experimentName, List<String> validEmails);
  
  public void setSession(Session session);

}
