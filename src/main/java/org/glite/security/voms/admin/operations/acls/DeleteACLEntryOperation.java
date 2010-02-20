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
package org.glite.security.voms.admin.operations.acls;

import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.glite.security.voms.admin.dao.ACLDAO;
import org.glite.security.voms.admin.dao.VOMSGroupDAO;
import org.glite.security.voms.admin.error.VOMSAuthorizationException;
import org.glite.security.voms.admin.operations.BaseVomsOperation;
import org.glite.security.voms.admin.operations.VOMSPermission;
import org.glite.security.voms.admin.persistence.model.ACL;
import org.glite.security.voms.admin.persistence.model.VOMSAdmin;
import org.glite.security.voms.admin.persistence.model.VOMSGroup;

public class DeleteACLEntryOperation extends BaseVomsOperation {

	private static final Log log = LogFactory
			.getLog(DeleteACLEntryOperation.class);

	private ACL acl;
	private VOMSAdmin admin;

	private boolean recursive = false;

	protected Object doExecute() {

		if (isRecursive()) {

			if (acl.getContext().isGroupContext()) {
				try {

					List childrenGroups = VOMSGroupDAO.instance().getChildren(
							acl.getGroup());
					Iterator childIter = childrenGroups.iterator();

					while (childIter.hasNext()) {

						VOMSGroup childGroup = (VOMSGroup) childIter.next();
						DeleteACLEntryOperation op = instance(childGroup
								.getACL(), admin, recursive);
						op.execute();
					}

					log.debug("Removing ACL entry for admin '" + admin
							+ "' in ACL '" + acl.getContext()
							+ "' [recursive].");
					ACLDAO.instance().deleteACLEntry(acl, admin);
					return acl;

				} catch (VOMSAuthorizationException e) {

					log
							.warn("Authorization Error saving recursively ACL entry !");

				} catch (RuntimeException e) {

					throw e;
				}

			} else {

				log.debug("Removing ACL entry for admin '" + admin
						+ "' in ACL '" + acl.getContext() + "' [recursive].");
				ACLDAO.instance().deleteACLEntry(acl, admin);
				return acl;
			}

		} else {

			log.debug("Removing ACL entry for admin '" + admin + "' in ACL '"
					+ acl.getContext() + "'.");
			ACLDAO.instance().deleteACLEntry(acl, admin);
			return acl;
		}

		return null;
	}

	protected void setupPermissions() {

		VOMSPermission requiredPerms = null;
		if (acl.isDefautlACL())
			requiredPerms = VOMSPermission.getEmptyPermissions()
					.setACLDefaultPermission().setACLReadPermission()
					.setACLWritePermission();
		else
			requiredPerms = VOMSPermission.getEmptyPermissions()
					.setACLReadPermission().setACLWritePermission();

		addRequiredPermission(acl.getContext(), requiredPerms);

	}

	private DeleteACLEntryOperation(ACL acl, VOMSAdmin admin) {

		this.acl = acl;
		this.admin = admin;
	}

	private DeleteACLEntryOperation(ACL acl, VOMSAdmin admin, boolean propagate) {

		this.acl = acl;
		this.admin = admin;
		this.recursive = propagate;
	}

	public static DeleteACLEntryOperation instance(ACL acl, VOMSAdmin admin) {

		return new DeleteACLEntryOperation(acl, admin);
	}

	public static DeleteACLEntryOperation instance(ACL acl, VOMSAdmin admin,
			boolean propagate) {

		return new DeleteACLEntryOperation(acl, admin, propagate);
	}

	protected boolean isRecursive() {
		return recursive;
	}

}
