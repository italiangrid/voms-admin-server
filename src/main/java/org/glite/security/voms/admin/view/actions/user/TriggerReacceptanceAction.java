package org.glite.security.voms.admin.view.actions.user;

import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.glite.security.voms.admin.dao.generic.AUPDAO;
import org.glite.security.voms.admin.dao.generic.DAOFactory;
import org.glite.security.voms.admin.operations.aup.TriggerReacceptanceOperation;
import org.glite.security.voms.admin.view.actions.BaseAction;


@ParentPackage("base")
@Results( {
		@Result(name = BaseAction.SUCCESS, location = "userDetail"),
		@Result(name = BaseAction.INPUT, location = "usersDetail") })

public class TriggerReacceptanceAction extends UserActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Override
	public String execute() throws Exception {
		
		AUPDAO aupDAO = DAOFactory.instance().getAUPDAO();
		
		new TriggerReacceptanceOperation(aupDAO.getVOAUP(), getModel()).execute();
		
		return SUCCESS;
	}

	
}
