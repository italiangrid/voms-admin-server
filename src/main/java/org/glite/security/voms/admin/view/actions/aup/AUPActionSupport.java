package org.glite.security.voms.admin.view.actions.aup;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.glite.security.voms.admin.dao.generic.AUPDAO;
import org.glite.security.voms.admin.dao.generic.DAOFactory;
import org.glite.security.voms.admin.model.AUP;
import org.glite.security.voms.admin.view.actions.BaseAction;

import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;

@ParentPackage("base")
@Results({
	@Result(name=BaseAction.INPUT, location="aups")
})

public class AUPActionSupport extends BaseAction implements Preparable, ModelDriven<AUP>{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	AUP voAUP;
	
	public void prepare() throws Exception {
		
		AUPDAO dao = DAOFactory.instance().getAUPDAO();
			
		if (voAUP == null)
			voAUP = dao.getVOAUP();
		
	}



	@Action("load")
	public String load() throws Exception{
		
		return INPUT;
	}


	public AUP getModel() {
		
		return voAUP;
	}
	
}
