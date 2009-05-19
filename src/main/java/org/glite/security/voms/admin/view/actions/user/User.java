package org.glite.security.voms.admin.view.actions.user;

import org.glite.security.voms.admin.dao.generic.DAOFactory;
import org.glite.security.voms.admin.model.VOMSUser;
import org.glite.security.voms.admin.view.actions.BaseCRUDAction;

import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;

public class User extends BaseCRUDAction<VOMSUser, Long> implements Preparable, ModelDriven<VOMSUser> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	public User() {
		
		dao = DAOFactory.instance().getUserDAO();
		
	}
	
	public void prepare() throws Exception {
		if (getRequestId() == 0)
			model = new VOMSUser();
		else{
			model = dao.findById(getRequestId(), false);
		}
		
	}

}
