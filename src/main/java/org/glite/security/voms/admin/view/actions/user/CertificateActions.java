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
import org.glite.security.voms.admin.model.Certificate;
import org.glite.security.voms.admin.operations.users.AddUserCertificateOperation;
import org.glite.security.voms.admin.operations.users.RemoveUserCertificateOperation;
import org.glite.security.voms.admin.view.actions.BaseAction;

@ParentPackage("base")
@Results( { @Result(name = BaseAction.SUCCESS, location = "userDetail"),
		@Result(name = BaseAction.EDIT, location = "addCertificate"),
		@Result(name = BaseAction.INPUT, location = "addCertificate") })
public class CertificateActions extends UserActionSupport {

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
	public String deleteCertificate() throws Exception {

		
		Certificate cert = CertificateDAO.instance().findById(
				getCertificateId());
		
		// FIXME: create constructor that accepts a certificate
		RemoveUserCertificateOperation.instance(cert.getSubjectString(), cert.getCa().getSubjectString()).execute();

		return SUCCESS;

	}

	@Action("add-certificate")
	public String addCertificate() throws Exception {
		return INPUT;
	}

	@Action("save-certificate")
	public String saveCertificate() throws Exception {

		if (certificateFile != null) {

			X509Certificate cert = CertUtil
					.parseCertficate(new FileInputStream(certificateFile));
			
			AddUserCertificateOperation.instance(getModel(), cert).execute();
			

		} else {
			
			AddUserCertificateOperation.instance(getModel(), subject, caSubject, null).execute();
			

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
