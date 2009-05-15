package org.glite.security.voms.admin.view.actions.group;

import org.glite.security.voms.admin.dao.generic.DAOFactory;
import org.glite.security.voms.admin.model.VOMSGroup;
import org.glite.security.voms.admin.view.actions.BaseCRUDAction;

import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;

public class Group extends BaseCRUDAction<VOMSGroup,Long> implements Preparable, ModelDriven<VOMSGroup>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void prepare() throws Exception {
		// Instantiate DAO here
		setDao(DAOFactory.instance().getGroupDAO());
		
		if (getRequestId() == 0)
			model = new VOMSGroup();
		else
			model = getDao().findById(getRequestId(), true);
		
	}
	
	

}
