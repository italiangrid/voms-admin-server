package org.glite.security.voms.admin.test;

import java.util.Calendar;
import java.util.Date;

import org.apache.struts2.views.util.UrlHelper;
import org.glite.security.voms.admin.configuration.VOMSConfiguration;
import org.glite.security.voms.admin.configuration.VOMSConfigurationConstants;
import org.glite.security.voms.admin.persistence.DBUtil;
import org.glite.security.voms.admin.persistence.HibernateFactory;
import org.glite.security.voms.admin.persistence.dao.generic.DAOFactory;
import org.glite.security.voms.admin.persistence.dao.generic.RequestDAO;
import org.glite.security.voms.admin.persistence.model.request.NewVOMembershipRequest;
import org.glite.security.voms.admin.persistence.model.request.RequesterInfo;
import org.glite.security.voms.admin.persistence.model.request.Request.STATUS;
import org.glite.security.voms.admin.util.URLBuilder;


public class SubmitMembershipRequest implements Runnable{

	
	
	@Override
	public void run() {

		HibernateFactory.beginTransaction();
		
		RequesterInfo ri = new RequesterInfo();
		ri.setName("Ille ");
		ri.setSurname("Camughe ");
		ri.setEmailAddress("andrea.ceccanti@cnaf.infn.it");
		ri.setInstitution("INFN");
		ri.setCertificateSubject("/C=IT/O=INFN/OU=Personal Certificate/L=CNAF/CN=Andrea Ceccanti");
		ri.setCertificateIssuer("/C=IT/O=INFN/CN=INFN CA");
		
		long requestLifetime = VOMSConfiguration.instance().getLong("voms.request.vo_membership.lifetime",
			300);
	
		Calendar now = Calendar.getInstance();
	
		now.add(Calendar.SECOND,(int) requestLifetime);
	
		Date expirationDate = now.getTime();
		RequestDAO reqDAO = DAOFactory.instance().getRequestDAO();
		NewVOMembershipRequest request = reqDAO.createVOMembershipRequest(ri, 
			expirationDate);
		
		String baseURL = "https://wilco.cnaf.infn.it:8443/voms/mysql";
		
		System.out.println(URLBuilder.buildRequestConfirmURL(baseURL,
			request));
		
		HibernateFactory.commitTransaction();
		
	}
	

	public static void main(String[] args) {

		System.setProperty(VOMSConfigurationConstants.VO_NAME, args[0]);
		VOMSConfiguration.load(null);
		new SubmitMembershipRequest().run();
	}
}
