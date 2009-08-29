package org.glite.security.voms.admin.view.actions.role;

import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.glite.security.voms.admin.view.actions.BaseAction;

@ParentPackage("base")
@Results( { @Result(name = BaseAction.SUCCESS, location = "roleDetail"),
		@Result(name = BaseAction.INPUT, location = "roleDetail") })
public class EditAction extends RoleActionSupport {

}
