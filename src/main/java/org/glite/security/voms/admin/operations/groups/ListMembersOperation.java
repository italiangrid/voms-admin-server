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

import org.glite.security.voms.admin.dao.VOMSGroupDAO;
import org.glite.security.voms.admin.dao.VOMSRoleDAO;
import org.glite.security.voms.admin.operations.BaseMemberhipReadOperation;
import org.glite.security.voms.admin.operations.VOMSContext;
import org.glite.security.voms.admin.persistence.model.VOMSGroup;
import org.glite.security.voms.admin.persistence.model.VOMSRole;

public class ListMembersOperation extends BaseMemberhipReadOperation {

	private ListMembersOperation(VOMSContext context) {

		super(context);

	}

	protected Object doExecute() {

		if (!__context.isGroupContext())
			return VOMSRoleDAO.instance().getMembers(__context.getGroup(),
					__context.getRole());

		return VOMSGroupDAO.instance().getMembers(__context.getGroup());
	}

	public static ListMembersOperation instance(VOMSGroup g, VOMSRole r) {

		return new ListMembersOperation(VOMSContext.instance(g, r));
	}

	public static ListMembersOperation instance(String context) {

		return new ListMembersOperation(VOMSContext.instance(context));

	}
}
