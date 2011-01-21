/**
 * Copyright (c) Members of the EGEE Collaboration. 2006-2009.
 * See http://www.eu-egee.org/partners/ for details on the copyright holders.
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
 *
 * Authors:
 * 	Andrea Ceccanti (INFN)
 */

package org.glite.security.voms.admin.integration.orgdb.database;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;

import org.glite.security.voms.admin.integration.orgdb.dao.OrgDBDAOFactory;
import org.glite.security.voms.admin.integration.orgdb.dao.OrgDBVOMSPersonDAO;
import org.glite.security.voms.admin.integration.orgdb.model.Experiment;
import org.glite.security.voms.admin.integration.orgdb.model.Institute;
import org.glite.security.voms.admin.integration.orgdb.model.Participation;
import org.glite.security.voms.admin.integration.orgdb.model.VOMSOrgDBPerson;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.tool.hbm2ddl.SchemaUpdate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class OrgDBDeployer {
	
	public static final Logger log = LoggerFactory.getLogger(OrgDBDeployer.class);
	
	/**
	 * This 
	 */
	public static final String ORG_DB_TEST_PROPERTIES_PATH = "/test/orgdb-test.properties";
	public static final String PRODUCTION_CONF_PROPERTY_NAME = "OrgDBPropertiesPath";

	private static final String INSTITUTE_NAME = "test";
	private static final String MY_EMAIL = "andrea.ceccanti@cnaf.infn.it";
	
	
	protected Properties getOrgDBTestProperties() throws IOException{
		
		Properties props = new Properties();
		
		props.load(getClass().getResourceAsStream(ORG_DB_TEST_PROPERTIES_PATH));
		return props;
		
		
	}
	
	protected Properties getOrgDBProductionProperties() throws IOException{
		
		Properties props = new Properties();
		FileInputStream propStream = new FileInputStream(new File(System.getProperty(PRODUCTION_CONF_PROPERTY_NAME)));
		
		props.load(propStream);
		
		return props;
		
	}
	protected void deployTestDB() throws IOException{
		
		Configuration cfg = OrgDBSessionFactory.buildConfiguration(getOrgDBTestProperties());
		SchemaUpdate updater = new SchemaUpdate(cfg);
		updater.execute(true, true);
		
	}	
	
	
	protected void createTestUser(String name, String surname, String emailAddress, String experimentName) throws IOException {
		
		OrgDBSessionFactory.initialize(getOrgDBTestProperties());
		Session s = OrgDBSessionFactory.getSessionFactory().openSession();
		Transaction t = s.beginTransaction();
		
		String instituteId = (String)s.createQuery("select code from Institute where originalName = :name").setString("name", INSTITUTE_NAME).uniqueResult();
		Experiment e = (Experiment) s.createQuery("from Experiment where name = :experimentName").setString("experimentName", experimentName).uniqueResult();
		
		Calendar cal = Calendar.getInstance();
		
		cal.roll(Calendar.DAY_OF_YEAR, -1);
		
		if (e == null){
			
			// Create experiment
			e = new Experiment();
			e.setName(experimentName);
			e.setStatus("PR"); // Don't ask me what this means
			e.setGbFlag("Y");
			e.setParent(null);
			s.save(e);
		}
		
		OrgDBVOMSPersonDAO dao = OrgDBDAOFactory.instance().getVOMSPersonDAO();
		
		List<VOMSOrgDBPerson> persons = dao.findPersonByName(name, surname);
		
		VOMSOrgDBPerson p;
		
		if (persons.isEmpty()){
			
			p = new VOMSOrgDBPerson();
			Long newId = (Long)s.createQuery("select max(id)+1 from VOMSPerson").uniqueResult();
			p.setId(newId);
			
			p.setFirstName(name);
			p.setName(surname);
			p.setEmail(emailAddress);
			p.setPhysicalEmail(emailAddress);
		
			p.setAtCern("Y");
			
			
		}else{
			
			p = persons.get(0);
			
			
		}
		
		Participation pp = new Participation();
		pp.getId().setPersonId(p.getId());
		pp.getId().setInstituteId(instituteId);
		pp.getId().setStartDate(cal.getTime());
		pp.getId().setExperimentId(e.getName());
		
		if (p.getParticipations() == null)
			p.setParticipations(new HashSet<Participation>());
		
		p.getParticipations().add(pp);
		
		s.saveOrUpdate(p);
		s.saveOrUpdate(pp);
		
		t.commit();
		s.close();
		
	}
	public OrgDBDeployer() throws IOException {
		
		createTestUser("CICCIO", "PASTICCIO", MY_EMAIL, "ATLAS");
		
		
	}
	
	
	
	public static void main(String[] args) throws IOException {
		
		new OrgDBDeployer();

	}

}
