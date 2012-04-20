package org.glite.security.voms.admin.persistence;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.glite.security.voms.admin.error.VOMSException;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DBUtil {
	
	public static Logger log = LoggerFactory.getLogger(DBUtil.class);
	
	public static Configuration loadHibernateConfiguration(String hibernatePropertiesFile) {
		
		Properties dbProperties = new Properties();

		try {	
			
			dbProperties.load(new FileInputStream(hibernatePropertiesFile));

		} catch (IOException e) {

			log.error("Error loading hibernate properties: " + e.getMessage(),
					e);
			
			throw new VOMSException("Error loading hibernate properties: "
					+ e.getMessage(), e);
		}

		Configuration cfg = new AnnotationConfiguration().addProperties(
				dbProperties).configure();
		
		return cfg;
	
	}
	
	public static Configuration loadHibernateConfiguration(String configurationDir, String voName) {

		String f = String.format("%s/%s/%s", configurationDir, voName, "voms.database.properties");
		return loadHibernateConfiguration(f);
	}
	
}
