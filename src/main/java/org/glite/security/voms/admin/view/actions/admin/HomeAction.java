package org.glite.security.voms.admin.view.actions.admin;

import java.util.List;

import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.glite.security.voms.admin.dao.generic.DAOFactory;
import org.glite.security.voms.admin.dao.generic.RequestDAO;
import org.glite.security.voms.admin.model.request.Request;
import org.glite.security.voms.admin.view.actions.BaseAction;

@ParentPackage("base")
@Result(name = BaseAction.SUCCESS, location = "adminHome")
public class HomeAction extends AdminActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	List<Request> pendingRequests;

	@Override
	public void prepare() throws Exception {

		RequestDAO rDAO = DAOFactory.instance().getRequestDAO();
		
		pendingRequests = rDAO.findPendingRequests();
		super.prepare();
	}

	public List<Request> getPendingRequests() {
		return pendingRequests;
	}
}
