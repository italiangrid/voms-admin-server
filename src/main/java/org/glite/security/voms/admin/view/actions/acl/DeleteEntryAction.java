package org.glite.security.voms.admin.view.actions.acl;

import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.glite.security.voms.admin.dao.ACLDAO;
import org.glite.security.voms.admin.dao.VOMSAdminDAO;
import org.glite.security.voms.admin.operations.acls.DeleteACLEntryOperation;
import org.glite.security.voms.admin.view.actions.BaseAction;

@ParentPackage("base")
@Results({
	@Result(name=BaseAction.SUCCESS,location="manage", type="chain"),
	@Result(name=BaseAction.INPUT, location="deleteACLEntry")
})
public class DeleteEntryAction extends ACLActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Override
	public String execute() throws Exception {
		
		DeleteACLEntryOperation op = DeleteACLEntryOperation.instance(getModel(), getAdmin(), getPropagate());
        op.execute();
        
        ACLDAO dao = ACLDAO.instance();
        // Delete admin if it doesn't have any active permissions
        if (!admin.isInternalAdmin()){
        	if (!dao.hasActivePermissions(admin))
        		VOMSAdminDAO.instance().delete( admin );	
        }
        
        // Delete default ACL if it's empty
        if (model.isDefautlACL() && model.getPermissions().isEmpty())
        	dao.delete(model);
        	
		return SUCCESS;
	}

}
