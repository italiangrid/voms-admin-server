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
package org.glite.security.voms.admin.view.actions.home;

import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.glite.security.voms.admin.operations.CurrentAdmin;
import org.glite.security.voms.admin.persistence.model.VOMSGroup;
import org.glite.security.voms.admin.persistence.model.VOMSRole;
import org.glite.security.voms.admin.view.actions.BaseAction;

@ParentPackage(value = "base")
@Results({
  @Result(name = "admin-home", location = "/admin/home.action",
    type = "redirect"),
  @Result(name = "user-home", location = "/user/home.action", type = "redirect"),
  @Result(name = "unauthenticated", location = "guest"),
  @Result(name = "register", location = "/register/start.action",
    type = "redirect") })
public class LoginAction extends BaseAction {

  /**
	 * 
	 */
  private static final long serialVersionUID = 1L;

  @Override
  public String execute() throws Exception {

    CurrentAdmin admin = CurrentAdmin.instance();
    VOMSGroup rootGroup = getVORootGroup();
    VOMSRole groupManagerRole = getGroupManagerRole();
    
    if (admin.isVOAdmin()){
      return "admin-home";
    }
    else if (groupManagerRole != null && 
      admin.hasRole(rootGroup, groupManagerRole)){
      return "admin-home";
    } else if (admin.isVoUser()){
      return "user-home";
    }
    else if (admin.isUnauthenticated()){
      return "unauthenticated";
    }
      
    return "register";
    
  }

}
