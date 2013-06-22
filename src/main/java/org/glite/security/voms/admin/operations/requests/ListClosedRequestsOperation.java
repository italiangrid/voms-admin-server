package org.glite.security.voms.admin.operations.requests;

import org.glite.security.voms.admin.persistence.dao.generic.DAOFactory;
import org.glite.security.voms.admin.persistence.dao.generic.RequestDAO;

public class ListClosedRequestsOperation extends RequestReadOperation {

	@Override
	protected Object doExecute() {
		
		RequestDAO dao = DAOFactory.instance().getRequestDAO();
		return dao.findClosedRequests();
	}

}
