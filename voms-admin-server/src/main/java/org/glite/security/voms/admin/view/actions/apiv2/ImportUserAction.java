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

import java.util.Objects;

import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.glite.security.voms.admin.apiv2.VOMSUserJSON;
import org.glite.security.voms.admin.operations.users.ImportUserOperation;
import org.glite.security.voms.admin.view.actions.BaseAction;

import com.google.common.base.Strings;
import com.opensymphony.xwork2.ModelDriven;

@ParentPackage("json")
@InterceptorRef(value = "jsonStack",
params = { "json.root", "model" })
@Results({ @Result(name = BaseAction.SUCCESS, type = "json"),
  @Result(name = BaseAction.INPUT, type = "json") })
public class ImportUserAction extends BaseAction implements ModelDriven<VOMSUserJSON>{

  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  VOMSUserJSON user = new VOMSUserJSON();
  
  @Override
  public void validate() {
   
    if (Objects.isNull(user)){
      addFieldError("user", "user cannot be null");
    }
    if (Strings.isNullOrEmpty(user.getEmailAddress())){
      addFieldError("user.emailAddress", "emailAddress cannot be empty");
    }
    
    if (user.getCertificates() == null || user.getCertificates().isEmpty()){
      addFieldError("user.certificates", "certficates cannot be empty");
    }
    if (user.getFqans() == null || user.getFqans().isEmpty()){
      addFieldError("user.fqans", "fqans cannot be empty");
    }
  }
  
  @Override
  public String execute() throws Exception {
    
    ImportUserOperation.instance(user).execute();
    
    return SUCCESS;
  }


  public VOMSUserJSON getUser() {
    return user;
  }


  public void setUser(VOMSUserJSON user) {
    this.user = user;
  }

  @Override
  public VOMSUserJSON getModel() {
    return user;
  }

}
