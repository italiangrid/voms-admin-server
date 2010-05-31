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
package org.glite.security.voms.admin.operations.groups;

import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.glite.security.voms.admin.operations.BaseVomsOperation;
import org.glite.security.voms.admin.operations.VOMSContext;
import org.glite.security.voms.admin.operations.VOMSPermission;
import org.glite.security.voms.admin.operations.roles.ListRolesOperation;
import org.glite.security.voms.admin.persistence.HibernateFactory;
import org.glite.security.voms.admin.persistence.dao.VOMSGroupDAO;
import org.glite.security.voms.admin.persistence.model.VOMSGroup;
import org.glite.security.voms.admin.persistence.model.VOMSRole;
import org.glite.security.voms.admin.util.PathNamingScheme;

public class CreateGroupOperation extends BaseVomsOperation {
	private static final Logger log = LoggerFactory
			.getLogger(CreateGroupOperation.class);

	String groupName;

	private CreateGroupOperation(String name) {

		groupName = name;
	}

	private void setupACLs(VOMSGroup g) {

		log.debug("Setting up acls for group '" + g + "'.");

		// Setup the ACL for the newly created group starting from the
		// parent's default ACL, if exists, or from the parent's ACL.
		if (g.getParent().getDefaultACL() != null)
			g.importACL(g.getParent().getDefaultACL());
		else
			g.importACL(g.getParent().getACL());

		// Create ACLs for existing roles
		List roles = (List) ListRolesOperation.instance().execute();

		Iterator rolesIter = roles.iterator();

		while (rolesIter.hasNext()) {

			VOMSRole r = (VOMSRole) rolesIter.next();
			log.debug("Importing group '" + g + "' acl in role '" + r + "'.");
			r.importACL(g);
			HibernateFactory.getSession().save(r);

		}

	}

	protected Object doExecute() {

		VOMSGroup g = VOMSGroupDAO.instance().create(groupName);

		setupACLs(g);

		HibernateFactory.getSession().save(g);

		return g;
	}

	public static CreateGroupOperation instance(String groupName) {

		return new CreateGroupOperation(groupName);
	}

	protected void setupPermissions() {

		String parentGroupName = PathNamingScheme.getParentGroupName(groupName);

		VOMSGroup parentGroup = VOMSGroupDAO.instance().findByName(
				parentGroupName);

		// Add CONTAINER_READ permissions on the path from the root group to
		// the grandfather of the group that is being created
		addPermissionsOnPath(parentGroup, VOMSPermission
				.getContainerReadPermission());

		// Add CONTAINER_WRITE permissions on the parent group of the group that
		// is
		// being created
		addRequiredPermission(VOMSContext.instance(parentGroup), VOMSPermission
				.getContainerRWPermissions());

		if (!parentGroup.isRootGroup()) {
			addRequiredPermission(VOMSContext.getVoContext(), VOMSPermission
					.getContainerRWPermissions());
		}

		if (log.isDebugEnabled())
			logRequiredPermissions();
	}

	protected String logArgs() {
		return ToStringBuilder.reflectionToString(this);
	}
}
