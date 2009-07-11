package org.glite.security.voms.admin.view.actions.request;

import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Results;
import org.glite.security.voms.admin.dao.generic.DAOFactory;
import org.glite.security.voms.admin.model.request.Request;
import org.glite.security.voms.admin.view.actions.BaseAction;

import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;

@ParentPackage("base")
@Results({})
public class RequestActionSupport extends BaseAction implements Preparable, ModelDriven<Request> {
	

	Long requestId = -1L;
	
	Request request;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	public void prepare() throws Exception {
		if (request  == null){
			
			if (requestId != -1L)
				request = DAOFactory.instance().getRequestDAO().findById(requestId, true);
		}
		
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

	
	
}
