package org.glite.security.voms.admin.view.actions.group;

import java.util.List;

import org.glite.security.voms.admin.dao.VOMSAttributeDAO;
import org.glite.security.voms.admin.model.VOMSAttributeDescription;
import org.glite.security.voms.admin.model.VOMSGroup;
import org.glite.security.voms.admin.view.actions.BaseAction;

import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;

public class GroupActionSupport extends BaseAction implements ModelDriven<VOMSGroup>, Preparable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	Long groupId = -1L;
	VOMSGroup group;
	
	List<VOMSAttributeDescription> attributeClasses;
	
	
	public VOMSGroup getModel() {
		return group;
	}

	public void prepare() throws Exception {
		
		if (getModel() == null)
			if (getGroupId()!= -1)
				group = groupById(getGroupId());
		
		attributeClasses = (List<VOMSAttributeDescription>) VOMSAttributeDAO.instance().getAllAttributeDescriptions();
	}

	public Long getGroupId() {
		return groupId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

	public List<VOMSAttributeDescription> getAttributeClasses() {
		return attributeClasses;
	}

	public void setAttributeClasses(List<VOMSAttributeDescription> attributeClasses) {
		this.attributeClasses = attributeClasses;
	}
	
	
	
}
