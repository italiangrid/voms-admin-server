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
