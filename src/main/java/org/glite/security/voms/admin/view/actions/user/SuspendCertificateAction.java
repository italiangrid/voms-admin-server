package org.glite.security.voms.admin.view.actions.user;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.glite.security.voms.admin.dao.CertificateDAO;
import org.glite.security.voms.admin.model.Certificate;
import org.glite.security.voms.admin.model.VOMSUser.SuspensionReason;
import org.glite.security.voms.admin.operations.users.RestoreUserCertificateOperation;
import org.glite.security.voms.admin.operations.users.SuspendUserCertificateOperation;
import org.glite.security.voms.admin.view.actions.BaseAction;

@ParentPackage("base")
@Results( { @Result(name = BaseAction.SUCCESS, location = "userDetail"),
		@Result(name = BaseAction.INPUT, location = "suspendCertificate") })
public class SuspendCertificateAction extends UserActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	Long certificateId;

	Certificate certificate;

	String suspensionReason;

	@Override
	public String execute() throws Exception {

		SuspensionReason r = SuspensionReason.OTHER;
		r.setMessage(suspensionReason);

		SuspendUserCertificateOperation.instance(getModel(), certificate, r)
				.execute();

		return SUCCESS;
	}

	@Action("restore-certificate")
	public String restore() throws Exception {

		RestoreUserCertificateOperation.instance(getModel(), certificate)
				.execute();
		return SUCCESS;
	}

	@Override
	public void prepare() throws Exception {

		if (certificate == null)
			certificate = CertificateDAO.instance().findById(certificateId);

		super.prepare();

	}

	public Long getCertificateId() {
		return certificateId;
	}

	public void setCertificateId(Long certificateId) {
		this.certificateId = certificateId;
	}

	public String getSuspensionReason() {
		return suspensionReason;
	}

	public void setSuspensionReason(String suspensionReason) {
		this.suspensionReason = suspensionReason;
	}

	public Certificate getCertificate() {
		return certificate;
	}

	public void setCertificate(Certificate certificate) {
		this.certificate = certificate;
	}

}
