package org.glite.security.voms.admin.view.actions.user;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.glite.security.voms.admin.dao.SearchResults;
import org.glite.security.voms.admin.operations.users.SearchUsersOperation;
import org.glite.security.voms.admin.view.actions.BaseSearchAction;

@ParentPackage(value="base")
@Result(name="success", location="SearchUsers")
public class SearchAction extends BaseSearchAction {
	
	static final Log log = LogFactory.getLog(SearchAction.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Override
	public SearchResults defaultSearch() {
		
		log.debug("defaultSearch:");
		return search();
	}

	@Override
	public SearchResults search() {
		
		log.debug("search:");
		
		SearchUsersOperation op;
		if (searchData == null)
			op = SearchUsersOperation.instance(null,0,15);
		else
			op = SearchUsersOperation.instance(searchData.getText(), searchData.getFirstResult(), 15);
		
		return (SearchResults)op.execute();
	}
	
}
