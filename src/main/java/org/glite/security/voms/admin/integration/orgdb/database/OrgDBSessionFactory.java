package org.glite.security.voms.admin.integration.orgdb.database;

import java.util.Properties;

import org.glite.security.voms.admin.integration.orgdb.model.Country;
import org.glite.security.voms.admin.integration.orgdb.model.Experiment;
import org.glite.security.voms.admin.integration.orgdb.model.Institute;
import org.glite.security.voms.admin.integration.orgdb.model.InstituteAddress;
import org.glite.security.voms.admin.integration.orgdb.model.Participation;
import org.glite.security.voms.admin.integration.orgdb.model.VOMSPerson;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OrgDBSessionFactory {
	
	static final Logger log = LoggerFactory.getLogger(OrgDBSessionFactory.class);
	
	private static SessionFactory orgDbSessionFactory;
	
	public static void initialize(Properties orgbHibernateProperties){
		
		if (orgDbSessionFactory != null)
			throw new OrgDBError("Session factory already initialized!");
		
		try{
			
			AnnotationConfiguration cfg = new AnnotationConfiguration()
				.addAnnotatedClass(Country.class)
				.addAnnotatedClass(Experiment.class)
				.addAnnotatedClass(Institute.class)
				.addAnnotatedClass(InstituteAddress.class)
				.addAnnotatedClass(Participation.class)
				.addAnnotatedClass(VOMSPerson.class)
				.setProperties(orgbHibernateProperties);
			
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
