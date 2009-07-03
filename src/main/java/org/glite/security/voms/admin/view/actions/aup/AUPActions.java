package org.glite.security.voms.admin.view.actions.aup;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.glite.security.voms.admin.dao.generic.AUPDAO;
import org.glite.security.voms.admin.dao.generic.DAOFactory;
import org.glite.security.voms.admin.model.AUP;
import org.glite.security.voms.admin.view.actions.BaseAction;

import com.opensymphony.xwork2.Preparable;

@ParentPackage("base")
@Results({
	@Result(name=BaseAction.INPUT, location="aups")
})

public class AUPActions extends BaseAction implements Preparable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	AUP gridAUP;
	AUP voAUP;
	
	public void prepare() throws Exception {
		
		AUPDAO dao = DAOFactory.instance().getAUPDAO();
		
		if (gridAUP == null)
			gridAUP = dao.getGridAUP();
		
		if (voAUP == null)
			voAUP = dao.getVOAUP();
		
	}



	public AUP getGridAUP() {
		return gridAUP;
	}



	public void setGridAUP(AUP gridAUP) {
		this.gridAUP = gridAUP;
	}



	public AUP getVoAUP() {
		return voAUP;
	}



	public void setVoAUP(AUP voAUP) {
		this.voAUP = voAUP;
	}


	@Action("load")
	public String load() throws Exception{
		
		return INPUT;
	}
	
}
