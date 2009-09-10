package org.glite.security.voms.admin.view.interceptors;

import java.util.Map;

import org.glite.security.voms.admin.dao.SearchResults;
import org.glite.security.voms.admin.view.actions.SearchAware;
import org.glite.security.voms.admin.view.actions.SearchData;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

public class SearchAwareInterceptor extends AbstractInterceptor {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public String intercept(ActionInvocation invocation) throws Exception {
		
		Action action = (Action) invocation.getAction();
		
		Map<String, Object> session  = (Map<String, Object>) invocation.getInvocationContext().getSession();
		
		if (action instanceof SearchAware)
			if (session.containsKey("searchData") && session.containsKey("searchResults")){
				
				SearchAware saAction = (SearchAware)action;
				saAction.setSearchData((SearchData) session.get("searchData"));
				saAction.setSearchResults((SearchResults) session.get("searchResults"));
			}
		
		return invocation.invoke();
	}

}
