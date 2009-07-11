package org.glite.security.voms.admin.view.actions.group;

import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.glite.security.voms.admin.view.actions.BaseAction;

@ParentPackage("base")

@Results({ 
	@Result(name=BaseAction.SUCCESS,location="groupDetail"),
	@Result(name=BaseAction.INPUT,location="groupDetail")
})
public class EditAction extends GroupActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
}
