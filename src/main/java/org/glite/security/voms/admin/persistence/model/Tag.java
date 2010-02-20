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
package org.glite.security.voms.admin.persistence.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import org.glite.security.voms.admin.operations.VOMSPermission;

public class Tag implements Serializable {

	/**
     * 
     */
	private static final long serialVersionUID = 1L;

	private Long id;

	private String name;

	private VOMSPermission permissions;

	private VOMSPermission permissionsOnPath;

	private VOMSPermission voPermissions;

	private Set<TagMapping> mappings = new HashSet<TagMapping>();

	private Boolean implicit;

	public Tag() {

	}

	public Tag(String name, VOMSPermission perm, VOMSPermission permOnPath,
			boolean implicit) {

		this.name = name;
		this.permissions = perm;
		this.permissionsOnPath = permOnPath;
		this.implicit = implicit;
	}

	public Long getId() {

		return id;
	}

	public void setId(Long id) {

		this.id = id;
	}

	public String getName() {

		return name;
	}

	public void setName(String name) {

		this.name = name;
	}

	public VOMSPermission getPermissions() {
		return permissions;
	}

	public void setPermissions(VOMSPermission permissions) {
		this.permissions = permissions;
	}

	public VOMSPermission getPermissionsOnPath() {
		return permissionsOnPath;
	}

	public void setPermissionsOnPath(VOMSPermission permissionsOnPath) {
		this.permissionsOnPath = permissionsOnPath;
	}

	/**
	 * @return the mappings
	 */
	public Set<TagMapping> getMappings() {

		return mappings;
	}

	/**
	 * @param mappings
	 *            the mappings to set
	 */
	public void setMappings(Set<TagMapping> mappings) {

		this.mappings = mappings;
	}

	public boolean isImplicit() {

		return getImplicit();
	}

	/**
	 * @return the implicit
	 */
	public Boolean getImplicit() {

		return implicit;
	}

	/**
	 * @param implicit
	 *            the implicit to set
	 */
	public void setImplicit(Boolean implicit) {

		this.implicit = implicit;
	}

	public boolean equals(Object other) {

		if (this == other)
			return true;

		if (!(other instanceof Tag))
			return false;

		if (other == null)
			return false;

		Tag that = (Tag) other;

		return getName().equals(that.getName());
	}

	@Override
	public int hashCode() {

		return getName().hashCode();
	}

}
