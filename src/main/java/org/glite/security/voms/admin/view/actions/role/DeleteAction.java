package org.glite.security.voms.admin.view.actions.role;

import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.glite.security.voms.admin.model.VOMSRole;
import org.glite.security.voms.admin.operations.roles.DeleteRoleOperation;
import org.glite.security.voms.admin.view.actions.BaseAction;

@ParentPackage("base")
@Results( {
		@Result(name = BaseAction.SUCCESS, location = "search", type = "chain"),
		@Result(name = BaseAction.INPUT, location = "roles") })
public class DeleteAction extends RoleActionSupport {

	@Override
	public String execute() throws Exception {

		VOMSRole r = (VOMSRole) DeleteRoleOperation.instance(getModel())
				.execute();
		if (r != null)
			addActionMessage(getText("confirm.role.deletion", new String[]{r.getName()}));
		return SUCCESS;
	}

}
