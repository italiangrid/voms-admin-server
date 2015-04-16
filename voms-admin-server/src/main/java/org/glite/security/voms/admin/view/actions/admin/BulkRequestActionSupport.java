package org.glite.security.voms.admin.view.actions.admin;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.glite.security.voms.admin.persistence.model.request.Request;
import org.glite.security.voms.admin.view.actions.BaseAction;
import org.glite.security.voms.admin.view.util.RequestsUtil;

import com.opensymphony.xwork2.Preparable;

public class BulkRequestActionSupport extends BaseAction implements Preparable {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  List<Long> requestIds;

  Map<Long, Request> pendingRequestMap = new HashMap<Long, Request>();

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

  protected void refreshManageableRequests(){
    pendingRequestMap.clear();
    List<Request> manageableRequests = RequestsUtil.findManageableRequests();

    for (Request r : manageableRequests) {
      pendingRequestMap.put(r.getId(), r);
    }
  }
  
  @Override
  public void prepare() throws Exception {

    refreshManageableRequests();
    selectedRequests.clear();
    
    if (requestIds != null) {

      for (Long id : requestIds) {
        Request r = pendingRequestMap.get(id);
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
    if (pendingRequestMap.isEmpty()){
      return Collections.emptyList();
    }

    return pendingRequestMap.values();
  }
  
}
