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
package org.glite.security.voms.admin.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.glite.security.voms.admin.common.VOMSServiceConstants;
import org.glite.security.voms.admin.common.NullArgumentException;
import org.glite.security.voms.admin.common.PathNamingScheme;
import org.glite.security.voms.admin.common.VOMSSyntaxException;
import org.glite.security.voms.admin.database.AlreadyExistsException;
import org.glite.security.voms.admin.database.Auditable;
import org.glite.security.voms.admin.model.task.Task;
import org.glite.security.voms.admin.operations.VOMSContext;

public class VOMSAdmin implements Serializable, Auditable, Cloneable {

	private static Log log = LogFactory.getLog(VOMSAdmin.class);

	/**
     * 
     */
	private static final long serialVersionUID = -5459874418491929253L;

	Long id;

	String dn;

	VOMSCA ca;

	String emailAddress;

	Set<TagMapping> tagMappings = new HashSet<TagMapping>();

	Set<Task> tasks = new HashSet<Task>();

	public VOMSAdmin() {

	}

	/**
	 * @return Returns the ca.
	 */
	public VOMSCA getCa() {

		return ca;
	}

	/**
	 * @param ca
	 *            The ca to set.
	 */
	public void setCa(VOMSCA ca) {

		this.ca = ca;
	}

	/**
	 * @return Returns the dn.
	 */
	public String getDn() {

		return dn;
	}

	/**
	 * @param dn
	 *            The dn to set.
	 */
	public void setDn(String dn) {

		this.dn = dn;
	}

	/**
	 * @return Returns the id.
	 */
	public Long getId() {

		return id;
	}

	/**
	 * @param id
	 *            The id to set.
	 */
	public void setId(Long id) {

		this.id = id;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object other) {

		if (this == other)
			return true;

		if (!(other instanceof VOMSAdmin))
			return false;

		if (other == null)
			return false;

		final VOMSAdmin that = (VOMSAdmin) other;

		return (getDn().equals(that.getDn()));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {

		return getDn().hashCode();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#clone()
	 */
	public Object clone() throws CloneNotSupportedException {

		VOMSAdmin newInstance = (VOMSAdmin) super.clone();
		newInstance.id = id;
		newInstance.dn = dn;
		newInstance.ca = ca;
		return newInstance;
	}

	/* Service methods */

	public String toString() {

		ToStringBuilder builder = new ToStringBuilder(this);

		return builder.append(dn).append(ca.getSubjectString()).toString();

	}

	public String getEmailAddress() {

		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {

		this.emailAddress = emailAddress;
	}

	public boolean isInternalAdmin() {
		return getCa().getSubjectString().startsWith(
				VOMSServiceConstants.INTERNAL_DN_PREFIX);
	}

	public boolean isUnauthenticated(){
		
		return (getDn().equals(VOMSServiceConstants.UNAUTHENTICATED_CLIENT)
				&& 
				getCa().getSubjectString().equals(VOMSServiceConstants.VIRTUAL_CA));
	}
	public boolean isGroupAdmin() {

		boolean result;
		try {

			result = PathNamingScheme.isGroup(getDn());

		} catch (VOMSSyntaxException e) {

			return false;
		}

		return result;
	}

	public boolean isRoleAdmin() {
		boolean result;

		try {

			result = PathNamingScheme.isQualifiedRole(getDn());

		} catch (VOMSSyntaxException e) {

			return false;
		}
		return result;
	}

	/**
	 * @return the mappings
	 */
	public Set<TagMapping> getTagMappings() {

		return tagMappings;
	}

	/**
	 * @param mappings
	 *            the mappings to set
	 */
	public void setTagMappings(Set<TagMapping> mappings) {

		this.tagMappings = mappings;
	}

	public Set<Tag> getTagsInContext(VOMSContext c) {

		if (c.isGroupContext())
			return getTagsInGroup(c.getGroup());
		else
			return getTagsInRole(c.getGroup(), c.getRole());

	}

	public Set<Tag> getTagsInGroup(VOMSGroup g) {

		Set<Tag> result = new HashSet<Tag>();

		for (TagMapping m : tagMappings) {

			if (m.getAdmin().equals(this) && m.getGroup().equals(g))
				result.add(m.getTag());
		}

		return result;
	}

	public Set<Tag> getTagsInRole(VOMSGroup g, VOMSRole r) {

		Set<Tag> result = new HashSet<Tag>();

		for (TagMapping m : tagMappings) {

			if (!m.isGroupMapping())
				continue;

			if (m.getAdmin().equals(this) && m.getGroup().equals(g)
					&& m.getRole().equals(r))
				result.add(m.getTag());
		}

		return result;
	}

	public void assignTagInGroup(VOMSGroup group, Tag tag) {

		if (group == null)
			throw new NullArgumentException("group cannot be null!");

		if (tag == null)
			throw new NullArgumentException("tag cannot be null!");

		TagMapping m = new TagMapping(tag, group, null, this);

		if (!getTagMappings().add(m))
			throw new AlreadyExistsException("Admin '" + this
					+ "' already has tag '" + tag + "' in group '" + group
					+ "'");

	}

	public void assignTagInRole(VOMSGroup group, VOMSRole role, Tag tag) {

		if (group == null)
			throw new NullArgumentException("group cannot be null!");

		if (tag == null)
			throw new NullArgumentException("tag cannot be null!");

		if (role == null)
			throw new NullArgumentException("role cannot be null!");

		TagMapping m = new TagMapping(tag, group, role, this);

		if (!getTagMappings().add(m))
			throw new AlreadyExistsException("Admin '" + this
					+ "' already has tag '" + tag + "' in group '" + group
					+ "'");

	}

	/**
	 * @return the tasks
	 */
	public Set<Task> getTasks() {

		return tasks;
	}

	/**
	 * @param tasks
	 *            the tasks to set
	 */
	public void setTasks(Set<Task> tasks) {

		this.tasks = tasks;
	}

}
