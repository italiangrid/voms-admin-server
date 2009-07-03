package org.glite.security.voms.admin.view.actions.aup;

import java.net.URL;

import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.glite.security.voms.admin.dao.generic.AUPDAO;
import org.glite.security.voms.admin.dao.generic.DAOFactory;
import org.glite.security.voms.admin.view.actions.BaseAction;

import com.opensymphony.xwork2.validator.annotations.RequiredStringValidator;
import com.opensymphony.xwork2.validator.annotations.ValidatorType;

@ParentPackage("base")
@Results({
	@Result(name=BaseAction.INPUT, location="addAupVersion"),
	@Result(name=BaseAction.SUCCESS, location="/aup/load.action", type="redirect")
})
public class AddVersionAction extends AUPVersionActions {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String url;
	
	@Override
	public String execute() throws Exception {
	
		AUPDAO dao  = DAOFactory.instance().getAUPDAO();
		
		dao.addVersion(getModel(), getVersion(), new URL(getUrl()));
		return SUCCESS;
		
	}
	
	@RequiredStringValidator(type=ValidatorType.FIELD, message="The url field is required!")
	public String getUrl() {
		return url;
	}


	public void setUrl(String url) {
		this.url = url;
	}
}
