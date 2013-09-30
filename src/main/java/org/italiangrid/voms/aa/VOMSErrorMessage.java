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

package org.italiangrid.voms.aa;



public class VOMSErrorMessage {

	private final VOMSError error;
	private String message;
		
	private VOMSErrorMessage(VOMSError e, String message) {
		this.error = e;
		this.message = message;
	}
	
	private VOMSErrorMessage(VOMSError e) {
		this(e, null);
	}

	public String getMessage() {
		if (message == null)
			return error.getDefaultMessage();
		
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public VOMSError getError() {	
		return error;
	}

	@Override
	public String toString() {
		return String.format("[%s] %s", error.name(), 
			(message == null) ? error.getDefaultMessage() : message);
	}
	
	public static VOMSErrorMessage noSuchUser(String userDN, String userCA){
		VOMSErrorMessage m = new VOMSErrorMessage(VOMSError.NoSuchUser);
		m.setMessage(String.format("No such user: subject='%s', issuer='%s'", 
			userDN, userCA));
		return m;
	}
	
	public static VOMSErrorMessage noSuchAttribute(String fqan){
		VOMSErrorMessage m = new VOMSErrorMessage(VOMSError.NoSuchAttribute);
		m.setMessage(String.format(
			"User is not authorized to request attribute '%s' or attribute does " +
			"not exist.", fqan));
		return m;
	}
	
	public static VOMSErrorMessage suspendedUser(String userDN, 
		String userCA, 
		String suspensionReason){
		VOMSErrorMessage m = new VOMSErrorMessage(VOMSError.SuspendedUser);
		m.setMessage(String.format("User '%s, %s' is currently suspended. Reason: %s",
			userDN, userCA, suspensionReason));
		return m;
	}
	
	public static VOMSErrorMessage suspendedCertificate(String certSubject, 
		String certIssuer, 
		String suspensionReason){
		VOMSErrorMessage m = new VOMSErrorMessage(VOMSError.SuspendedCertificate);
		m.setMessage(String.format("Certificate '%s, %s' is currently suspended. Reason: %s",
			certSubject, certIssuer, suspensionReason));
		return m;
	}
	
	public static VOMSErrorMessage internalError(String message){
		VOMSErrorMessage m = new VOMSErrorMessage(VOMSError.InternalError);
		m.setMessage(message);
		return m;
	}
	
	public static VOMSErrorMessage endpointDisabled(){
		VOMSErrorMessage m = new VOMSErrorMessage(VOMSError.EndpointDisabled);
		return m;
	}
	
	public static VOMSErrorMessage unauthenticatedClient(){
		VOMSErrorMessage m = new VOMSErrorMessage(VOMSError.UnauthenticatedClient);
		return m;
	}
}
