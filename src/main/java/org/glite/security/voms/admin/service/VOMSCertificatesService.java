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
package org.glite.security.voms.admin.service;

import java.rmi.RemoteException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.glite.security.voms.admin.error.NullArgumentException;
import org.glite.security.voms.admin.operations.users.AddUserCertificateOperation;
import org.glite.security.voms.admin.operations.users.FindUserOperation;
import org.glite.security.voms.admin.operations.users.RemoveUserCertificateOperation;
import org.glite.security.voms.admin.operations.users.RestoreUserCertificateOperation;
import org.glite.security.voms.admin.operations.users.SuspendUserCertificateOperation;
import org.glite.security.voms.admin.persistence.error.NoSuchUserException;
import org.glite.security.voms.admin.persistence.model.VOMSUser;
import org.glite.security.voms.service.certificates.VOMSCertificates;
import org.glite.security.voms.service.certificates.X509Certificate;

public class VOMSCertificatesService implements VOMSCertificates {

	private static final Log log = LogFactory
			.getLog(VOMSCertificatesService.class);

	public void addCertificate(long userId, X509Certificate cert)
			throws RemoteException {

		try {
			if (cert == null)
				throw new NullArgumentException(
						"X509Certificate cannot be null!");

			if (userId < 0)
				throw new IllegalArgumentException(
						"the userId must be a positive integer!");

			VOMSUser user = (VOMSUser) FindUserOperation.instance(userId)
					.execute();

			if (cert.getBytes() != null) {

				java.security.cert.X509Certificate x509Cert = ServiceUtils
						.certificateFromBytes(cert.getBytes());
				AddUserCertificateOperation.instance(user, x509Cert).execute();

			} else
				AddUserCertificateOperation.instance(user, cert.getSubject(),
						cert.getIssuer(), cert.getNotAfter()).execute();

		} catch (RuntimeException e) {

			ServiceExceptionHelper.handleServiceException(log, e);

			throw e;

		}
	}

	public X509Certificate[] getCertificates(long userId)
			throws RemoteException {

		try {

			VOMSUser u = (VOMSUser) FindUserOperation.instance(userId)
					.execute();
			return ServiceUtils.toX509CertificateArray(u.getCertificates());

		} catch (RuntimeException e) {

			ServiceExceptionHelper.handleServiceException(log, e);

			throw e;

		}
	}

	public X509Certificate[] getCertificates(String subject, String issuer)
			throws RemoteException {

		try {

			VOMSUser u = (VOMSUser) FindUserOperation.instance(subject, issuer)
					.execute();

			if (u == null)
				throw new NoSuchUserException(String.format("No '%s,%s' user found in this VO", subject,issuer));
			
			return ServiceUtils.toX509CertificateArray(u.getCertificates());

		} catch (RuntimeException e) {
			ServiceExceptionHelper.handleServiceException(log, e);

			throw e;
		}

	}

	public long getUserIdFromDn(String subject, String issuer)
			throws RemoteException {

		try {
			if (subject == null)
				throw new NullArgumentException(
						"User's subject cannot be null!");

			if (issuer == null)
				throw new NullArgumentException("User's issuer cannot be null!");

			VOMSUser u = (VOMSUser) FindUserOperation.instance(subject, issuer)
					.execute();

			if (u == null)
				return -1;

			return u.getId().longValue();

		} catch (RuntimeException e) {
			ServiceExceptionHelper.handleServiceException(log, e);

			throw e;

		}
	}

	public void addCertificate(String registeredCertSubject,
			String registeredCertIssuer, X509Certificate cert)
			throws RemoteException {

		try {
			long userId = getUserIdFromDn(registeredCertSubject,
					registeredCertIssuer);

			addCertificate(userId, cert);

		} catch (RuntimeException e) {
			ServiceExceptionHelper.handleServiceException(log, e);

			throw e;

		}
	}

	public void removeCertificate(X509Certificate cert) throws RemoteException {

		try {
			if (cert.getBytes() != null) {

				java.security.cert.X509Certificate x509Cert = ServiceUtils
						.certificateFromBytes(cert.getBytes());
				RemoveUserCertificateOperation.instance(x509Cert).execute();

			} else
				RemoveUserCertificateOperation.instance(cert.getSubject(),
						cert.getIssuer()).execute();
		} catch (RuntimeException e) {
			ServiceExceptionHelper.handleServiceException(log, e);
			throw e;
		}

	}

	public void suspendCertificate(X509Certificate cert, String reason)
			throws RemoteException {
		try {
			SuspendUserCertificateOperation.instance(cert.getSubject(),
					cert.getIssuer(), reason).execute();
		} catch (RuntimeException e) {
			ServiceExceptionHelper.handleServiceException(log, e);
			throw e;
		}

	}

	public void restoreCertificate(X509Certificate cert) 
			throws RemoteException {
		
		try{
			RestoreUserCertificateOperation.instance(cert.getSubject(), cert.getIssuer()).execute();
			
		} catch (RuntimeException e) {
			ServiceExceptionHelper.handleServiceException(log, e);
			throw e;
		}
		
		
	}

}
