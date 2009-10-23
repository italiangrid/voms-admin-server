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
package org.glite.security.voms.admin.api;

import org.glite.security.voms.admin.api.acl.VOMSACL;

/**
 * Represents access control list entries within the VOMS database.
 * 
 * <p>
 * Access control lists (ACLs) provide authorization information within the VOMS
 * database. They consists of a list of principal-operation-allow/deny triplets
 * called ACL entries. An entry allows or denies an operation to a client
 * principal based on its third element. A client is allowed to perform an
 * operation if she has no matching deny entry but at least one allow entry in
 * the relevant ACL.
 * 
 * <p>
 * The principal of an ACL entry may be a VO group or role in this or in another
 * VO, in which case that entry matches a set of clients instead of a single
 * individual client.
 * 
 * <p>
 * The following operations are defined: <code>CREATE</code>,
 * <code>DELETE</code>, <code>ADD</code>, <code>REMOVE</code>,
 * <code>SET_ACL</code>, <code>GET_ACL</code>, <code>SET_DEFAULT_ACL</code>,
 * <code>GET_DEFAULT_ACL</code>, <code>LIST</code>, plus a special wildcard
 * operation <code>ALL</code>, which is a shorthand for all operations.
 * 
 * @author <a href="mailto:Akos.Frohner@cern.ch">Akos Frohner</a>
 * @deprecated Starting from VOMS Admin 2, this interface for ACL management is
 *             deprecated an no longer understood. Use the ACL management
 *             interface defined in {@link VOMSACL}.
 */
public class ACLEntry {

	/** Empty public constructor. */
	public ACLEntry() {
	}

	/** Returns the principal's DN for this ACL entry. */
	public String getAdminDN() {
		return null;
	};

	/** Returns the principal's CA for this ACL entry. */
	public String getAdminCA() {
		return null;
	};

	/** Sets the principal's DN for this ACL entry. */
	public void setAdminDN(String dn) {
	}

	/** Sets the principal's CA for this ACL entry. */
	public void setAdminCA(String ca) {
	}

	/** Returns the operation field of this ACL entry. */
	public String getOperationName() {
		return null;
	};

	/** Sets the operation field of this ACL entry. */
	public void setOperationName(String operation) {
	}

	/** Returns the allow field of this ACL entry. */
	public boolean isAllow() {
		return false;
	}

	/** Sets the allow field of this ACL entry. */
	public void setAllow(boolean allow) {
	}

}

// Please do not change this line.
// arch-tag: 6efdd02a-3936-4b69-9035-32b016dfb2ae

