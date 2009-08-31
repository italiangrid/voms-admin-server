package org.glite.security.voms.admin.view.actions.user;

import org.apache.struts2.convention.annotation.ParentPackage;
import org.glite.security.voms.admin.model.VOMSUser;
import org.glite.security.voms.admin.view.actions.BaseAction;

import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;

@ParentPackage("base")
public class UserActionSupport extends BaseAction implements
		ModelDriven<VOMSUser>, Preparable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	Long userId = -1L;

	VOMSUser model;

	public VOMSUser getModel() {

		return model;
	}

	public void prepare() throws Exception {

		if (getModel() == null) {

			if (getUserId() != -1)
				model = userById(getUserId());
		}

	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}
	
}
