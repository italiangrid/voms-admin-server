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
package org.glite.security.voms.admin.persistence.dao;

import java.util.Iterator;
import java.util.List;

import org.glite.security.voms.admin.error.NullArgumentException;
import org.glite.security.voms.admin.operations.VOMSContext;
import org.glite.security.voms.admin.operations.VOMSPermission;
import org.glite.security.voms.admin.persistence.HibernateFactory;
import org.glite.security.voms.admin.persistence.model.ACL;
import org.glite.security.voms.admin.persistence.model.VOMSAdmin;
import org.glite.security.voms.admin.persistence.model.VOMSGroup;
import org.glite.security.voms.admin.persistence.model.VOMSRole;

public class ACLDAO {

	private ACLDAO() {

		HibernateFactory.beginTransaction();
	}

	public ACL getById(Long id) {

		return (ACL) HibernateFactory.getSession().load(ACL.class, id);
	}

	public ACL create(VOMSGroup g, boolean isDefault) {
		if (g == null)
			throw new NullArgumentException(
					"Cannot create an ACL for a null group!");

		ACL acl = new ACL(g, isDefault);
		HibernateFactory.getSession().save(acl);
		g.getAcls().add(acl);
		HibernateFactory.getSession().save(g);

		return acl;

	}

	public void delete(ACL acl) {

		if (acl == null)
			throw new NullArgumentException("Cannot delete a null ACL!");

		VOMSContext context = acl.getContext();
		if (context.isGroupContext() && acl.isDefautlACL()
				&& acl.getPermissions().isEmpty()) {
			context.getGroup().getAcls().remove(acl);
			HibernateFactory.getSession().delete(acl);
			HibernateFactory.getSession().update(context.getGroup());
		}
	}

	public void deletePermissionsForRole(VOMSRole r) {

		List roleAdmins = VOMSAdminDAO.instance().getRoleAdmins(r);

		Iterator i = roleAdmins.iterator();

		while (i.hasNext())
			deletePermissionsForAdmin((VOMSAdmin) i.next());

	}

	public void deletePermissionsForAdmin(VOMSAdmin a) {

		String query = "select a from org.glite.security.voms.admin.persistence.model.ACL a join a.permissions where admin_id = :adminId";
		Iterator i = HibernateFactory.getSession().createQuery(query).setLong(
				"adminId", a.getId().longValue()).iterate();

		while (i.hasNext()) {

			ACL acl = (ACL) i.next();
			acl.removePermissions(a);
			HibernateFactory.getSession().save(acl);
		}

	}

	public List<VOMSAdmin> getAdminsWithoutActivePermissions(){
	    	    
	    String query = "from VOMSAdmin a where a.dn not like '/O=VOMS%' and a not in (select distinct(index(p)) from ACL a join a.permissions p)";
	    return HibernateFactory.getSession().createQuery(query).list();
	}
	
	
	public boolean hasActivePermissions(VOMSAdmin a) {

		String query = "select count(*) from org.glite.security.voms.admin.persistence.model.ACL a join a.permissions where admin_id = :adminId";
		Long count = (Long) HibernateFactory.getSession().createQuery(query)
				.setLong("adminId", a.getId().longValue()).uniqueResult();

		return (count.longValue() > 0);
	}

	public void recursiveSaveACLEntry(ACL acl, VOMSAdmin admin,
			VOMSPermission perms) {

		if (acl == null)
			throw new NullArgumentException("acl cannot be null!");

		if (admin == null)
			throw new NullArgumentException("admin cannot be null!");

		if (perms == null)
			throw new NullArgumentException("perms cannot be null!");

		saveACLEntry(acl, admin, perms);

		if (acl.getContext().isGroupContext() && !acl.isDefautlACL()) {

			List childrenGroups = VOMSGroupDAO.instance().getChildren(
					acl.getGroup());

			Iterator childGroupIter = childrenGroups.iterator();

			while (childGroupIter.hasNext()) {

				VOMSGroup childGroup = (VOMSGroup) childGroupIter.next();
				recursiveSaveACLEntry(childGroup.getACL(), admin, perms);
			}
		}
	}

	public void saveACLEntry(ACL acl, VOMSAdmin admin, VOMSPermission perms) {

		if (acl == null)
			throw new NullArgumentException("acl cannot be null!");

		if (admin == null)
			throw new NullArgumentException("admin cannot be null!");

		if (perms == null)
			throw new NullArgumentException("perms cannot be null!");

		acl.setPermissions(admin, perms);
		HibernateFactory.getSession().save(acl);

		if (acl.getContext().isGroupContext()) {

			// add entry to 'children' role contexts
			List roles = VOMSRoleDAO.instance().getAll();
			Iterator r = roles.iterator();

			while (r.hasNext()) {

				VOMSRole childRole = (VOMSRole) r.next();
				ACL childRoleACL = childRole.getACL(acl.getGroup());
				childRoleACL.setPermissions(admin, perms);
				HibernateFactory.getSession().save(childRoleACL);
			}
		}

	}

	public void recursiveDeleteACLEntry(ACL acl, VOMSAdmin admin) {

		if (acl == null)
			throw new NullArgumentException("acl cannot be null!");

		if (admin == null)
			throw new NullArgumentException("admin cannot be null!");

		if (acl.getContext().isGroupContext() && !acl.isDefautlACL()) {

			List childrenGroups = VOMSGroupDAO.instance().getChildren(
					acl.getGroup());

			Iterator childGroupIter = childrenGroups.iterator();

			while (childGroupIter.hasNext()) {

				VOMSGroup childGroup = (VOMSGroup) childGroupIter.next();
				recursiveDeleteACLEntry(childGroup.getACL(), admin);

			}
		}

		deleteACLEntry(acl, admin);

	}

	public void deleteACLEntry(ACL acl, VOMSAdmin admin) {

		if (acl == null)
			throw new NullArgumentException("acl cannot be null!");

		if (admin == null)
			throw new NullArgumentException("admin cannot be null!");

		acl.removePermissions(admin);

		if (acl.getPermissions().isEmpty() && acl.isDefautlACL())
			ACLDAO.instance().delete(acl);
		else
			HibernateFactory.getSession().save(acl);

		if (acl.getContext().isGroupContext()) {

			// delete entry from 'children' role contexts
			List roles = VOMSRoleDAO.instance().getAll();
			Iterator r = roles.iterator();

			while (r.hasNext()) {

				VOMSRole childRole = (VOMSRole) r.next();
				ACL childRoleACL = childRole.getACL(acl.getGroup());
				childRoleACL.removePermissions(admin);
				HibernateFactory.getSession().save(childRoleACL);
			}
		}

	}

	public static ACLDAO instance() {
		return new ACLDAO();
	}
}
