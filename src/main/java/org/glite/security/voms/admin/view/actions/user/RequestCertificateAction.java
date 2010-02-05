package org.glite.security.voms.admin.view.actions.user;

import java.io.File;
import java.io.FileInputStream;
import java.security.cert.X509Certificate;

import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.glite.security.voms.admin.common.CertUtil;
import org.glite.security.voms.admin.common.DNUtil;
import org.glite.security.voms.admin.common.VOMSConfiguration;
import org.glite.security.voms.admin.dao.CertificateDAO;
import org.glite.security.voms.admin.dao.generic.DAOFactory;
import org.glite.security.voms.admin.dao.generic.RequestDAO;
import org.glite.security.voms.admin.event.EventManager;
import org.glite.security.voms.admin.event.registration.CertificateRequestSubmittedEvent;
import org.glite.security.voms.admin.model.request.CertificateRequest;

@ParentPackage("base")

@Results({
	
	@Result(name=UserActionSupport.SUCCESS,location="userHome"),
	@Result(name=UserActionSupport.ERROR,location="certificateRequest.jsp"),
	@Result(name=UserActionSupport.INPUT,location="requestCertificate")
})
public class RequestCertificateAction extends UserActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	File certificateFile;

	String subject;
	String caSubject;
	
	
		
	public void validate(){
		
		CertificateDAO dao = CertificateDAO.instance();
		
		if (certificateFile != null){
			
			X509Certificate cert = null;
			
			try {
				cert = CertUtil.parseCertficate(new FileInputStream(certificateFile));
				
			} catch (Throwable e) {
				
				addFieldError("certificateFile","Error parsing certificate passed as argument: "+e.getMessage()+". Please upload a valid X509, PEM encoded certificate.");
				return;
			}
			
			if (cert == null){
				addFieldError("certificateFile", "Error parsing certificate passed as argument!");
				return;
			}
			
			if (dao.find(cert) != null)
				addFieldError("certificateFile","Certificate already bound!");
			
			subject = DNUtil.getBCasX500(cert.getSubjectX500Principal());
			caSubject = DNUtil.getBCasX500(cert.getIssuerX500Principal());
			
		}else if (subject!= null && !"".equals(subject)){
			
			if (dao.findByDNCA(subject, caSubject) != null){
				addFieldError("subject", "Certificate already bound!");
				addFieldError("caSubject", "Certificate already bound!");
			}
		}else{
			
			addActionError("Please specify a Subject, CA couple or choose a certificate file that will be uploaded to the server!");
		}
		
		
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
	
	@Override
	public String execute() throws Exception {
		
		if (!VOMSConfiguration.instance().getBoolean(
				VOMSConfiguration.REGISTRATION_SERVICE_ENABLED, true))
			return "registrationDisabled";
		
		RequestDAO reqDAO = DAOFactory.instance().getRequestDAO();
		
		CertificateRequest req = reqDAO.createCertificateRequest(getModel(), getSubject(), getCaSubject(), getDefaultFutureDate());
		EventManager.dispatch(new CertificateRequestSubmittedEvent(req, getHomeURL()));
		
		refreshPendingRequests();
		
		return SUCCESS;
	}
	
}
