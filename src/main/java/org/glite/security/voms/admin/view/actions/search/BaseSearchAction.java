package org.glite.security.voms.admin.view.actions.search;

import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.glite.security.voms.admin.dao.SearchResults;
import org.glite.security.voms.admin.operations.search.BaseSearchOperation;
import org.glite.security.voms.admin.view.actions.BaseAction;
import org.glite.security.voms.admin.view.actions.SearchData;

import com.opensymphony.xwork2.validator.annotations.VisitorFieldValidator;

@ParentPackage("base")
public abstract class BaseSearchAction extends BaseAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final String USER_SEARCH_NAME = "user";
	public static final String GROUP_SEARCH_NAME = "group";
	public static final String ROLE_SEARCH_NAME = "role";
	public static final String ATTRIBUTE_SEARCH_NAME = "attribute";

	public static final String MEMBER_SEARCH_NAME = "member";

	public static final String GROUP_MEMBER_SEARCH_NAME = "group-member";
	public static final String ROLE_MEMBER_SEARCH_NAME = "role-member";

	protected SearchData searchData;

	protected SearchResults searchResults;

	@VisitorFieldValidator(appendPrefix = true, message = "Invalid input:  ")
	public SearchData getSearchData() {
		return searchData;
	}

	public void setSearchData(SearchData searchData) {
		this.searchData = searchData;
	}

	@Override
	public String execute() throws Exception {

		searchResults = (SearchResults) BaseSearchOperation.instance(
				getSearchData()).execute();

		return SUCCESS;

	}

	public SearchResults getSearchResults() {
		return searchResults;
	}

	public void setSearchResults(SearchResults searchResults) {
		this.searchResults = searchResults;
	}

	protected void initSearchData(String searchType) {

		if (getSearchData() == null) {
			SearchData sd = new SearchData();
			sd.setType(searchType);
			setSearchData(sd);

		} else
			getSearchData().setType(searchType);
	}

}
