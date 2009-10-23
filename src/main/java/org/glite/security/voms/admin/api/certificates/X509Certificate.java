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

public class X509Certificate implements Serializable {

	/**
     * 
     */
	private static final long serialVersionUID = 1L;

	long id;

	String subject;
	String issuer;
	String notAfter;
	
	byte[] bytes;

	public byte[] getBytes() {

		return bytes;
	}

	public void setBytes(byte[] bytes) {

		this.bytes = bytes;
	}

	public String getIssuer() {

		return issuer;
	}

	public void setIssuer(String issuer) {

		this.issuer = issuer;
	}

	public String getNotAfter() {

		return notAfter;
	}

	public void setNotAfter(String notAfter) {

		this.notAfter = notAfter;
	}

	public String getSubject() {

		return subject;
	}

	public void setSubject(String subject) {

		this.subject = subject;
	}

	public long getId() {

		return id;
	}

	public void setId(long id) {

		this.id = id;
	}

}
