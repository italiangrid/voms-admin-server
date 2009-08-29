package org.glite.security.voms.admin.view.actions.admin;

import java.util.List;

import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.glite.security.voms.admin.dao.generic.DAOFactory;
import org.glite.security.voms.admin.model.request.NewVOMembershipRequest;
import org.glite.security.voms.admin.view.actions.BaseAction;

@ParentPackage("base")
@Result(name = BaseAction.SUCCESS, location = "adminHome")
public class HomeAction extends AdminActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	List<NewVOMembershipRequest> pendingRequests;

	@Override
	public void prepare() throws Exception {

		pendingRequests = DAOFactory.instance().getRequestDAO()
				.findConfirmedVOMembershipRequests();
		super.prepare();
	}

	public List<NewVOMembershipRequest> getPendingRequests() {
		return pendingRequests;
	}

}
