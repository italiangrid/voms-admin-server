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

package org.glite.security.voms.admin.test;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.glite.security.voms.admin.configuration.VOMSConfiguration;
import org.glite.security.voms.admin.persistence.dao.VOMSGroupDAO;
import org.glite.security.voms.admin.persistence.dao.generic.DAOFactory;
import org.glite.security.voms.admin.persistence.dao.generic.RequestDAO;
import org.glite.security.voms.admin.persistence.model.VOMSGroup;
import org.glite.security.voms.admin.persistence.model.request.NewVOMembershipRequest;
import org.glite.security.voms.admin.persistence.model.request.Request.STATUS;
import org.glite.security.voms.admin.persistence.model.request.RequesterInfo;

public class CreateRandomConfirmedMembershipRequests implements Runnable {
	
	public static final int NUM_REQUEST = 5;
	public static final String EMAIL = "andrea.ceccanti@cnaf.infn.it";
	
	public void run() {
		
		long requestLifetime = VOMSConfiguration.instance().getLong("voms.request.vo_membership.lifetime",
				300);
		
		Calendar now = Calendar.getInstance();
		
		now.add(Calendar.SECOND,(int) requestLifetime);
		
		Date expirationDate = now.getTime(); 
		
		List<VOMSGroup> groups = VOMSGroupDAO.instance().getAll();
		
		groups = groups.subList(1, groups.size());
		
		RequestDAO reqDAO = DAOFactory.instance().getRequestDAO();
		Integer groupSize = groups.size();
		
		Random r = new Random();
		
		for (int i=0; i < NUM_REQUEST; i++){
			
			RequesterInfo ri = new RequesterInfo();
			ri.setName("Ille "+i);
			ri.setSurname("Camughe "+i);
			ri.setEmailAddress(EMAIL);
			ri.setInstitution("IGI");
			ri.setCertificateSubject("Test "+i);
			ri.setCertificateIssuer("/C=IT/O=INFN/CN=INFN CA");
			
			Integer randomGroupSize = r.nextInt(15);
			
			ri.addInfo(RequesterInfo.MULTIVALUE_COUNT_PREFIX+"requestedGroup", randomGroupSize.toString());
			
			for (int j=0; j < randomGroupSize; j++)
				ri.addInfo("requestedGroup"+j, groups.get(j).getName());
			
			NewVOMembershipRequest request = reqDAO.createVOMembershipRequest(ri, expirationDate);
			request.setStatus(STATUS.CONFIRMED);
		

		}
	}
}
