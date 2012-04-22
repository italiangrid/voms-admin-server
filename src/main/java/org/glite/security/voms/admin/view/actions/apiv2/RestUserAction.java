package org.glite.security.voms.admin.view.actions.apiv2;

import java.util.Collection;

import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.json.annotations.JSON;
import org.glite.security.voms.admin.persistence.dao.VOMSUserDAO;
import org.glite.security.voms.admin.persistence.model.VOMSUser;
import org.glite.security.voms.admin.view.actions.BaseAction;

import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import com.opensymphony.xwork2.validator.annotations.RequiredStringValidator;
import com.opensymphony.xwork2.validator.annotations.ValidatorType;

@ParentPackage("json")
@Results( { @Result(name = BaseAction.SUCCESS, type = "json") })
public class RestUserAction extends BaseAction 
	implements Preparable, ModelDriven<VOMSUser>{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	VOMSUser user;
	
	Long userId;
	
	String certificateSubject;
	String caSubject;
	
	public VOMSUser getModel() {
		
		return user;
	}

	public void prepare() throws Exception {
				
		if (getModel() == null){
			
			if ((certificateSubject == null) || (caSubject == null)){
				return;
			}
			
			user = VOMSUserDAO.instance().findByCertificate(certificateSubject, caSubject);
		}
	}

	@RequiredStringValidator(type=ValidatorType.FIELD, message="Please provide a DN for the user.")
	public String getCertificateSubject() {
		return certificateSubject;
	}

	@RequiredStringValidator(type=ValidatorType.FIELD, message="Please provide a CA for the user.")
	public String getCaSubject() {
		return caSubject;
	}
	

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public void setCertificateSubject(String certificateSubject) {
		this.certificateSubject = certificateSubject;
	}

	public void setCaSubject(String caSubject) {
		this.caSubject = caSubject;
	}

	@JSON(serialize=false)
	public Long getUserId() {
		return userId;
	}

}
