package org.glite.security.voms.admin.actionforms;

import org.apache.struts.action.ActionForm;
import org.apache.struts.upload.FormFile;

public class CertificateUploadForm extends ActionForm {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private FormFile certificateFile;

	public FormFile getCertificateFile() {
		return certificateFile;
	}

	public void setCertificateFile(FormFile certificateFile) {
		this.certificateFile = certificateFile;
	}
	
	

}
