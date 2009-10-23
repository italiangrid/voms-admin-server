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

import org.glite.security.voms.admin.api.VOMSException;

public interface VOMSACL {

	public ACLEntry[] getACL(String container) throws VOMSException;

	public void setACL(String container, ACLEntry[] acl) throws VOMSException;

	public void addACLEntry(String container, ACLEntry aclEntry,
			boolean propagateToChildrenContexts) throws VOMSException;

	public void removeACLEntry(String container, ACLEntry aclEntry,
			boolean removeFromChildrenContexts) throws VOMSException;

	public ACLEntry[] getDefaultACL(String group) throws VOMSException;

	public void setDefaultACL(String group, ACLEntry[] acl)
			throws VOMSException;

	public void addDefaultACLEntry(String group, ACLEntry aclEntry)
			throws VOMSException;

	public void removeDefaultACLEntry(String group, ACLEntry aclEntry)
			throws VOMSException;
}
