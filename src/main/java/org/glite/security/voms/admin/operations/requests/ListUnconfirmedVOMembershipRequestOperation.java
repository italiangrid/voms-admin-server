package org.glite.security.voms.admin.operations.requests;

import org.glite.security.voms.admin.persistence.dao.generic.DAOFactory;
import org.glite.security.voms.admin.persistence.dao.generic.RequestDAO;

public class ListUnconfirmedVOMembershipRequestOperation extends RequestReadOperation{

	private ListUnconfirmedVOMembershipRequestOperation() {
		// TODO Auto-generated constructor stub
	}
	
	public static ListUnconfirmedVOMembershipRequestOperation instance(){
		
		return new ListUnconfirmedVOMembershipRequestOperation();
	}
	
	@Override
	protected Object doExecute() {
		RequestDAO dao = DAOFactory.instance().getRequestDAO();
		
		return dao.findPendingVOMembershipRequests();
	}

}
