package org.glite.security.voms.admin.view.actions.register;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.glite.security.voms.admin.dao.generic.DAOFactory;
import org.glite.security.voms.admin.dao.generic.RequestDAO;
import org.glite.security.voms.admin.model.request.Request.StatusFlag;
import org.glite.security.voms.admin.view.actions.BaseAction;

@ParentPackage("base")
@Results({
	@Result(name=BaseAction.SUCCESS,location="registrationCancelled"),
	@Result(name = BaseAction.ERROR, location = "registrationConfirmationError")
})
public class CancelRequestAction extends RegisterActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final Log log = LogFactory.getLog(CancelRequestAction.class);
	
	String confirmationId;
	
	@Override
	public String execute() throws Exception {
		
		if (!registrationEnabled())
			return REGISTRATION_DISABLED;
		
		if (!request.getStatus().equals(StatusFlag.SUBMITTED))
			throw new IllegalArgumentException(
					"Cannot cancel an already confirmed request!");
		
		if (request.getConfirmId().equals(confirmationId)){
			
			RequestDAO rDAO = DAOFactory.instance().getRequestDAO();
			log.info("Removing request '"+request+"' on user's request.");
			
			rDAO.makeTransient(request);
			return SUCCESS;
		}
			
		return ERROR;
	}

	public String getConfirmationId() {
		return confirmationId;
	}

	public void setConfirmationId(String confirmationId) {
		this.confirmationId = confirmationId;
	}
	
}
