package org.glite.security.voms.admin.view.actions.manager;

import java.util.List;

import org.apache.struts2.convention.annotation.Result;
import org.glite.security.voms.admin.persistence.dao.generic.DAOFactory;
import org.glite.security.voms.admin.persistence.dao.generic.GroupManagerDAO;
import org.glite.security.voms.admin.persistence.model.GroupManager;
import org.glite.security.voms.admin.view.actions.BaseAction;

@Result(name=BaseAction.SUCCESS, location="managerIndex")
public class IndexAction extends BaseAction {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	List<GroupManager> groupManagers;
	
	@Override
	public String execute() throws Exception {
	
		GroupManagerDAO dao = DAOFactory.instance().getGroupManagerDAO();
		groupManagers = dao.findAll();
		
		return SUCCESS;
	}

	
	/**
	 * @return the groupManagers
	 */
	public List<GroupManager> getGroupManagers() {
	
		return groupManagers;
	}

	
	/**
	 * @param groupManagers the groupManagers to set
	 */
	public void setGroupManagers(List<GroupManager> groupManagers) {
	
		this.groupManagers = groupManagers;
	}

	
}
