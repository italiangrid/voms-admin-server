package org.glite.security.voms.admin.view.actions.acl;

import org.apache.struts2.convention.annotation.ParentPackage;
import org.glite.security.voms.admin.dao.ACLDAO;
import org.glite.security.voms.admin.dao.VOMSAdminDAO;
import org.glite.security.voms.admin.model.ACL;
import org.glite.security.voms.admin.model.VOMSAdmin;
import org.glite.security.voms.admin.view.actions.BaseAction;

import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;

@ParentPackage("base")
public abstract class ACLActionSupport extends BaseAction implements Preparable, ModelDriven<ACL>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	Long aclId = -1L;
	Long adminId = -1L;
	
	ACL model;
	VOMSAdmin admin;
	
	
	protected Boolean propagate;
	
	public void prepare() throws Exception {
		
		if (getModel()==null){
			
			if (getAclId()!= -1)
				model = ACLDAO.instance().getById(getAclId());
			
			if (getAdminId() != -1)
				admin = VOMSAdminDAO.instance().getById(getAdminId());
				
		}
		
		
	}

	public ACL getModel() {

		return model;
	}

	
	public Long getAclId() {
		return aclId;
	}

	public void setAclId(Long aclId) {
		this.aclId = aclId;
	}

	public Long getAdminId() {
		return adminId;
	}

	public void setAdminId(Long adminId) {
		this.adminId = adminId;
	}

	
	public VOMSAdmin getAdmin() {
		return admin;
	}

	public void setAdmin(VOMSAdmin admin) {
		this.admin = admin;
	}
	
	public Boolean getPropagate() {
		return propagate;
	}

	public void setPropagate(Boolean propagate) {
		this.propagate = propagate;
	}
	
}
