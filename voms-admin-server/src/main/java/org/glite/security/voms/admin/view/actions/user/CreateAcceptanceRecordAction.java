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
package org.glite.security.voms.admin.view.actions.user;

import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.interceptor.TokenInterceptor;
import org.glite.security.voms.admin.operations.aup.CreateAcceptanceRecordOperation;
import org.glite.security.voms.admin.persistence.dao.generic.AUPDAO;
import org.glite.security.voms.admin.persistence.dao.generic.DAOFactory;
import org.glite.security.voms.admin.view.actions.BaseAction;

@Results({
  @Result(name = BaseAction.SUCCESS, location = "userDetail"),
  @Result(name = BaseAction.INPUT, location = "userDetail"),
  @Result(name = TokenInterceptor.INVALID_TOKEN_CODE,
    location = "userDetail") })
@InterceptorRef(value = "authenticatedStack", params = {
  "token.includeMethods", "execute" })
public class CreateAcceptanceRecordAction extends UserActionSupport {

  /**
     * 
     */
  private static final long serialVersionUID = 1L;

  @Override
  public String execute() throws Exception {

    AUPDAO aupDAO = DAOFactory.instance().getAUPDAO();

    new CreateAcceptanceRecordOperation(aupDAO.getVOAUP(), getModel())
      .execute();

    addActionMessage("AUP acceptance record created.");

    return SUCCESS;
  }

}
