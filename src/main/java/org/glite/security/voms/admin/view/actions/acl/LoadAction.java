package org.glite.security.voms.admin.view.actions.acl;

import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.glite.security.voms.admin.dao.ACLDAO;
import org.glite.security.voms.admin.model.VOMSGroup;
import org.glite.security.voms.admin.view.actions.BaseAction;

@ParentPackage("base")
@Results( { @Result(name = BaseAction.SUCCESS, location = "aclDetail"),
		@Result(name = BaseAction.INPUT, location = "aclDetail") })
public class LoadAction extends ACLActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
