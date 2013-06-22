package org.glite.security.voms.admin.view.actions.manager;

import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.glite.security.voms.admin.operations.group_manager.DeleteGroupManagerOperation;
import org.glite.security.voms.admin.persistence.dao.generic.DAOFactory;
import org.glite.security.voms.admin.persistence.dao.generic.GroupManagerDAO;
import org.glite.security.voms.admin.persistence.model.GroupManager;
import org.glite.security.voms.admin.view.actions.BaseAction;

import com.opensymphony.xwork2.Preparable;

@Results({
		@Result(name=BaseAction.SUCCESS, location="index", type="redirectAction")
})

public class DeleteAction extends BaseAction implements Preparable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	Long id;
	GroupManager manager;

	
	@Override
	public void prepare() throws Exception {

		GroupManagerDAO dao = DAOFactory.instance().getGroupManagerDAO();
		manager = dao.findById(id, true);
	}

	@Override
	public String execute() throws Exception {
		
		if (manager != null){
			DeleteGroupManagerOperation op = new DeleteGroupManagerOperation(manager);
			op.execute();
		}
				
		addActionMessage("Manager succesfully deleted: "+manager.getName());
		return SUCCESS;
	}
	
	/**
	 * @return the id
	 */
	public Long getId() {
	
		return id;
	}

	
	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
	
		this.id = id;
	}

	

}
