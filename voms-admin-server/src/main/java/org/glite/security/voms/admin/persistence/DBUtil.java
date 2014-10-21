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

		String f = String.format("%s/%s/%s", configurationDir, voName, "database.properties");
		return loadHibernateConfiguration(f);
	}
	
}
