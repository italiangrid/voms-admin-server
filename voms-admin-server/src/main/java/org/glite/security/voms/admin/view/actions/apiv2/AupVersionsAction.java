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

import java.util.ArrayList;
import java.util.List;

import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.glite.security.voms.admin.apiv2.AUPVersionJSON;
import org.glite.security.voms.admin.persistence.dao.generic.AUPVersionDAO;
import org.glite.security.voms.admin.persistence.dao.generic.DAOFactory;
import org.glite.security.voms.admin.persistence.model.AUPVersion;
import org.glite.security.voms.admin.view.actions.BaseAction;

@ParentPackage("json")
@Results({@Result(name = BaseAction.SUCCESS, type = "json", params = {"root", "result"})})
public class AupVersionsAction extends BaseAction {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  List<AUPVersionJSON> result;

  @Override
  public String execute() throws Exception {

    AUPVersionDAO dao = DAOFactory.instance().getAUPVersionDAO();

    List<AUPVersion> versions = dao.findAll();
    
    result = new ArrayList<>();
    
    for (AUPVersion v: versions){
      result.add(AUPVersionJSON.fromAUPVersion(v));
    }
    
    return SUCCESS;
  }

  public List<AUPVersionJSON> getResult() {
    return result;
  }

}
