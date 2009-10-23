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
