package org.glite.security.voms.admin.view.actions;

import org.glite.security.voms.admin.dao.SearchResults;

import com.opensymphony.xwork2.ActionSupport;

public abstract class BaseSearchAction extends ActionSupport {

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
		
		
		if (getSearchData() == null)
			searchResults = defaultSearch();
		else
			searchResults = search();
		
		return SUCCESS;	
		
	}

	public SearchResults getSearchResults() {
		return searchResults;
	}


	public void setSearchResults(SearchResults searchResults) {
		this.searchResults = searchResults;
	}
	
	public abstract SearchResults search();
	public abstract SearchResults defaultSearch();

}
