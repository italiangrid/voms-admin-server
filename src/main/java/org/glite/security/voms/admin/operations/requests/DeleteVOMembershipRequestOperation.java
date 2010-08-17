package org.glite.security.voms.admin.operations.requests;


import org.glite.security.voms.admin.error.NotFoundException;
import org.glite.security.voms.admin.persistence.dao.generic.DAOFactory;
import org.glite.security.voms.admin.persistence.dao.generic.RequestDAO;
import org.glite.security.voms.admin.persistence.model.request.NewVOMembershipRequest;

public class DeleteVOMembershipRequestOperation extends RequestRWOperation {

	Long requestId;
	NewVOMembershipRequest request;
	
	public DeleteVOMembershipRequestOperation(Long requestId) {
		
		RequestDAO dao = DAOFactory.instance().getRequestDAO();
		request = (NewVOMembershipRequest) dao.findById(requestId, true);
		if (request  == null)
			throw new NotFoundException("No VO membership request found for id "+requestId);
	}
	
	public DeleteVOMembershipRequestOperation(NewVOMembershipRequest req){
		
		this.request = req;  
	}
	
	
	@Override
	protected Object doExecute() {
		
		RequestDAO dao = DAOFactory.instance().getRequestDAO();
		
		dao.makeTransient(request);
		
		return request;
	}

}
