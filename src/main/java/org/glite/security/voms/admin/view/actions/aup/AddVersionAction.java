package org.glite.security.voms.admin.view.actions.aup;

import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.glite.security.voms.admin.model.AUPVersion;
import org.glite.security.voms.admin.operations.aup.AddVersionOperation;
import org.glite.security.voms.admin.view.actions.BaseAction;

import com.opensymphony.xwork2.validator.annotations.RegexFieldValidator;
import com.opensymphony.xwork2.validator.annotations.RequiredStringValidator;
import com.opensymphony.xwork2.validator.annotations.ValidatorType;

@ParentPackage("base")
@Results( {
		@Result(name = BaseAction.INPUT, location = "addAupVersion"),
		@Result(name = BaseAction.SUCCESS, location = "/aup/load.action", type = "redirect") })
public class AddVersionAction extends AUPVersionActions {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String url;

	@Override
	public void validate() {
		for (AUPVersion existingVersion: getModel().getVersions())
			if (existingVersion.getVersion().equals(getVersion()))
				addFieldError("version", "Version '"+getVersion()+"' already exists for this aup!");
		
		AUPVersion candidateVersion = new AUPVersion();
		candidateVersion.setUrl(url);
		if (candidateVersion.getURLContent() == null){
			addFieldError("url", "Error fetching the content of the specified URL! Please provide a valid URL pointing to a text file!");
		}
		
	}
	@Override
	public String execute() throws Exception {

		AddVersionOperation op = new AddVersionOperation(getModel(), getVersion(), url);
		op.execute();
		
		return SUCCESS;

	}

	@RequiredStringValidator(type = ValidatorType.FIELD, message = "The url field is required!")
	@RegexFieldValidator(type = ValidatorType.FIELD, message = "The version field contains illegal characters!", expression = "^[^<>&=;]*$")
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
}
