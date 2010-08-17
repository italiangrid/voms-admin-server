package org.glite.security.voms.admin.integration.orgdb.dao;

import java.util.List;

import org.glite.security.voms.admin.integration.orgdb.model.VOMSPerson;
import org.glite.security.voms.admin.persistence.dao.generic.GenericDAO;

public interface OrgDBVOMSPersonDAO extends GenericDAO<VOMSPerson, Long> {
	
	public VOMSPerson findPersonByEmail(String emailAddress);
	public VOMSPerson findPersonById(Long personId);
	public List<VOMSPerson> findPersonByName(String firstName, String name);
	public List<VOMSPerson> findPersonBySurname(String surname);
	
	public VOMSPerson findPersonWithValidExperimentParticipationById(Long personId, String experimentName);
	public VOMSPerson findPersonWithValidExperimentParticipationByEmail(String emailAddress, String experimentName);
	public List<VOMSPerson> findPersonsWithValidExperimentParticipationByName(String name, String surname, String experimentName);
	
	public List<VOMSPerson> findPersonsWithExpiredExperimentParticipationById(List<Long> personIds, String experimentName);
	public List<VOMSPerson> findPersonsWithExpiredExperimentParticipationByEmail(List<String> emailAddresses, String experimentName);
	
	public List<VOMSPerson> findPersonsWithValidExperimentParticipation(String experimentName);
	public Long countPersonsWithValidExperimentParticipation(String experimentName);
	
	public List<VOMSPerson> findPersonsWithExpiredExperimentParticipation(String experimentName);
	
}
