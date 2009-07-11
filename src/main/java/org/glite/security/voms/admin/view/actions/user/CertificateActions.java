package org.glite.security.voms.admin.view.actions.user;

import java.io.File;
import java.io.FileInputStream;
import java.security.cert.X509Certificate;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.glite.security.voms.admin.common.CertUtil;
import org.glite.security.voms.admin.dao.CertificateDAO;
import org.glite.security.voms.admin.dao.VOMSUserDAO;
import org.glite.security.voms.admin.model.Certificate;
import org.glite.security.voms.admin.view.actions.BaseAction;


@ParentPackage("base")
@Results({
	@Result(name=BaseAction.SUCCESS,location="userDetail"),
	@Result(name=BaseAction.EDIT, location="addCertificate"),
	@Result(name=BaseAction.INPUT, location="addCertificate")
})
public class CertificateActions extends UserActionSupport{

	
	public static final Log log = LogFactory.getLog(CertificateActions.class);
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
		
	Long certificateId;
	
	File certificateFile;
	
	String subject;
	String caSubject;
	
	@Action("delete-certificate")
	public String deleteCertificate() throws Exception{
		
		
		// FIXME: do this with an operation!
		Certificate cert = CertificateDAO.instance().findById(getCertificateId());
		VOMSUserDAO.instance().deleteCertificate(getModel(), cert);
		
		return SUCCESS;
		
	}
	
	@Action("add-certificate")
	public String addCertificate() throws Exception{
		return INPUT;
	}
	
	@Action("save-certificate")
	public String saveCertificate() throws Exception{
		
		// FIXME: do this with an operation!
		if (certificateFile != null){
			
			X509Certificate cert = CertUtil.parseCertficate(new FileInputStream(certificateFile));
			VOMSUserDAO.instance().addCertificate(getModel(), cert);
			
		}else{
			
			VOMSUserDAO.instance().addCertificate(getModel(), subject,caSubject);
			
		}
		
		
		return SUCCESS;
	}

	public File getCertificateFile() {
		return certificateFile;
	}

	public void setCertificateFile(File certificateFile) {
		this.certificateFile = certificateFile;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getCaSubject() {
		return caSubject;
	}

	public void setCaSubject(String caSubject) {
		this.caSubject = caSubject;
	}

	public Long getCertificateId() {
		return certificateId;
	}

	public void setCertificateId(Long certificateId) {
		this.certificateId = certificateId;
	}
	
	
}
