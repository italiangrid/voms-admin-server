package org.glite.security.voms.admin.view.actions.search;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Actions;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.glite.security.voms.admin.dao.SearchResults;
import org.glite.security.voms.admin.operations.search.BaseSearchOperation;
import org.glite.security.voms.admin.view.actions.SearchData;

import com.opensymphony.xwork2.ActionSupport;


@ParentPackage("base")

@Results({
	@Result(name=SearchAction.USER_SEARCH_NAME, location="users"),
	@Result(name=SearchAction.GROUP_SEARCH_NAME, location="groups"),
	@Result(name=SearchAction.ROLE_SEARCH_NAME, location="roles"),
	@Result(name=SearchAction.ATTRIBUTE_SEARCH_NAME, location="attributes")
})

public class SearchAction extends ActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static final String USER_SEARCH_NAME = "user";
	public static final String GROUP_SEARCH_NAME = "group";
	public static final String ROLE_SEARCH_NAME = "role";
	public static final String ATTRIBUTE_SEARCH_NAME = "attribute";
	
	protected SearchData searchData;

	protected SearchResults searchResults;

	public SearchData getSearchData() {
		return searchData;
	}


	public void setSearchData(SearchData searchData) {
		this.searchData = searchData;
	}
	
	
	public String input() throws Exception{
		
		return getSearchData().getType().toLowerCase();
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
	
	private void initSearchData(String searchType){
		
		if (getSearchData() == null){
			SearchData sd = new SearchData();
			sd.setType(searchType);
			setSearchData(sd);
			
		}else
			getSearchData().setType(searchType);
	}
	
	@Actions({
		@Action("user"),
		@Action("/user/search")
	})
	public String userSearch() throws Exception{
		
		initSearchData(USER_SEARCH_NAME);
		return execute();
			
	}
	
	@Actions({
		@Action("group"),
		@Action("/group/search")
	})
	public String groupSearch() throws Exception{
		
		initSearchData(GROUP_SEARCH_NAME);
		return execute();
		
		
	}
	
	@Actions({
		@Action("role"),
		@Action("/role/search")
	})
	public String roleSearch() throws Exception{
		
		initSearchData(ROLE_SEARCH_NAME);
		return execute();
		
	}
	
	@Actions({
		@Action("attribute"),
		@Action("/attribute/search")
	})
	public String attributeSearch() throws Exception{
		initSearchData(ATTRIBUTE_SEARCH_NAME);
		return execute();
	}
}

