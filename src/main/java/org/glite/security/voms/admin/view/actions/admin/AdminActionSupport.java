package org.glite.security.voms.admin.view.actions.admin;

import org.apache.struts2.convention.annotation.ParentPackage;
import org.glite.security.voms.admin.model.VOMSAdmin;
import org.glite.security.voms.admin.operations.CurrentAdmin;
import org.glite.security.voms.admin.view.actions.BaseAction;

import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;

@ParentPackage("base")
public class AdminActionSupport extends BaseAction implements
		ModelDriven<VOMSAdmin>, Preparable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	VOMSAdmin admin;

	public VOMSAdmin getModel() {

		return admin;
	}

	public void prepare() throws Exception {

		admin = CurrentAdmin.instance().getAdmin();

	}

}
