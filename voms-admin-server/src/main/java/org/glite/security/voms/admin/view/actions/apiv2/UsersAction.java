/**
 * Copyright (c) Istituto Nazionale di Fisica Nucleare (INFN). 2006-2016
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
package org.glite.security.voms.admin.view.actions.apiv2;

import java.util.List;

import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.glite.security.voms.admin.apiv2.JSONSerializer;
import org.glite.security.voms.admin.apiv2.ListUserResultJSON;
import org.glite.security.voms.admin.apiv2.VOMSUserJSON;
import org.glite.security.voms.admin.operations.users.SearchUsersOperation;
import org.glite.security.voms.admin.persistence.dao.SearchResults;
import org.glite.security.voms.admin.persistence.model.VOMSUser;
import org.glite.security.voms.admin.view.actions.BaseAction;

import com.opensymphony.xwork2.Preparable;

@ParentPackage("json")
@Results({@Result(name = BaseAction.SUCCESS, type = "json", params={"root", "result"})})
public class UsersAction extends BaseAction implements Preparable{
  
  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  Integer startIndex;
  Integer pageSize;
  
  ListUserResultJSON result;
  
  @Override
  public void prepare() throws Exception {
    if (startIndex == null){
      startIndex = 0;
    }
    
    if (pageSize == null){
      pageSize = 100;
    }
    
    result = null;
  }
  
  @Override
  public String execute() throws Exception {

    SearchResults results = SearchUsersOperation.instance("", startIndex, pageSize).execute();
    
    List<VOMSUser> users = results.getResults();
    result = new ListUserResultJSON();
    result.setCount(results.getCount());
    result.setPageSize(pageSize);
    
    List<VOMSUserJSON> serializedUsers = JSONSerializer.serialize(users);
    result.setResult(serializedUsers);
    
    return SUCCESS;
  }
  

  public Integer getStartIndex() {
    return startIndex;
  }

  public void setStartIndex(Integer startIndex) {
    this.startIndex = startIndex;
  }

  public Integer getPageSize() {
    return pageSize;
  }

  public void setPageSize(Integer pageSize) {
    this.pageSize = pageSize;
  }
  
  public ListUserResultJSON getResult() {
    return result;
  }
}
