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
package org.glite.security.voms.admin.view.actions.user;

import java.util.HashMap;
import java.util.Map;

import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.glite.security.voms.admin.persistence.dao.VOMSUserDAO;
import org.glite.security.voms.admin.taglib.SearchNavBarTag;
import org.glite.security.voms.admin.view.actions.BaseAction;
import org.glite.security.voms.admin.view.actions.search.BaseSearchAction;

import com.opensymphony.xwork2.Preparable;

@ParentPackage("base")
@Results( {

@Result(name = BaseAction.SUCCESS, location = "users"),
		@Result(name = BaseAction.INPUT, location = "users") })
public class SearchAction extends BaseSearchAction implements Preparable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	String limitToSuspendedUsers;

	public void prepare() throws Exception {
		initSearchData(USER_SEARCH_NAME);

	}

	protected Map<String,String> getSearchCustomFlags(){
		
		Map<String,String> params = new HashMap<String, String>();
		
		if (limitToSuspendedUsers != null){
			params.put("limitToSuspendedUsers", limitToSuspendedUsers);
		}
		
		return params;
	}
	
	@Override
	public String execute() throws Exception {
	
		if (limitToSuspendedUsers == null){
			return super.execute();
		}
		
		if (limitToSuspendedUsers.equals("false"))
			return super.execute();
		
		searchResults = VOMSUserDAO.instance().searchSuspended(searchData.getText(), 
					searchData.getFirstResult(), 
					searchData.getMaxResults());
		
		session.put("searchData", getSearchData());
		session.put("searchResults", searchResults);
		
		session.put(SearchNavBarTag.SEARCH_PARAMS_KEY, getSearchCustomFlags());
		
		return SUCCESS;
		
	}
	
	public String getLimitToSuspendedUsers() {
		return limitToSuspendedUsers;
	}

	public void setLimitToSuspendedUsers(String limitToSuspendedUsers) {
		this.limitToSuspendedUsers = limitToSuspendedUsers;
	}
	
	
}
