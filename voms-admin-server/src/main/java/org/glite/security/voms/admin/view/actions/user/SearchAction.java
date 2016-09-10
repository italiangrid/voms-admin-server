/**
 * Copyright (c) Istituto Nazionale di Fisica Nucleare (INFN). 2006-2015
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
 */
package org.glite.security.voms.admin.view.actions.user;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.glite.security.voms.admin.operations.BaseVoReadOperation;
import org.glite.security.voms.admin.operations.VOMSOperation;
import org.glite.security.voms.admin.operations.users.SearchUsersPersonalInformationOperation;
import org.glite.security.voms.admin.persistence.dao.SearchResults;
import org.glite.security.voms.admin.persistence.dao.VOMSUserDAO;
import org.glite.security.voms.admin.taglib.SearchNavBarTag;
import org.glite.security.voms.admin.view.actions.BaseAction;
import org.glite.security.voms.admin.view.actions.search.BaseSearchAction;

import com.opensymphony.xwork2.Preparable;

@Results({

  @Result(name = BaseAction.SUCCESS, location = "users"),
  @Result(name = BaseAction.INPUT, location = "users") })
public class SearchAction extends BaseSearchAction implements Preparable {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  String limitToSuspendedUsers;
  String limitToUsersWithPendingSignAUPRequest;

  public void prepare() throws Exception {

    initSearchData(USER_SEARCH_NAME);

  }

  protected Map<String, String> getSearchCustomFlags() {

    Map<String, String> params = new HashMap<String, String>();

    if (limitToSuspendedUsers != null) {
      params.put("limitToSuspendedUsers", limitToSuspendedUsers);
      params.put("limitToUsersWithPendingSignAUPRequest",
        limitToUsersWithPendingSignAUPRequest);
    }

    return params;
  }

  private Callable<SearchResults> searchWithPendingAUPRequest() {

    return new Callable<SearchResults>() {

      @Override
      public SearchResults call() throws Exception {

        return VOMSUserDAO.instance().searchWithPendingAUPRequest(
          searchData.getText(), searchData.getFirstResult(),
          searchData.getMaxResults());
      }
    };
  }

  private Callable<SearchResults> searchSuspendedUsers() {

    return new Callable<SearchResults>() {

      @Override
      public SearchResults call() throws Exception {
        
        VOMSOperation<SearchResults> op = 
          new BaseVoReadOperation<SearchResults>() {
          
          @Override
          protected SearchResults doExecute() {
            return VOMSUserDAO.instance().searchSuspended(searchData.getText(),
              searchData.getFirstResult(), searchData.getMaxResults());
          }
        };

        return op.execute();
      }
    };
  }

  protected Callable<SearchResults> searchExpiredUsers() {
    return new Callable<SearchResults>() {

      @Override
      public SearchResults call() throws Exception {
        
        VOMSOperation<SearchResults> op = 
          new BaseVoReadOperation<SearchResults>() {
          
          @Override
          protected SearchResults doExecute() {
            return VOMSUserDAO.instance().searchExpired(searchData.getText(),
              searchData.getFirstResult(), searchData.getMaxResults());
          }
        };

        return op.execute();
      }
    };
  }
  
  @Override
  public String execute() throws Exception {

    if (limitToSuspendedUsers == null
      && limitToUsersWithPendingSignAUPRequest == null) {
      return super.execute();
    }

    if (limitToSuspendedUsers.equals("false")
      && limitToUsersWithPendingSignAUPRequest.equals("false")) {
      return super.execute();
    }

    if (limitToUsersWithPendingSignAUPRequest.equals("true")) {

      searchResults = SearchUsersPersonalInformationOperation
        .instance(searchWithPendingAUPRequest())
        .execute();

    } else if (limitToSuspendedUsers.equals("true")) {

      searchResults = searchSuspendedUsers().call();
    }

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

  /**
   * @return the limitToUsersWithPendingSignAUPRequest
   */
  public String getLimitToUsersWithPendingSignAUPRequest() {

    return limitToUsersWithPendingSignAUPRequest;
  }

  /**
   * @param limitToUsersWithPendingSignAUPRequest the
   * limitToUsersWithPendingSignAUPRequest to set
   */
  public void setLimitToUsersWithPendingSignAUPRequest(
    String limitToUsersWithPendingSignAUPRequest) {

    this.limitToUsersWithPendingSignAUPRequest = limitToUsersWithPendingSignAUPRequest;
  }

}
