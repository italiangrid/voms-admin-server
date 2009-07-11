package org.glite.security.voms.admin.view.actions.acl;

import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.glite.security.voms.admin.dao.ACLDAO;
import org.glite.security.voms.admin.model.VOMSGroup;
import org.glite.security.voms.admin.view.actions.BaseAction;

@ParentPackage("base")
@Results({
	@Result(name=BaseAction.SUCCESS,location="aclDetail"),
	@Result(name=BaseAction.INPUT, location="addACLEntry")
})
public class AddDefaultEntryAction extends AddEntryAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	long aclGroupId = -1;
	
	@Override
	public void prepare() throws Exception {
		
		if (getModel()==null){
			
			VOMSGroup g = groupById(aclGroupId);
			if (g.getDefaultACL() == null)
				// FIXME: do it with an operation
				model = ACLDAO.instance().create( g, true );
			else
				model = g.getDefaultACL();
		}
		
		super.prepare();
		
	}

	public long getAclGroupId() {
		return aclGroupId;
	}

	public void setAclGroupId(long aclGroupId) {
		this.aclGroupId = aclGroupId;
	}
	
	
}
