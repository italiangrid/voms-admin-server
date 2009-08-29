package org.glite.security.voms.admin.view.actions.group;

import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.glite.security.voms.admin.model.VOMSGroup;
import org.glite.security.voms.admin.operations.groups.DeleteGroupOperation;
import org.glite.security.voms.admin.view.actions.BaseAction;

@ParentPackage("base")
@Results( { @Result(name = BaseAction.SUCCESS, location = "/group/search.action", type = "redirect") })
public class DeleteAction extends GroupActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public String execute() throws Exception {

		VOMSGroup g = (VOMSGroup) DeleteGroupOperation.instance(getModel())
				.execute();

		if (g != null)
			addActionMessage(getText("confirm.group.deletion", g.getName()));

		return SUCCESS;
	}

}
