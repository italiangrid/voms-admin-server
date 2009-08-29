package org.glite.security.voms.admin.view.actions.role;

import java.util.List;

import org.glite.security.voms.admin.dao.VOMSAttributeDAO;
import org.glite.security.voms.admin.model.VOMSAttributeDescription;
import org.glite.security.voms.admin.model.VOMSGroup;
import org.glite.security.voms.admin.model.VOMSRole;
import org.glite.security.voms.admin.operations.groups.ListGroupsOperation;
import org.glite.security.voms.admin.view.actions.BaseAction;

import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;

public class RoleActionSupport extends BaseAction implements ModelDriven<VOMSRole>, Preparable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	Long roleId = -1L;
	
	VOMSRole role;
	
	List<VOMSAttributeDescription> attributeClasses;
	List<VOMSGroup> voGroups;
	
	
	public VOMSRole getModel() {
		return role;
	}

	public void prepare() throws Exception {
	
		if (getModel()==null){
			
			if (roleId != -1)
				role = roleById(roleId);
		}
		
		attributeClasses = (List<VOMSAttributeDescription>)VOMSAttributeDAO.instance().getAllAttributeDescriptions();
		voGroups = (List<VOMSGroup>)ListGroupsOperation.instance().execute();
	}

	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	
	public List<VOMSGroup> getVoGroups() {
		return voGroups;
	}

	public void setVoGroups(List<VOMSGroup> voGroups) {
		this.voGroups = voGroups;
	}

	public List<VOMSAttributeDescription> getAttributeClasses() {
		return attributeClasses;
	}
	
}
