package org.glite.security.voms.admin.operations.search;

import org.glite.security.voms.admin.common.VOMSConfiguration;
import org.glite.security.voms.admin.common.VOMSException;
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
			VOMSConfiguration.USER_MAX_RESULTS_PER_PAGE, 15);
	int groupMaxResults = VOMSConfiguration.instance().getInt(
			VOMSConfiguration.GROUP_MAX_RESULTS_PER_PAGE, 15);
	int roleMaxResults = VOMSConfiguration.instance().getInt(
			VOMSConfiguration.ROLE_MAX_RESULTS_PER_PAGE, 15);
	int attributeMaxResults = VOMSConfiguration.instance().getInt(
			VOMSConfiguration.ATTRIBUTES_MAX_RESULTS_PER_PAGE, 15);

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
						searchData.getFirstResult(), userMaxResults).execute();
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
