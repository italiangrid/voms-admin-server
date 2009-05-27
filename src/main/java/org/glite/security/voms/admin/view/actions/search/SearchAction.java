package org.glite.security.voms.admin.view.actions.search;

import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.glite.security.voms.admin.dao.SearchResults;
import org.glite.security.voms.admin.operations.search.BaseSearchOperation;
import org.glite.security.voms.admin.view.actions.SearchData;

import com.opensymphony.xwork2.ActionSupport;


@ParentPackage("base")

@Results({
	@Result(name="user", location="userSearch"),
	@Result(name="group", location="groupSearch"),
	@Result(name="role", location="groupSearch"),
	@Result(name="attribute", location="attributeSearch")
})

public class SearchAction extends ActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	protected SearchData searchData;

	protected SearchResults searchResults;

	public SearchData getSearchData() {
		return searchData;
	}


	public void setSearchData(SearchData searchData) {
		this.searchData = searchData;
	}
	
	
	@Override
	public String execute() throws Exception {
		
		searchResults = (SearchResults) BaseSearchOperation.instance(getSearchData()).execute();
		
		// Default search is a user search
		if (getSearchData() ==  null)
			return "user";
		else
			return getSearchData().getType().toLowerCase();	
		
	}

	public SearchResults getSearchResults() {
		return searchResults;
	}


	public void setSearchResults(SearchResults searchResults) {
		this.searchResults = searchResults;
	}
	

}
