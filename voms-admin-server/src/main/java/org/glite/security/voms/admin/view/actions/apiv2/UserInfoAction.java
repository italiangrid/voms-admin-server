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

import static java.util.Objects.nonNull;

import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.glite.security.voms.admin.apiv2.JSONSerializer;
import org.glite.security.voms.admin.apiv2.VOMSUserJSON;
import org.glite.security.voms.admin.operations.users.FindUserOperation;
import org.glite.security.voms.admin.persistence.model.VOMSUser;
import org.glite.security.voms.admin.view.actions.BaseAction;

import com.opensymphony.xwork2.Preparable;

@ParentPackage("json")
@Results({@Result(name = BaseAction.SUCCESS, type = "json", params={"root", "userInfo"}),
  @Result(name=UserInfoAction.NOT_FOUND, type="httpheader", params={"error", "404", "errorMessage", "User not found"})
})
public class UserInfoAction extends BaseAction implements Preparable {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  public static final String NOT_FOUND = "NOT_FOUND";
  
  Long userId = -1L;
  
  String dn = null;
  String ca = null;

  VOMSUserJSON userInfo;
  VOMSUser user;

  @Override
  public void prepare() throws Exception {

    if (user == null) {
      
      if (userId != -1L){
        user = (VOMSUser) FindUserOperation.instance(userId).execute();
      } else {
        
        if (nonNull(dn) && nonNull(ca)){
          user = (VOMSUser) FindUserOperation.instance(dn, ca).execute();
        } else {
          addActionError("Please provide arguments: userId or the (dn, ca) couple!");
        }
      }
    }
  }

  @Override
  public String execute() throws Exception {

    if (user == null){
      return NOT_FOUND;
    }
    
    userInfo = JSONSerializer.serialize(user);
    return SUCCESS;
  }

  public VOMSUserJSON getUserInfo() {

    return userInfo;
  }

  public Long getUserId() {
    return userId;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
  }

  public String getDn() {
    return dn;
  }

  public void setDn(String dn) {
    this.dn = dn;
  }

  public String getCa() {
    return ca;
  }

  public void setCa(String ca) {
    this.ca = ca;
  }

}
