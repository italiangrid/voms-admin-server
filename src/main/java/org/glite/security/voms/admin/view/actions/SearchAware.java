package org.glite.security.voms.admin.view.actions;

import org.glite.security.voms.admin.dao.SearchResults;

public interface SearchAware {
	
	public void setSearchData(SearchData sd);
	public void setSearchResults(SearchResults sr);
	
	public SearchData getSearchData();
	public SearchResults getSearchResults();
	

}
