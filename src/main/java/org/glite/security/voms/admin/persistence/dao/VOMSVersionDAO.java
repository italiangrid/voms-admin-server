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
package org.glite.security.voms.admin.persistence.dao;

import org.glite.security.voms.admin.configuration.VOMSConfiguration;
import org.glite.security.voms.admin.configuration.VOMSConfigurationConstants;
import org.glite.security.voms.admin.core.VOMSServiceConstants;
import org.glite.security.voms.admin.persistence.error.HibernateFactory;
import org.glite.security.voms.admin.persistence.model.VOMSDBVersion;

public class VOMSVersionDAO {

	private VOMSVersionDAO() {

		super();
	}

	public static VOMSVersionDAO instance() {

		return new VOMSVersionDAO();
	}

	
	public VOMSDBVersion createCurrentVersion(){
		VOMSDBVersion v = new VOMSDBVersion();
		v.setVersion(VOMSServiceConstants.VOMS_DB_VERSION);
		v.setAdminVersion(VOMSConfiguration.instance().getString(VOMSConfigurationConstants.VOMS_ADMIN_SERVER_VERSION));
		
		return v;
	}
	
	public void setupVersion() {

		VOMSDBVersion v = getVersion();
		
		if (v == null){
			
			v = createCurrentVersion();
			HibernateFactory.getSession().save(v);
			
		}else{
			
			if (v.getVersion() != VOMSServiceConstants.VOMS_DB_VERSION){
				// Delete existing VOMS server version
				HibernateFactory.getSession().delete(v);
				
				HibernateFactory.getSession().save(createCurrentVersion());
			}else{
				
				v.setAdminVersion(VOMSConfiguration.instance().getString(VOMSConfigurationConstants.VOMS_ADMIN_SERVER_VERSION));
			}
			
		}

	}
	
	public VOMSDBVersion getVersion(){
		
		return (VOMSDBVersion) HibernateFactory.getSession().createQuery("from VOMSDBVersion" ).uniqueResult();
		
		
	}
	
	
	

}
