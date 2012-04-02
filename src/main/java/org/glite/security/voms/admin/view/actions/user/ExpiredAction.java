package org.glite.security.voms.admin.view.actions.user;

import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.glite.security.voms.admin.persistence.dao.SearchResults;
import org.glite.security.voms.admin.persistence.dao.VOMSUserDAO;
import org.glite.security.voms.admin.view.actions.BaseAction;

@ParentPackage("base")
@Results( {

@Result(name = BaseAction.SUCCESS, location = "expiredUsers"),
		@Result(name = BaseAction.INPUT, location = "expiredUsers") })

public class ExpiredAction extends SearchAction {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Override
	public String execute() throws Exception {
		
		searchResults = SearchResults.fromList(VOMSUserDAO.instance().findExpiredUsers());
		
		session.put("searchData", getSearchData());
		session.put("searchResults", searchResults);
		
		return SUCCESS;
	}

}
