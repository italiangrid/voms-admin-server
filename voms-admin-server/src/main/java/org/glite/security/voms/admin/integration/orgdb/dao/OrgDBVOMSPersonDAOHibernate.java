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
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;

public class OrgDBVOMSPersonDAOHibernate extends
  OrgDBGenericHibernateDAO<VOMSOrgDBPerson, Long> implements OrgDBVOMSPersonDAO {

  public VOMSOrgDBPerson findPersonWithValidExperimentParticipationById(
    Long personId, String experimentName) {

    String query = "select p from VOMSOrgDBPerson p join p.participations pp where "
      + "p.id = :personId and "
      + "(pp.experiment.name = :experimentName and "
      + "pp.id.startDate <= current_date() and "
      + "(pp.endDate is null or "
      + "pp.endDate > current_date())" + ")";

    Query q = getSession().createQuery(query).setLong("personId", personId)
      .setString("experimentName", experimentName);

    return (VOMSOrgDBPerson) q.uniqueResult();
  }

  public VOMSOrgDBPerson findPersonWithValidExperimentParticipationByEmail(
    String emailAddress, String experimentName) {

    String query = "select p from VOMSOrgDBPerson p join p.participations pp where "
      + "(lower(p.physicalEmail) = :email or "
      + "lower(p.email) = :email) and "
      + "(pp.experiment.name = :experimentName and "
      + "pp.id.startDate <= current_date() and "
      + "(pp.endDate is null or "
      + "pp.endDate > current_date())" + ")";

    Query q = getSession().createQuery(query)
      .setString("email", emailAddress.toLowerCase())
      .setString("experimentName", experimentName);

    return (VOMSOrgDBPerson) q.uniqueResult();

  }

  public List<VOMSOrgDBPerson> findPersonsWithExpiredExperimentParticipationById(
    List<Long> personIds, String experimentName) {

    return null;
  }

  public List<VOMSOrgDBPerson> findPersonsWithExpiredExperimentParticipationByEmail(
    List<String> emailAddresses, String experimentName) {

    return null;
  }

  public VOMSOrgDBPerson findPersonByEmail(String emailAddress) {

    String query = "select p from VOMSOrgDBPerson p join p.participations pp where "
      + "lower(p.physicalEmail) = :email or lower(p.email) = :email";

    Query q = getSession().createQuery(query).setString("email",
      emailAddress.toLowerCase());

    return (VOMSOrgDBPerson) q.uniqueResult();
  }

  public VOMSOrgDBPerson findPersonById(Long personId) {

    return (VOMSOrgDBPerson) getSession().load(VOMSOrgDBPerson.class, personId);
  }

  public List<VOMSOrgDBPerson> findPersonByName(String firstName, String name) {

    return findByCriteria(Restrictions.eq("firstName", firstName).ignoreCase(),
      Restrictions.eq("name", name).ignoreCase());
  }

  public List<VOMSOrgDBPerson> findPersonBySurname(String surname) {

    return findByCriteria(Restrictions.eq("name", surname).ignoreCase());
  }

  public List<VOMSOrgDBPerson> findPersonsWithExpiredExperimentParticipation(
    String experimentName, List<String> validEmails) {

    String query = "select p from VOMSOrgDBPerson p join p.participations pp where "
      + "pp.experiment.name = :experimentName and "
      + "pp.endDate != null  and pp.endDate < current_date() and"
      + "(lower(p.physicalEmail) in :validEmails or lower(p.email) in :validEmails)";

    Query q = getSession().createQuery(query)
      .setString("experimentName", experimentName)
      .setParameterList("validEmails", validEmails);
    return q.list();
  }

  @SuppressWarnings("unchecked")
  public List<VOMSOrgDBPerson> findPersonsWithValidExperimentParticipation(
    String experimentName) {

    String query = "select p from VOMSOrgDBPerson p join p.participations pp where "
      + "pp.experiment.name = :experimentName and "
      + "pp.id.startDate <= current_date() and "
      + "(pp.endDate is null or "
      + "pp.endDate > current_date()) and (p.email is not null or p.physicalEmail is not null)";

    Query q = getSession().createQuery(query).setString("experimentName",
      experimentName);
    return q.list();
  }

  public Long countPersonsWithValidExperimentParticipation(String experimentName) {

    String query = "select count(*) from VOMSOrgDBPerson p join p.participations pp where "
      + "pp.experiment.name = :experimentName and "
      + "pp.id.startDate <= current_date() and "
      + "(pp.endDate is null or "
      + "pp.endDate > current_date()) and (p.email is not null or p.physicalEmail is not null)";

    Query q = getSession().createQuery(query).setString("experimentName",
      experimentName);

    return (Long) q.uniqueResult();
  }

  @SuppressWarnings("unchecked")
  public List<VOMSOrgDBPerson> findPersonsWithValidExperimentParticipationByName(
    String name, String surname, String experimentName) {

    String query = "select p from VOMSOrgDBPerson p join p.participations pp where "
      + "p.firstName = upper(:name) and "
      + "p.name  = upper(:surname) and "
      + "(pp.experiment.name = :experimentName and "
      + "pp.id.startDate <= current_date() and "
      + "(pp.endDate is null or "
      + "pp.endDate > current_date())" + ")";

    Query q = getSession().createQuery(query).setString("name", name)
      .setString("surname", surname)
      .setString("experimentName", experimentName);

    return q.list();
  }

}
