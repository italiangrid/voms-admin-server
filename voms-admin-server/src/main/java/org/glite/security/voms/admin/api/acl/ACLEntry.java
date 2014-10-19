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
package org.glite.security.voms.admin.api.acl;

import java.io.Serializable;

import org.glite.security.voms.admin.operations.VOMSPermission;
/**
 * This class models an ACL entry in the VOMS database. It defines the mapping between an administrator identified by an X509 certificate and a set
 * of permission bits, as defined in the {@link VOMSPermission} class.
 * 
 * 
 * @author andreaceccanti
 *
 */
public class ACLEntry implements Serializable {

	/**
     * 
     */
	private static final long serialVersionUID = 1L;

	/**
	 * The administrator X509 certificate subject.
	 */
	String adminSubject;
	
	/**
	 * The administrator X509 certificate issuer subject.
	 */
	String adminIssuer;

	/**
	 * The permissions bits.
	 */
	int vomsPermissionBits = 0;

	
	/**
	 * Returns the administrator X509 certificate issuer subject.
	 * 
	 * @return
	 * A string representing the the administrator X509 certificate issuer subject.
	 */
	public String getAdminIssuer() {

		return adminIssuer;
	}

	/**
	 * Sets the administrator X509 certificate issuer subject.
	 * 
	 * @param adminIssuer
	 * A string representing the the administrator X509 certificate issuer subject.
	 */
	public void setAdminIssuer(String adminIssuer) {

		this.adminIssuer = adminIssuer;
	}

	/**
	 * Returns the administrator X509 certificate subject.
	 * 
	 * @return
	 * A string representing the the administrator X509 certificate subject.
	 */
	public String getAdminSubject() {

		return adminSubject;
	}

	/**
	 * Sets the administrator X509 certificate subject.
	 * 
	 * @param adminSubject
	 * A string representing the the administrator X509 certificate subject.
	 */
	public void setAdminSubject(String adminSubject) {

		this.adminSubject = adminSubject;
	}

	/**
	 * Returns the VOMS permission bits for this ACL entry.
	 * 
	 * @return
	 * an integer representing the VOMS permission bits
	 */
	public int getVomsPermissionBits() {

		return vomsPermissionBits;
	}

	/**
	 * Sets the VOMS permissions bits for this ACL entry.
	 * 
	 * @param vomsPermissionBits
	 * an integer representing the VOMS permission bits
	 */
	public void setVomsPermissionBits(int vomsPermissionBits) {

		this.vomsPermissionBits = vomsPermissionBits;
	}

}
