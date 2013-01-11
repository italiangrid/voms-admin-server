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

package it.infn.cnaf.voms.test.saml_client;

import it.infn.cnaf.voms.saml.axis_skeletons.AttributeAuthorityPortType;

import java.rmi.RemoteException;

import junit.extensions.TestSetup;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.glite.security.voms.User;
import org.glite.security.voms.VOMSException;
import org.glite.security.voms.service.admin.VOMSAdmin;
import org.glite.security.voms.service.attributes.VOMSAttributes;
import org.opensaml.DefaultBootstrap;
import org.opensaml.saml2.core.AttributeQuery;
import org.opensaml.saml2.core.Response;
import org.opensaml.saml2.core.StatusCode;

public class SAMLEndpointTest extends TestCase {
	
	public static final String DEFAULT_HOST = "localhost";
	public static final String DEFAULT_VO = "mysql";
	
	public static final String DEFAULT_DN = "/C=IT/O=INFN/OU=Personal Certificate/L=CNAF/CN=Andrea Ceccanti";
	public static final String DEFAULT_CA = "/C=IT/O=INFN/CN=INFN CA";
	public static final String DEFAULT_EMAIL = "andrea.ceccanti@cnaf.infn.it";
	
	static String host = System.getProperty("samltest.host", DEFAULT_HOST);
	static String vo = System.getProperty("samltest.vo", DEFAULT_VO);
	static String dn = System.getProperty("samltest.user_dn", DEFAULT_DN);
	static String ca = System.getProperty("samltest.user_ca", DEFAULT_CA);
	static String email = System.getProperty("samltest.user_email", DEFAULT_EMAIL);
	
	static boolean verbose = Boolean.parseBoolean(System.getProperty("samltest.verbose", "true"));
		
	static AttributeAuthorityPortType samlEndpoint = null;
	static VOMSAdmin adminEndpoint = null;
	static VOMSAttributes attributesEndpoint = null;
	
	static String USER_NOT_FOUND_MESSAGE = "User identified by '%s' not found!";
	
	
	protected boolean hasStatusCode(Response r, String statusCode){
		
		String responseCode = r.getStatus().getStatusCode().getValue();
		
		return statusCode.equals(responseCode);
		
		
	}
		
	protected boolean hasStatusMessage(Response r, String message){
	
		String statusMessage = r.getStatus().getStatusMessage().getMessage(); 
		
		return message.equals(statusMessage);
		
	}
	
	
	protected Response doQuery(AttributeQuery q) throws Exception{
		
		if (verbose){
			System.out.println("Attribute query: ");
			SAMLTestUtils.printXMLObject(q);
		}
		
		Response r = samlEndpoint.attributeQuery(q);
		
		if (verbose){
			System.out.println("Response: ");
			SAMLTestUtils.printXMLObject(r);
		}
		
		return r;
		
	}
	
	
	public void testUserNotInVOFailure() throws Exception{
		
		Response r = doQuery(SAMLTestUtils.buildAttributeQuery(dn, vo));
		
		assertTrue(hasStatusCode(r, StatusCode.RESPONDER_URI));
		assertTrue(hasStatusMessage(r, String.format(USER_NOT_FOUND_MESSAGE, dn) ));	
		
	}
	
	
	public void testEmptyQuery() throws Exception{
		
		createTestUser();
		
		Response r = doQuery(SAMLTestUtils.buildAttributeQuery(dn, vo));
		
		assertTrue(hasStatusCode(r, StatusCode.SUCCESS_URI));
		
		dropTestUser();
		
		
		
	}
	
	protected void createTestUser() throws VOMSException, RemoteException{
		
		User u = new User();
		
		u.setDN(dn);
		u.setCA(ca);
		u.setMail(email);
		
		adminEndpoint.createUser(u);
		
	}
	
	protected void dropTestUser() throws VOMSException, RemoteException{
		
		adminEndpoint.deleteUser(dn, ca);
		
	}
	
	public static Test suite(){
		
		// Bootstrap OpenSAML only once for the whole test.
		return new TestSetup(new TestSuite(SAMLEndpointTest.class)){
			@Override
			protected void setUp() throws Exception {
				DefaultBootstrap.bootstrap();
				
				samlEndpoint = SAMLTestUtils.getVOMSSAMLService(host, vo);
				adminEndpoint = SAMLTestUtils.getVOMSAdminService(host, vo);
				attributesEndpoint = SAMLTestUtils.getVOMSAttributesService(host, vo);
				
				
			}
			
		
		};
	}

}