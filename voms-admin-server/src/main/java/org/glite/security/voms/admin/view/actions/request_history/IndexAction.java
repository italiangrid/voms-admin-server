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
package org.glite.security.voms.admin.view.actions.request_history;

import java.util.List;

import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.glite.security.voms.admin.operations.requests.ListClosedRequestsOperation;
import org.glite.security.voms.admin.persistence.model.request.Request;
import org.glite.security.voms.admin.view.actions.BaseAction;

@Results({ @Result(name = BaseAction.SUCCESS, location = "requestHistory") })
public class IndexAction extends BaseAction {

  /**
	 * 
	 */
  private static final long serialVersionUID = 1L;

  List<Request> closedRequests;

  @Override
  public String execute() throws Exception {

    ListClosedRequestsOperation op = new ListClosedRequestsOperation();
    closedRequests = (List<Request>) op.execute();

    return SUCCESS;
  }

  /**
   * @return the closedRequests
   */
  public List<Request> getClosedRequests() {

    return closedRequests;
  }

  /**
   * @param closedRequests
   *          the closedRequests to set
   */
  public void setClosedRequests(List<Request> closedRequests) {

    this.closedRequests = closedRequests;
  }

}
