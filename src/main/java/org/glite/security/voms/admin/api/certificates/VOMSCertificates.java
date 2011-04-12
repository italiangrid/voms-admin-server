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
package org.glite.security.voms.admin.api.certificates;
/**
 * This service defines methods to manage certificates linked to a VOMS membership.
 * 
 *  @author <a href="mailto:andrea.ceccanti@cnaf.infn.it">Andrea Ceccanti</a>
 *
 */
public interface VOMSCertificates {

    	
	public long getUserIdFromDn(String dn, String ca);

	/**
	 * Adds a certificate to a VOMS membership.
	 * 
	 * @param userId
	 * The VOMS user id. 
	 * 
	 * @param cert
	 * The certificate to be added.
	 */
	public void addCertificate(long userId, X509Certificate cert);

	/**
	 * Adds a certificate to a VOMS membership, identified by an X.509 certificate.
	 * 
	 * @param registeredCertSubject
	 * The subject of a certificate already bound to a VOMS user.
	 * 
	 * @param registeredCertIssuer
	 * The issuer of a certificate already bound to a VOMS user.
	 * 
	 * @param cert
	 * The certificate to be added to the VOMS membership.
	 */
	public void addCertificate(String registeredCertSubject,
			String registeredCertIssuer, X509Certificate cert);

	/**
	 * Returns the certificates currently bound to a given VOMS user.
	 * 
	 * @param userId
	 * The VOMS user id.
	 * 
	 * @return
	 * An array of {@link X509Certificate} objects representing the certificates registered for the user
	 */
	public X509Certificate[] getCertificates(long userId);

	/**
	 * Returns the certificates currently bound to a given VOMS user, identified by an X.509 certificate.
	 * 
	 * @param registeredCertSubject
	 * The subject of a certificate already bound to a VOMS user.
	 * 
	 * @param registeredCertIssuer
	 * The issuer of a certificate already bound to a VOMS user.
	 * 
	 * @return
	 * An array of {@link X509Certificate} objects representing the certificates registered for the user
	 */
	public X509Certificate[] getCertificates(String registeredCertSubject,
			String registeredCertIssuer);

	/**
	 * Suspends a VOMS user certificate for a given reason. Suspended certificates cannot be used to obtain VOMS attributes.
	 *  
	 * @param cert
	 * The {@link X509Certificate} to be suspended.
	 * 
	 * @param reason
	 * A string containing a suspension reason. 
	 */
	public void suspendCertificate(X509Certificate cert, String reason);
	
	/**
	 * Restores a suspended VOMS user certificate. Suspended certificates cannot be used to obtain VOMS attributes.
	 * 
	 * @param cert
	 * The {@link X509Certificate} to be restored.
	 */
	public void restoreCertificate(X509Certificate cert);

	/**
	 * Removes a certificate from a given VOMS membership.
	 * 
	 * @param cert
	 * The {@link X509Certificate} to be removed.
	 */
	public void removeCertificate(X509Certificate cert);

}
