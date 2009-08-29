/*******************************************************************************
 *Copyright (c) Members of the EGEE Collaboration. 2006. 
 *See http://www.eu-egee.org/partners/ for details on the copyright
 *holders.  
 *
 *Licensed under the Apache License, Version 2.0 (the "License"); 
 *you may not use this file except in compliance with the License. 
 *You may obtain a copy of the License at 
 *
 *    http://www.apache.org/licenses/LICENSE-2.0 
 *
 *Unless required by applicable law or agreed to in writing, software 
 *distributed under the License is distributed on an "AS IS" BASIS, 
 *WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 *See the License for the specific language governing permissions and 
 *limitations under the License.
 *
 * Authors:
 *     Andrea Ceccanti - andrea.ceccanti@cnaf.infn.it
 *******************************************************************************/

package org.glite.security.voms.admin.model;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.glite.security.voms.admin.database.Auditable;
import org.glite.security.voms.admin.database.NoSuchAttributeException;

/**
 * 
 * @author andrea
 * 
 */
public class VOMSRole implements Serializable, Auditable, Comparable {

	private static final long serialVersionUID = -5063337678658382573L;

	public VOMSRole() {

	}

	public VOMSRole(String name) {

		this.name = name;
	}

	Long id;

	String name;

	Set attributes = new HashSet();

	Set mappings = new TreeSet();

	Set acls = new HashSet();

	Set<TagMapping> tagMappings = new HashSet<TagMapping>();

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

	/**
	 * @return Returns the name.
	 */
	public String getName() {

		return name;
	}

	/**
	 * @param name
	 *            The name to set.
	 */
	public void setName(String name) {

		this.name = name;
	}

	public boolean equals(Object other) {

		if (this == other)
			return true;

		if (!(other instanceof VOMSRole))
			return false;

		VOMSRole that = (VOMSRole) other;

		if (that == null)
			return false;

		return getName().equals(that.getName());
	}

	public int hashCode() {

		return getName().hashCode();
	}

	public int compareTo(Object o) {

		if (this.equals(o))
			return 0;
		VOMSRole that = (VOMSRole) o;

		if ((that.name == null) && (this.name == null))
			return 1;

		if ((that.name == null) || (this.name == null))
			return -1;

		return this.name.compareTo(that.name);
	}

	public String toString() {

		return "Role=" + name;
	}

	public Set getMappings() {

		return mappings;
	}

	public void setMappings(Set mappings) {

		this.mappings = mappings;
	}

	public Set getUsers(VOMSGroup g) {

		SortedSet res = new TreeSet();
		Iterator mIter = mappings.iterator();

		while (mIter.hasNext()) {

			VOMSMapping m = (VOMSMapping) mIter.next();
			if (m.getGroup().equals(g))
				res.add(m.getUser());
		}

		return Collections.unmodifiableSortedSet(res);
	}

	public Set getMembersEmailAddresses(VOMSGroup g) {

		SortedSet res = new TreeSet();
		Iterator mIter = mappings.iterator();

		while (mIter.hasNext()) {

			VOMSMapping m = (VOMSMapping) mIter.next();
			if (m.getGroup().equals(g))
				res.add(m.getUser().getEmailAddress());
		}

		return Collections.unmodifiableSortedSet(res);
	}

	public boolean isAssignedTo(VOMSGroup g, VOMSUser u) {

		if (g == null)
			throw new IllegalArgumentException("g must not be null!");

		if (u == null)
			throw new IllegalArgumentException("u must not be null!");

		VOMSMapping m = new VOMSMapping(u, g, this);
		return mappings.contains(m);
	}

	public VOMSRoleAttribute getAttributeByName(VOMSGroup g, String attrName) {

		Iterator i = attributes.iterator();
		while (i.hasNext()) {
			VOMSRoleAttribute rav = (VOMSRoleAttribute) i.next();
			if (rav.getGroup().equals(g) && rav.getName().equals(attrName))
				return rav;
		}

		return null;
	}

	public Set getAttributesInGroup(VOMSGroup g) {

		HashSet result = new HashSet();
		Iterator i = attributes.iterator();

		while (i.hasNext()) {
			VOMSRoleAttribute rav = (VOMSRoleAttribute) i.next();
			if (rav.getGroup().equals(g))
				result.add(rav);
		}

		return result;

	}

	public void addAttribute(VOMSRoleAttribute val) {

		attributes.add(val);

	}

	public void deleteAttribute(VOMSRoleAttribute val) {

		if (!attributes.contains(val))
			throw new NoSuchAttributeException("Attribute \"" + val.getName()
					+ "\" not defined for \"" + this + "\" in group \""
					+ val.getGroup() + "\".");

		attributes.remove(val);

	}

	public Set getAttributes() {

		return attributes;
	}

	public void setAttributes(Set attributes) {

		this.attributes = attributes;
	}

	public Set getAcls() {

		return acls;
	}

	public void setAcls(Set acls) {

		this.acls = acls;
	}

	public ACL getACL(VOMSGroup g) {

		ACL result = null;

		Iterator i = getAcls().iterator();

		while (i.hasNext()) {

			ACL tmp = (ACL) i.next();
			if (tmp.getGroup().equals(g)
					&& (!tmp.getContext().isGroupContext())) {

				result = tmp;
				break;
			}
		}

		return result;

	}

	public void importACL(VOMSGroup g) {

		ACL groupACL;

		if (g.getDefaultACL() == null)
			groupACL = g.getACL();
		else
			groupACL = g.getDefaultACL();

		ACL newACL = new ACL(g, this, false);

		newACL.getPermissions().putAll(groupACL.getPermissions());

		getAcls().add(newACL);

	}

	/**
	 * @return the tagMappings
	 */
	public Set<TagMapping> getTagMappings() {

		return tagMappings;
	}

	/**
	 * @param tagMappings
	 *            the tagMappings to set
	 */
	public void setTagMappings(Set<TagMapping> tagMappings) {

		this.tagMappings = tagMappings;
	}

}
