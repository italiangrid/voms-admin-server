package org.glite.security.voms.admin.operations.group_manager;

import org.glite.security.voms.admin.operations.BaseVomsOperation;
import org.glite.security.voms.admin.operations.VOMSContext;
import org.glite.security.voms.admin.operations.VOMSPermission;
import org.glite.security.voms.admin.persistence.dao.generic.DAOFactory;
import org.glite.security.voms.admin.persistence.dao.generic.GroupManagerDAO;
import org.glite.security.voms.admin.persistence.model.GroupManager;


public abstract class BaseGroupManagerOperation extends BaseVomsOperation {

	protected GroupManager manager;
	protected GroupManagerDAO dao;
	
	protected BaseGroupManagerOperation(GroupManager m) {
		
		manager = m;
		dao = DAOFactory.instance().getGroupManagerDAO();
	}

	@Override
	protected void setupPermissions() {

		addRequiredPermission(VOMSContext.getVoContext(), 
			VOMSPermission.getAllPermissions());

	}

	
	/**
	 * @return the manager
	 */
	public GroupManager getManager() {
	
		return manager;
	}

	
	/**
	 * @param manager the manager to set
	 */
	public void setManager(GroupManager manager) {
	
		this.manager = manager;
	}
	

}
