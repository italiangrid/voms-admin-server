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
