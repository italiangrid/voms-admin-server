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
package org.glite.security.voms.admin.api.registration;

import org.glite.security.voms.admin.api.VOMSException;
/**
 * This service defines methods to submit VOMS membership requests.
 * 
 *  @author <a href="mailto:andrea.ceccanti@cnaf.infn.it">Andrea Ceccanti</a>
 *
 */
public interface VOMSRegistration {

    	/**
    	 * Submits a registration request. User certificate information is taken from the security context.
    	 * @param req
    	 * The {@link RegistrationRequest} object
    	 * 
    	 * @throws VOMSException
    	 */
	public void submitRegistrationRequest(RegistrationRequest req)
			throws VOMSException;

	/**
	 * Submits a registration request for a given user certificate.
	 *  
	 * @param userSubject
	 * The user certificate subject for which a registration request is submitted.
	 * 
	 * @param caSubject
	 * The user certificate issuer subject for which a registration request is submitted.
	 * 
	 * @param req
	 * The {@link RegistrationRequest} object describing the registration subject.
	 * 
	 * @throws VOMSException
	 */
	public void submitRegistrationRequestForUser(String userSubject,
			String caSubject, RegistrationRequest req) throws VOMSException;

}
