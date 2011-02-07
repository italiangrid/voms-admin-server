package org.glite.security.voms.admin.view.actions.user;

import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.interceptor.TokenInterceptor;
import org.glite.security.voms.admin.operations.aup.CreateAcceptanceRecordOperation;
import org.glite.security.voms.admin.persistence.dao.generic.AUPDAO;
import org.glite.security.voms.admin.persistence.dao.generic.DAOFactory;
import org.glite.security.voms.admin.view.actions.BaseAction;

@ParentPackage("base")
@Results( {
		@Result(name = BaseAction.SUCCESS, location = "aupHistory.jsp"),
		@Result(name = BaseAction.INPUT, location = "aupHistory.jsp"),
		@Result(name = TokenInterceptor.INVALID_TOKEN_CODE, location ="aupHistory.jsp")})

@InterceptorRef(value = "authenticatedStack", params = {
		"token.includeMethods", "execute" })
public class CreateAcceptanceRecordAction extends UserActionSupport {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    @Override
    public String execute() throws Exception {
        
	AUPDAO aupDAO = DAOFactory.instance().getAUPDAO();
	
	new CreateAcceptanceRecordOperation(aupDAO.getVOAUP(), getModel()).execute();
	addActionMessage("AUP acceptance record created.");
	
        return SUCCESS;
    }

}
