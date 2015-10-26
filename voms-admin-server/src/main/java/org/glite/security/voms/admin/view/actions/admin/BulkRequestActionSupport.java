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
package org.glite.security.voms.admin.view.actions.admin;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.glite.security.voms.admin.persistence.model.request.Request;
import org.glite.security.voms.admin.view.actions.BaseAction;
import org.glite.security.voms.admin.view.util.RequestUtil;

import com.opensymphony.xwork2.Preparable;

public class BulkRequestActionSupport extends BaseAction implements Preparable {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  List<Long> requestIds;

  Map<Long, Request> requestMap = new HashMap<Long, Request>();

  List<Request> selectedRequests = new ArrayList<Request>();

  @Override
  public void validate() {

    if (requestIds == null) {
      addActionError("No requests selected!");
    } else if (requestIds.contains("ognl.NoConversionPossible")) {
      addActionError("No requests selected!");
    }
  }

  public List<Long> getRequestIds() {

    return requestIds;
  }

  public void setRequestIds(List<Long> requestIds) {

    this.requestIds = requestIds;
  }

  protected void refreshRequests() {

    requestMap.clear();
    List<Request> manageableRequests = RequestUtil.findManageableRequests();

    for (Request r : manageableRequests) {
      requestMap.put(r.getId(), r);
    }

  }

  @Override
  public void prepare() throws Exception {

    refreshRequests();
    selectedRequests.clear();

    if (requestIds != null) {

      for (Long id : requestIds) {
        Request r = requestMap.get(id);
        if (r != null) {
          selectedRequests.add(r);
        }
      }
    }

  }

  public List<Request> getSelectedRequests() {

    return selectedRequests;
  }

  public void setSelectedRequests(List<Request> selectedRequests) {

    this.selectedRequests = selectedRequests;
  }

  public Collection<Request> getPendingRequests() {

    if (requestMap.isEmpty()) {
      return Collections.emptyList();
    }

    return requestMap.values();
  }

}
