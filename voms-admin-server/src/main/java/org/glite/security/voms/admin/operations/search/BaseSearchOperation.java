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
package org.glite.security.voms.admin.operations.search;

import org.glite.security.voms.admin.configuration.VOMSConfiguration;
import org.glite.security.voms.admin.configuration.VOMSConfigurationConstants;
import org.glite.security.voms.admin.error.VOMSException;
import org.glite.security.voms.admin.operations.BaseVoReadOperation;
import org.glite.security.voms.admin.operations.groups.SearchGroupsOperation;
import org.glite.security.voms.admin.operations.groups.SearchMembersOperation;
import org.glite.security.voms.admin.operations.roles.SearchRolesOperation;
import org.glite.security.voms.admin.operations.users.SearchUserAttributesOperation;
import org.glite.security.voms.admin.operations.users.SearchUsersOperation;
import org.glite.security.voms.admin.view.actions.SearchData;

public class BaseSearchOperation extends BaseVoReadOperation implements
		SearchOperation {

	SearchData searchData;

	int userMaxResults = VOMSConfiguration.instance().getInt(
			VOMSConfigurationConstants.USER_MAX_RESULTS_PER_PAGE, 15);
	int groupMaxResults = VOMSConfiguration.instance().getInt(
			VOMSConfigurationConstants.GROUP_MAX_RESULTS_PER_PAGE, 15);
	int roleMaxResults = VOMSConfiguration.instance().getInt(
			VOMSConfigurationConstants.ROLE_MAX_RESULTS_PER_PAGE, 15);
	int attributeMaxResults = VOMSConfiguration.instance().getInt(
			VOMSConfigurationConstants.ATTRIBUTES_MAX_RESULTS_PER_PAGE, 15);

	protected BaseSearchOperation(SearchData sd) {
		setSearchData(sd);
	}

	@Override
	protected Object doExecute() {

		// Default search is user search
		if (searchData == null)
			return SearchUsersOperation.instance(null, 0, userMaxResults)
					.execute();
		else {

			String searchType = searchData.getType().toLowerCase();

			if (searchData.getText() != null
					&& searchData.getText().trim().equals(""))
				searchData.setText(null);

			if (searchData.getMaxResults() == 0)
				searchData.setMaxResults(userMaxResults);

			if (searchType.equals("user"))
				return SearchUsersOperation.instance(searchData.getText(),
						searchData.getFirstResult(), searchData.getMaxResults()).execute();
			else if (searchType.equals("group"))
				return SearchGroupsOperation.instance(searchData.getText(),
						searchData.getFirstResult(), groupMaxResults).execute();
			else if (searchType.equals("role"))
				return SearchRolesOperation.instance(searchData.getText(),
						searchData.getFirstResult(), groupMaxResults).execute();
			else if (searchType.equals("attribute"))
				return SearchUserAttributesOperation.instance(
						searchData.getText(), searchData.getFirstResult(),
						groupMaxResults).execute();
			else if (searchType.equals("group-member")
					|| searchType.equals("role-member"))
				return SearchMembersOperation.instance(searchData).execute();
			else
				throw new VOMSException("Unsupported search type '"
						+ searchData.getType() + "'");

		}
	}

	public SearchData getSearchData() {

		return searchData;
	}

	public void setSearchData(SearchData data) {
		searchData = data;

	}

	public static SearchOperation instance(SearchData sd) {

		return new BaseSearchOperation(sd);
	}
}
