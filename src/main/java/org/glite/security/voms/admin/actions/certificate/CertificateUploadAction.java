package org.glite.security.voms.admin.actions.certificate;

import java.security.Security;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.glite.security.voms.admin.actionforms.CertificateUploadForm;
import org.glite.security.voms.admin.actions.BaseAction;
import org.glite.security.voms.admin.common.NullArgumentException;

public class CertificateUploadAction extends BaseAction {

	private static final Log log = LogFactory.getLog(CertificateUploadAction.class);
	static{
		if (Security.getProvider("BC") == null) {
			Security.addProvider(new BouncyCastleProvider());
		}
	}
	
	
	protected void validateCertificate(X509Certificate cert){
		
		// Do nothing as a first implementation...
		// I should call trustmanager checks here.
		
	}
	@Override
	public ActionForward execute(ActionMapping mapping, 
			ActionForm form, 
			HttpServletRequest request, 
			HttpServletResponse response) throws Exception {
		
		
		CertificateUploadForm cForm = (CertificateUploadForm)form;
		
		if (cForm.getCertificateFile() ==  null)
			throw new NullArgumentException("Certificate to be uploaded is null!");
		
		log.debug("Certificate size:"+cForm.getCertificateFile().getFileSize());
		
		CertificateFactory certFactory = CertificateFactory.getInstance("X.509", 
				Security.getProvider("BC"));
		
		X509Certificate cert = (X509Certificate) certFactory.generateCertificate(cForm.getCertificateFile().getInputStream());
		
		validateCertificate(cert);
		
		log.debug("Uploaded cert for ("+cert.getSubjectDN()+", issued by "+cert.getIssuerDN());
		
		request.setAttribute("certificate", cert);
		
		return findSuccess(mapping);
	}
}
