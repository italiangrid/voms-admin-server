package org.glite.security.voms.admin.test;

import java.util.Date;

import org.glite.security.voms.admin.dao.generic.RequestDAO;
import org.glite.security.voms.admin.dao.hibernate.HibernateDAOFactory;
import org.glite.security.voms.admin.database.HibernateFactory;
import org.glite.security.voms.admin.model.request.RequesterInfo;


public class TestRequests {
	
	
	public void createRequest(String dn, String ca, String email){
		
	}

    public void testCreateVOMembershipRequest(){
        
    	
    	HibernateFactory.beginTransaction();
    	
        RequesterInfo ri = new RequesterInfo();
        
        ri.setCertificateSubject( TestUtils.myDn );
        ri.setCertificateIssuer( TestUtils.myCA );
        ri.setEmailAddress( TestUtils.myEmail );
        
        ri.setVoMember( false );
        
        RequestDAO rDAO = HibernateDAOFactory.instance().getRequestDAO();
        
        rDAO.createVOMembershipRequest( ri, new Date() );
        
        HibernateFactory.commitTransaction();
        
    }
    
    public TestRequests() {

        testCreateVOMembershipRequest();
    }
    
    
    /**
     * @param args
     */
    public static void main( String[] args ) {
        
        TestUtils.configureLogging();
        TestUtils.setupVOMSConfiguration();
        TestUtils.setupVOMSDB();

        new TestRequests();
        
        System.exit(0);

    }

}
