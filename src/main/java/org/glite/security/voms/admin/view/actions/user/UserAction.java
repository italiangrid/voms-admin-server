package org.glite.security.voms.admin.view.actions.user;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.glite.security.voms.admin.model.VOMSUser;
import org.glite.security.voms.admin.view.actions.BaseAction;

import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;


@ParentPackage("base")

public class UserAction extends BaseAction implements ModelDriven<VOMSUser>, Preparable{

	public static Log log = LogFactory.getLog(UserAction.class);
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	VOMSUser model;
	String caSubject;
	
	
	
	public String preCreate(){
		
		log.debug("preCreate:");
		
		return EDIT;
		
	}
	
	public String create(){
		
		log.debug("create:");
		
		return LIST;
	}
	
	public String save(){
		
		log.debug("save:");
		
		if (getModel() == null){
			addActionError("Cannot save a null user!");
			return LIST;
		}
		
		return LIST;
	}
	
	public String delete(){
		
		log.debug("delete:");
		
		return LIST;
	
	}

	public VOMSUser getModel() {
		
		return model;
	}

	
	public void setModel(VOMSUser model) {
		this.model = model;
	}

	public String getCaSubject() {
		return caSubject;
	}

	public void setCaSubject(String caSubject) {
		this.caSubject = caSubject;
	}

	public void prepare() throws Exception {
		
		
	}
	

}
