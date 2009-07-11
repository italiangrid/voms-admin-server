package org.glite.security.voms.admin.view.actions.group;

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
	
	
	public VOMSGroup getModel() {
		return group;
	}

	public void prepare() throws Exception {
		
		if (getModel() == null)
			if (getGroupId()!= -1)
				group = groupById(getGroupId());
		
	}

	public Long getGroupId() {
		return groupId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}
	
	

}
