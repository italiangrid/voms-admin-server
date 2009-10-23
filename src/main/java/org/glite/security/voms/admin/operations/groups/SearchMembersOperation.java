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
import org.glite.security.voms.admin.model.VOMSGroup;
import org.glite.security.voms.admin.model.VOMSRole;
import org.glite.security.voms.admin.operations.BaseMemberhipReadOperation;
import org.glite.security.voms.admin.operations.VOMSContext;
import org.glite.security.voms.admin.view.actions.SearchData;

public class SearchMembersOperation extends BaseMemberhipReadOperation {

	int firstResult = 0, maxResults = 0;
	String searchString;

	private SearchMembersOperation(VOMSContext c, String searchString,
			int firstResult, int maxResults) {
		super(c);

		this.firstResult = firstResult;
		this.maxResults = maxResults;
		this.searchString = searchString;
	}

	private SearchMembersOperation(VOMSGroup g, VOMSRole r,
			String searchString, int firstResult, int maxResults) {

		super(VOMSContext.instance(g, r));

		this.firstResult = firstResult;
		this.maxResults = maxResults;

		this.searchString = searchString;

	}

	protected Object doExecute() {

		if (!__context.isGroupContext())
			return VOMSRoleDAO.instance().searchMembers(__context.getGroup(),
					__context.getRole(), searchString, firstResult, maxResults);

		return VOMSGroupDAO.instance().searchMembers(__context.getGroup(),
				searchString, firstResult, maxResults);

	}

	public static SearchMembersOperation instance(VOMSGroup g, VOMSRole r,
			String searchString, int firstResult, int maxResults) {

		return new SearchMembersOperation(g, r, searchString, firstResult,
				maxResults);
	}

	public static SearchMembersOperation instance(VOMSGroup g,
			String searchString, int firstResult, int maxResults) {

		return new SearchMembersOperation(g, null, searchString, firstResult,
				maxResults);
	}

	public static SearchMembersOperation instance(SearchData sd) {

		VOMSContext ctxt = VOMSContext
				.instance(sd.getGroupId(), sd.getRoleId());

		return new SearchMembersOperation(ctxt, sd.getText(), sd
				.getFirstResult(), sd.getMaxResults());
	}
}
