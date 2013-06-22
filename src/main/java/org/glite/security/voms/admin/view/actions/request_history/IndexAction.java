package org.glite.security.voms.admin.view.actions.request_history;

import java.util.List;

import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.glite.security.voms.admin.operations.requests.ListClosedRequestsOperation;
import org.glite.security.voms.admin.persistence.model.request.Request;
import org.glite.security.voms.admin.view.actions.BaseAction;

@Results({
	@Result(name= BaseAction.SUCCESS, location="requestHistory")
})
public class IndexAction extends BaseAction{

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
	 * @param closedRequests the closedRequests to set
	 */
	public void setClosedRequests(List<Request> closedRequests) {
	
		this.closedRequests = closedRequests;
	}

	
}
