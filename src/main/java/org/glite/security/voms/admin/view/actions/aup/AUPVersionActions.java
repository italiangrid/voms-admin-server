package org.glite.security.voms.admin.view.actions.aup;

import org.glite.security.voms.admin.dao.generic.AUPDAO;
import org.glite.security.voms.admin.dao.generic.DAOFactory;
import org.glite.security.voms.admin.model.AUP;
import org.glite.security.voms.admin.view.actions.BaseAction;

import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import com.opensymphony.xwork2.validator.annotations.RequiredStringValidator;
import com.opensymphony.xwork2.validator.annotations.ValidatorType;


public abstract class AUPVersionActions extends BaseAction implements Preparable, ModelDriven<AUP>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	Long aupId;
	
	
	AUP aup;
	
	String version;
		
	public void prepare() throws Exception {
		if (aup == null){
			
			AUPDAO dao = DAOFactory.instance().getAUPDAO();
			aup = dao.findById(aupId, true);
		}
		
	}

	public AUP getModel() {
		
		return aup;
	}
	
	
	@RequiredStringValidator(type=ValidatorType.FIELD, message="The version string is required")
	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	
	public Long getAupId() {
		return aupId;
	}

	public void setAupId(Long aupId) {
		this.aupId = aupId;
	}
	
	
}
