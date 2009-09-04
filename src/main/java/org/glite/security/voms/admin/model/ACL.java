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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.glite.security.voms.admin.common.VOMSServiceConstants;
import org.glite.security.voms.admin.dao.VOMSAdminDAO;
import org.glite.security.voms.admin.operations.VOMSContext;
import org.glite.security.voms.admin.operations.VOMSPermission;

public class ACL implements Serializable {

	/**
     * 
     */
	private static final long serialVersionUID = 1L;

	private static final Log log = LogFactory.getLog(ACL.class);

	Long id;

	VOMSGroup group = null;

	Boolean defaultACL;

	VOMSRole role = null;

	Map<VOMSAdmin, VOMSPermission> permissions = new HashMap<VOMSAdmin, VOMSPermission>();

	public ACL() {

	}

	public ACL(VOMSGroup g, VOMSRole r, boolean isDefaultACL) {

		this.group = g;
		this.role = r;
		this.defaultACL = new Boolean(isDefaultACL);
	}

	public ACL(VOMSGroup g, boolean isDefaultACL) {

		this(g, null, isDefaultACL);
	}

	public VOMSGroup getGroup() {

		return group;
	}

	public void setGroup(VOMSGroup group) {

		this.group = group;
	}

	public Map<VOMSAdmin, VOMSPermission> getPermissions() {

		return permissions;
	}

	public void setPermissions(Map<VOMSAdmin, VOMSPermission> permissions) {

		this.permissions = permissions;
	}

	public VOMSRole getRole() {

		return role;
	}

	public void setRole(VOMSRole role) {

		this.role = role;
	}

	public boolean equals(Object other) {

		if (this == other)
			return true;
		if (!(other instanceof ACL))
			return false;

		ACL that = (ACL) other;

		if (that == null)
			return false;

		if (getGroup().equals(that.getGroup())) {

			if ((getRole() == null) && (that.getRole() == null)) {

				return getDefaultACL().equals(that.getDefaultACL());
			}

			if ((getRole() != null) && (that.getRole() != null))
				return getRole().equals(that.getRole());
		}

		return false;
	}

	public int hashCode() {

		int result = 14;

		result = 29 * result + getGroup().hashCode();

		if (getRole() != null)
			result = 29 * result + getRole().hashCode();

		return result;
	}

	public Boolean getDefaultACL() {

		return defaultACL;
	}

	public void setDefaultACL(Boolean defaultACL) {

		this.defaultACL = defaultACL;
	}

	public Long getId() {

		return id;
	}

	public void setPermissions(VOMSAdmin a, VOMSPermission p) {

		getPermissions().put(a, p);

	}

	public void removePermissions(VOMSAdmin a) {

		getPermissions().remove(a);

	}

	public void setId(Long id) {

		this.id = id;
	}

	public boolean isDefautlACL() {

		return defaultACL.booleanValue();
	}

	public VOMSPermission getPermissions(VOMSAdmin a) {

		return getPermissions().get(a);

	}

	public Map<VOMSAdmin, VOMSPermission> getRolePermissions() {

		Map<VOMSAdmin, VOMSPermission> result = new HashMap<VOMSAdmin, VOMSPermission>();

		for (Map.Entry<VOMSAdmin, VOMSPermission> entry : getPermissions()
				.entrySet()) {

			VOMSAdmin admin = entry.getKey();

			if (admin.getCa().getSubjectString().equals(
					VOMSServiceConstants.ROLE_CA))
				result.put(admin, entry.getValue());
		}

		return result;
	}

	public Map<VOMSAdmin, VOMSPermission> getGroupPermissions() {

		Map<VOMSAdmin, VOMSPermission> result = new HashMap<VOMSAdmin, VOMSPermission>();

		for (Map.Entry<VOMSAdmin, VOMSPermission> entry : getPermissions()
				.entrySet()) {

			VOMSAdmin admin = entry.getKey();

			if (admin.getCa().getSubjectString().equals(
					VOMSServiceConstants.GROUP_CA))
				result.put(admin, entry.getValue());

		}

		return result;
	}

	public Set<VOMSAdmin> getAdminsWithPermissions(
			VOMSPermission requiredPermission) {

		Set<VOMSAdmin> results = new HashSet<VOMSAdmin>();

		for (Map.Entry<VOMSAdmin, VOMSPermission> entry : getPermissions()
				.entrySet()) {

			VOMSAdmin a = entry.getKey();
			VOMSPermission p = entry.getValue();

			// We return here only group admins and role admins!
			// FIXME: maybe tag admins should be here as well
			if (p.satisfies(requiredPermission)
					&& ((a.isGroupAdmin() || a.isRoleAdmin())))
				results.add(entry.getKey());

		}

		return results;

	}

	public VOMSPermission getAnyAuthenticatedUserPermissions() {

		VOMSAdmin anyAuthenticatedUserAdmin = VOMSAdminDAO.instance()
				.getAnyAuthenticatedUserAdmin();
		return permissions.get(anyAuthenticatedUserAdmin);
	}

	public Map<VOMSAdmin, VOMSPermission> getExternalPermissions() {

		Map<VOMSAdmin, VOMSPermission> result = new HashMap<VOMSAdmin, VOMSPermission>();

		Iterator<VOMSAdmin> admins = permissions.keySet().iterator();

		while (admins.hasNext()) {

			VOMSAdmin admin = admins.next();

			if ((admin.getDn().equals(VOMSServiceConstants.ANYUSER_ADMIN))
					|| (!admin.getDn().startsWith(
							VOMSServiceConstants.INTERNAL_DN_PREFIX)))
				result.put(admin, permissions.get(admin));

		}

		return result;
	}

	public VOMSContext getContext() {

		return VOMSContext.instance(getGroup(), getRole());
	}

	public String toString() {
		
		ToStringBuilder builder = new ToStringBuilder(this);
		builder.append("id",id).append("defaultACL", defaultACL).append("group", group).append("role", role).append("permissions",permissions);

		return builder.toString();

	}
}
