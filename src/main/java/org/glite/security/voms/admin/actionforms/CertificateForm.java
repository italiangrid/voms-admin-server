package org.glite.security.voms.admin.actionforms;

import org.apache.struts.action.ActionForm;
import org.apache.struts.upload.FormFile;

public class CertificateForm extends ActionForm {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Long certificateId;
	Long userId;
	
	String dn;
    String ca;

    String certificateType;
    
    FormFile certificateFile;

	public String getCa() {
		return ca;
	}

	public void setCa(String ca) {
		this.ca = ca;
	}

	public FormFile getCertificateFile() {
		return certificateFile;
	}

	public void setCertificateFile(FormFile certificateFile) {
		this.certificateFile = certificateFile;
	}

	

	public Long getCertificateId() {
		return certificateId;
	}

	public void setCertificateId(Long certificateId) {
		this.certificateId = certificateId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getDn() {
		return dn;
	}

	public void setDn(String dn) {
		this.dn = dn;
	}

	

	public String getCertificateType() {
		return certificateType;
	}

	public void setCertificateType(String certificateType) {
		this.certificateType = certificateType;
	}
    
}
