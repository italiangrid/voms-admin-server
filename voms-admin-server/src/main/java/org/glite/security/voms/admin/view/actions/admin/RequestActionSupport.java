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
package org.glite.security.voms.admin.view.actions.admin;

import java.util.List;

import org.apache.struts2.convention.annotation.Results;
import org.glite.security.voms.admin.persistence.dao.generic.DAOFactory;
import org.glite.security.voms.admin.persistence.model.request.Request;
import org.glite.security.voms.admin.view.actions.BaseAction;
import org.glite.security.voms.admin.view.util.RequestsUtil;

import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;

@Results({})
public class RequestActionSupport extends BaseAction implements Preparable,
  ModelDriven<Request> {

  Long requestId = -1L;

  Request request;

  List<Request> pendingRequests;

  /**
	 * 
	 */
  private static final long serialVersionUID = 1L;

  protected void refreshPendingRequests() {

    pendingRequests = RequestsUtil.findManageableRequests();

  }

  public void prepare() throws Exception {

    if (request == null) {

      if (requestId != -1L)
        request = DAOFactory.instance().getRequestDAO()
          .findById(requestId, true);
    }

    refreshPendingRequests();
  }

  public Request getModel() {

    return request;
  }

  public Long getRequestId() {

    return requestId;
  }

  public void setRequestId(Long requestId) {

    this.requestId = requestId;
  }

  public List<Request> getPendingRequests() {

    return pendingRequests;
  }

}
