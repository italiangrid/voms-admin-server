package org.glite.security.voms.admin.view.actions.aup;

import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.glite.security.voms.admin.dao.generic.AUPVersionDAO;
import org.glite.security.voms.admin.dao.generic.DAOFactory;
import org.glite.security.voms.admin.model.AUPVersion;
import org.glite.security.voms.admin.view.actions.BaseAction;

import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;

@ParentPackage("base")
@Results( { @Result(name = BaseAction.INPUT, location = "aupVersionDetail"),
		@Result(name = BaseAction.SUCCESS, location = "aupVersionDetail") })
public class VersionAction extends BaseAction implements
		ModelDriven<AUPVersion>, Preparable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	Long aupVersionId;
	AUPVersion version;

	public AUPVersion getModel() {

		return version;
	}

	public void prepare() throws Exception {
		if (version == null) {

			AUPVersionDAO dao = DAOFactory.instance().getAUPVersionDAO();
			version = dao.findById(aupVersionId, false);

		}

	}

	public Long getAupVersionId() {
		return aupVersionId;
	}

	public void setAupVersionId(Long aupVersionId) {
		this.aupVersionId = aupVersionId;
	}

}
