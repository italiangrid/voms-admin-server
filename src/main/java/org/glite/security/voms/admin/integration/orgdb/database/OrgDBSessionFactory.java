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

import java.util.Properties;

import org.glite.security.voms.admin.integration.orgdb.model.Country;
import org.glite.security.voms.admin.integration.orgdb.model.Experiment;
import org.glite.security.voms.admin.integration.orgdb.model.Institute;
import org.glite.security.voms.admin.integration.orgdb.model.InstituteAddress;
import org.glite.security.voms.admin.integration.orgdb.model.Participation;
import org.glite.security.voms.admin.integration.orgdb.model.VOMSOrgDBPerson;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.context.ThreadLocalSessionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OrgDBSessionFactory {
	
	static final Logger log = LoggerFactory.getLogger(OrgDBSessionFactory.class);
	
	private static volatile SessionFactory orgDbSessionFactory;
	
	
	public static AnnotationConfiguration buildConfiguration(Properties orgDbHibernateProperties){
		
		AnnotationConfiguration cfg = new AnnotationConfiguration()
			.addAnnotatedClass(Country.class)
			.addAnnotatedClass(Experiment.class)
			.addAnnotatedClass(Institute.class)
			.addAnnotatedClass(InstituteAddress.class)
			.addAnnotatedClass(Participation.class)
			.addAnnotatedClass(VOMSOrgDBPerson.class)
			.setProperties(orgDbHibernateProperties);
		
		// Hardwired configuration properties
		Properties p = new Properties();
		p.setProperty("hibernate.current_session_context_class", ThreadLocalSessionContext.class.getName());
		// p.setProperty("hibernate.show_sql", "true");
		
		log.debug("Hardwired configuration properties: {}",p);
		
		cfg.addProperties(p);
		
		return cfg;
		
	}
	public synchronized static void initialize(Properties orgbHibernateProperties){
		
		if (orgDbSessionFactory != null)
			throw new OrgDBError("Session factory already initialized!");
		
		try{
			
			AnnotationConfiguration cfg = buildConfiguration(orgbHibernateProperties);
			
			orgDbSessionFactory = cfg.configure().buildSessionFactory();
		
		}catch (HibernateException e) {
			
			String errorMsg = String.format("Cannot initialize OrgDB database connection: %s", e.getMessage());
			log.error(errorMsg,e);
			throw new OrgDBError(errorMsg,e);
			
		}
	}
	
	
	public static SessionFactory getSessionFactory(){
		
		if (orgDbSessionFactory == null)
			throw new OrgDBError("Session factory not initialized!");
		
		return orgDbSessionFactory;
	}
	
	public static void shutdown(){
		if (getSessionFactory() != null)
			getSessionFactory().close();
		
	}
	
	private OrgDBSessionFactory(){}
}
