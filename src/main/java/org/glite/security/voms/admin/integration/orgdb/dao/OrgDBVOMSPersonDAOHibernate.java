package org.glite.security.voms.admin.integration.orgdb.dao;

import java.util.List;

import org.glite.security.voms.admin.integration.orgdb.model.VOMSPerson;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;


public class OrgDBVOMSPersonDAOHibernate extends OrgDBGenericHibernateDAO<VOMSPerson, Long> implements
	OrgDBVOMSPersonDAO
{

	public VOMSPerson findPersonWithValidExperimentParticipationById(Long personId, String experimentName) {
		
		String query = "select p from VOMSPerson p join p.participations pp where " +
		"p.id = :personId and "+
		"(pp.experiment.name = :experimentName and " +
		"pp.id.startDate <= current_date() and " +
		"(pp.endDate is null or "+
		"pp.endDate > current_date())" +
		")";
		
		Query q = getSession().createQuery(query)
			.setLong("personId", personId)
			.setString("experimentName", experimentName);
		
		return (VOMSPerson) q.uniqueResult();
	}

	public VOMSPerson findPersonWithValidExperimentParticipationByEmail(String emailAddress,
			String experimentName) {
		
		String query = "select p from VOMSPerson p join p.participations pp where " +
				"(lower(p.physicalEmail) = :email or " +
				"lower(p.email) = :email) and " +
				"(pp.experiment.name = :experimentName and " +
				"pp.id.startDate <= current_date() and " +
				"(pp.endDate is null or "+
				"pp.endDate > current_date())" +
				")";
		
		Query q = getSession().createQuery(query)
			.setString("email", emailAddress)
			.setString("experimentName", experimentName);
		
		return (VOMSPerson) q.uniqueResult();
		
	}

	public List<VOMSPerson> findPersonsWithExpiredExperimentParticipationById(List<Long> personIds,
			String experimentName) {
		
		return null;
	}

	public List<VOMSPerson> findPersonsWithExpiredExperimentParticipationByEmail(
			List<String> emailAddresses, String experimentName) {

		return null;
	}

	public VOMSPerson findPersonByEmail(String emailAddress) {
		
		String query = "select p from VOMSPerson p join p.participations pp where " +
			"lower(p.physicalEmail) = :email or lower(p.email) = :email";
		
		Query q = getSession().createQuery(query).setString("email", emailAddress);
		
		return (VOMSPerson) q.uniqueResult();
	}

	public VOMSPerson findPersonById(Long personId) {
		
		return (VOMSPerson)getSession().load(VOMSPerson.class, personId);
	}

	public List<VOMSPerson> findPersonByName(String firstName, String name) {
		
		return findByCriteria(Restrictions.eq("firstName", firstName).ignoreCase(), Restrictions.eq("name", name).ignoreCase());
	}

	public List<VOMSPerson> findPersonBySurname(String surname) {
		return findByCriteria(Restrictions.eq("name", surname).ignoreCase());
	}

	public List<VOMSPerson> findPersonsWithExpiredExperimentParticipation(
			String experimentName) {
		// TODO Auto-generated method stub
		return null;
	}

	@SuppressWarnings("unchecked")
	public List<VOMSPerson> findPersonsWithValidExperimentParticipation(
			String experimentName) {
		
		String query = "select p from VOMSPerson p join p.participations pp where " +
		"pp.experiment.name = :experimentName and " +
		"pp.id.startDate <= current_date() and " +
		"(pp.endDate is null or "+
		"pp.endDate > current_date()) and (p.email is not null or p.physicalEmail is not null)";
		
		Query q = getSession().createQuery(query).setString("experimentName", experimentName);	
		return q.list();
	}

	public Long countPersonsWithValidExperimentParticipation(
			String experimentName) {
		
		String query = "select count(*) from VOMSPerson p join p.participations pp where " +
		"pp.experiment.name = :experimentName and " +
		"pp.id.startDate <= current_date() and " +
		"(pp.endDate is null or "+
		"pp.endDate > current_date()) and (p.email is not null or p.physicalEmail is not null)";
		
		Query q = getSession().createQuery(query).setString("experimentName", experimentName);
		
		return (Long) q.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	public List<VOMSPerson> findPersonsWithValidExperimentParticipationByName(
			String name, String surname, String experimentName) {
		
		String query = "select p from VOMSPerson p join p.participations pp where " +
		"p.firstName = upper(:name) and "+
		"p.name  = upper(:surname) and "+
		"(pp.experiment.name = :experimentName and " +
		"pp.id.startDate <= current_date() and " +
		"(pp.endDate is null or "+
		"pp.endDate > current_date())" +
		")";
		
		Query q = getSession().createQuery(query)
		.setString("name", name)
		.setString("surname", surname)
		.setString("experimentName", experimentName);
		
		return q.list();
	}

	
}
