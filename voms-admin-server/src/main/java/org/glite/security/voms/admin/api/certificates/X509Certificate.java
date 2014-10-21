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

import java.io.Serializable;

/** 
 * This class models X.509 certificate information as stored in the VOMS database.
 *
 */
public class X509Certificate implements Serializable {

	/**
     * 
     */
	private static final long serialVersionUID = 1L;

	/**
	 * The certificate id.
	 */
	long id;

	/**
	 * The certificate subject.
	 */
	String subject;
	
	/**
	 * The certificate issuer subject.
	 */
	String issuer;
	
	/**
	 * The certificate expiration date (deprecated).
	 */
	String notAfter;
	
	/**
	 * The certificate binary representation.
	 */
	byte[] bytes;

	/**
	 * Returns the certificate bytes.
	 * @return
	 */
	public byte[] getBytes() {

		return bytes;
	}

	/** 
	 * Sets the bytes for this certificate.
	 * 
	 * @param bytes
	 */
	public void setBytes(byte[] bytes) {

		this.bytes = bytes;
	}

	/**
	 * Returns the certificate issuer subject.
	 * 
	 * @return
	 */
	public String getIssuer() {

		return issuer;
	}

	/**
	 * Sets the certificate issuer subject.
	 * 
	 * @param issuer
	 */
	public void setIssuer(String issuer) {

		this.issuer = issuer;
	}

	/** 
	 * Returns the certificate expiration date (as a string)
	 * @return
	 */
	public String getNotAfter() {

		return notAfter;
	}

	/**
	 * Sets the certificate expiration date (as a string)
	 * 
	 * @param notAfter
	 */
	public void setNotAfter(String notAfter) {

		this.notAfter = notAfter;
	}

	/** 
	 * Returns the certificate subject.
	 * 
	 * @return
	 */
	public String getSubject() {

		return subject;
	}

	/**
	 * Sets the certificate subject.
	 * 
	 * @param subject
	 */
	public void setSubject(String subject) {

		this.subject = subject;
	}

	/**
	 * Returns the certificate id.
	 * 
	 * @return
	 */
	public long getId() {

		return id;
	}

	/**
	 * Sets the certificate id.
	 * @param id
	 */
	public void setId(long id) {

		this.id = id;
	}

}
