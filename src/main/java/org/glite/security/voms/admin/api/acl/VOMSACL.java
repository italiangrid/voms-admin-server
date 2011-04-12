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

/**
 * This service defines methods for managing VOMS Admin Access Control Lists (ACL).
 * 
 *  @author <a href="mailto:andrea.ceccanti@cnaf.infn.it">Andrea Ceccanti</a>
 *
 */
public interface VOMSACL {

    	/**
    	 * Returns the ACL for the given container. 
    	 * 
    	 * @param container
    	 * A String representing a VOMS group or qualified role.
    	 * 
    	 * @return
    	 * An array of {@link ACLEntry} objects defining the ACL for the given container
    	 *  
    	 * @throws VOMSException
    	 */
	public ACLEntry[] getACL(String container) throws VOMSException;

	/**
	 * Sets the ACL for a given container.
	 * 
	 * @param container
	 * A String representing a VOMS group or qualified role.
	 * 
	 * @param acl
	 * An array of {@link ACLEntry} objects defining the ACL for the given container
	 * 
	 * @throws VOMSException
	 */
	public void setACL(String container, ACLEntry[] acl) throws VOMSException;

	/**
	 * Adds an entry to the ACL for a given container.
	 * 
	 * @param container
	 * A String representing a VOMS group or qualified role.
	 * 
	 * @param aclEntry
	 * An {@link ACLEntry} object defining the entry to be added to the ACL.
	 * 
	 * @param propagateToChildrenContexts
	 * if <code>true</code>, the entry is propagated also to children context's ACLs. This applies only if the container passed as argument is a VOMS group.
	 *  
	 * @throws VOMSException
	 */
	public void addACLEntry(String container, ACLEntry aclEntry,
			boolean propagateToChildrenContexts) throws VOMSException;

	/**
	 * Removes an entry from the ACL of a given container.
	 * 
	 * @param container
	 * A String representing a VOMS group or qualified role.
	 * 
	 * @param aclEntry
	 * An {@link ACLEntry} object defining the entry to be added to the ACL.
	 * 
	 * @param removeFromChildrenContexts
	 * if <code>true</code>, the entry is removed also from children context's ACLs. This applies only if the container passed as argument is a VOMS group.
	 * 
	 * @throws VOMSException
	 */
	public void removeACLEntry(String container, ACLEntry aclEntry,
			boolean removeFromChildrenContexts) throws VOMSException;

	/** 
	 * Returns the <em>default</em> ACL for a given group. The <em>default</em> ACL can be defined for VOMS groups to override the ACL inherited
	 * by children groups.
	 * 
	 * @param group
	 * A string representing a VOMS group
	 * 
	 * @return
	 * An array of {@link ACLEntry} objects defining the default ACL for the given group.
	 * 
	 * @throws VOMSException
	 */
	public ACLEntry[] getDefaultACL(String group) throws VOMSException;

	
	/**
	 * Sets the <em>default</em> ACL for a given group. The <em>default</em> ACL can be defined for VOMS groups to override the ACL inherited
	 * by children groups.
	 * 
	 * @param group
	 * A string representing a VOMS group
	 * @param acl
	 * An array of {@link ACLEntry} objects defining the default ACL for the given group.
	 * 
	 * @throws VOMSException
	 */
	public void setDefaultACL(String group, ACLEntry[] acl)
			throws VOMSException;

	/**
	 * Adds an entry to the <em>default</em> ACL for a given group.
	 * 
	 * @param group
	 * A string representing a VOMS group.
	 * 
	 * @param aclEntry
	 * An {@link ACLEntry} object defining the entry to be added to the default ACL.
	 * @throws VOMSException
	 */
	public void addDefaultACLEntry(String group, ACLEntry aclEntry)
			throws VOMSException;

	/**
	 * Removes an entry from the <em>default</em> ACL for a given group.
	 * 
	 * @param group
	 * A string representing a VOMS group.
	 * 
	 * @param aclEntry
	 * An {@link ACLEntry} object defining the entry to be removed from the default ACL.
	 * 
	 * @throws VOMSException
	 */
	public void removeDefaultACLEntry(String group, ACLEntry aclEntry)
			throws VOMSException;
}
