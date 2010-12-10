package org.glite.security.voms.admin.test;

import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;

import javax.xml.rpc.ServiceException;

import org.glite.security.voms.VOMSException;
import org.glite.security.voms.service.admin.VOMSAdminServiceLocator;

public class CSRFAxisTestClient {

    private static final String HOST = "wilco.cnaf.infn.it";
    private static final String VO = "mysql";
    
    public CSRFAxisTestClient() throws VOMSException, RemoteException, ServiceException, MalformedURLException {
	
	String url = String.format( "https://%s:8443/voms/%s/services/VOMSAdmin", HOST, VO);
	
	VOMSAdminServiceLocator loc = new VOMSAdminServiceLocator();
	
	loc.getVOMSAdmin( new URL(url)).listRoles();
    }
    
    
    
    /**
     * @param args
     * @throws ServiceException 
     * @throws RemoteException 
     * @throws VOMSException 
     * @throws MalformedURLException 
     */
    public static void main(String[] args) throws VOMSException, RemoteException, ServiceException, MalformedURLException {
	
	new CSRFAxisTestClient();
    }

}
