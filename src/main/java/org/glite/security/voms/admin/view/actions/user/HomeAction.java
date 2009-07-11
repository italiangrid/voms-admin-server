package org.glite.security.voms.admin.view.actions.user;

import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.glite.security.voms.admin.common.VOMSException;
import org.glite.security.voms.admin.operations.CurrentAdmin;
import org.glite.security.voms.admin.view.actions.BaseAction;

@ParentPackage("base")
@Results({
	@Result(name=BaseAction.SUCCESS,location="userHome"),
	@Result(name=BaseAction.INPUT, location="userHome")
})


public class HomeAction extends UserActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Override
	public void prepare() throws Exception {
		if (CurrentAdmin.instance().getVoUser() ==  null)
			throw new VOMSException("Current authenticated client has no corresponding VO membership!");
			
		model = CurrentAdmin.instance().getVoUser();
	}
}
