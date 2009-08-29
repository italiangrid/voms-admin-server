package org.glite.security.voms.admin.view.actions.aup;

import java.util.Date;

import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.glite.security.voms.admin.database.HibernateFactory;
import org.glite.security.voms.admin.view.actions.BaseAction;

@ParentPackage("base")
@Results( { @Result(name = BaseAction.INPUT, location = "aups"),
		@Result(name = BaseAction.SUCCESS, location = "aups") })
public class TriggerAcceptanceAction extends AUPActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9160161595423900168L;

	@Override
	public String execute() throws Exception {
		getModel().getActiveVersion().setLastForcedReacceptanceTime(new Date());
		HibernateFactory.getSession().update(getModel());
		return SUCCESS;
	}

}
