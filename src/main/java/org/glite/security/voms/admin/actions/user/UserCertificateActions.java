package org.glite.security.voms.admin.actions.user;

import java.security.cert.X509Certificate;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.glite.security.voms.admin.actionforms.CertificateForm;
import org.glite.security.voms.admin.actions.BaseDispatchAction;
import org.glite.security.voms.admin.common.NullArgumentException;
import org.glite.security.voms.admin.dao.CertificateDAO;
import org.glite.security.voms.admin.dao.VOMSCADAO;
import org.glite.security.voms.admin.dao.VOMSUserDAO;
import org.glite.security.voms.admin.database.NoSuchCertificateException;
import org.glite.security.voms.admin.database.NoSuchUserException;
import org.glite.security.voms.admin.model.Certificate;
import org.glite.security.voms.admin.model.VOMSUser;

public class UserCertificateActions extends BaseDispatchAction {

	private static final Log log = LogFactory
	.getLog(UserCertificateActions.class);
	
	
	public ActionForward preAdd(ActionMapping mapping, 
			ActionForm form, 
			HttpServletRequest request, 
			HttpServletResponse response) throws Exception {
		
		log.debug(ToStringBuilder.reflectionToString(form));
		CertificateForm cForm = (CertificateForm)form;
		
		VOMSUser user = VOMSUserDAO.instance().findById(cForm.getUserId());
		
		if (user == null)
			throw new NoSuchUserException("No user with id '"+cForm.getUserId()+"' found!");
		
		List caList = VOMSCADAO.instance().getValid();
		
		storeUser(request, user);
		request.setAttribute("caList", caList);
		return mapping.findForward("add-certificate");
		
	}
	public ActionForward add(ActionMapping mapping, 
			ActionForm form, 
			HttpServletRequest request, 
			HttpServletResponse response) throws Exception {
		
		log.debug(ToStringBuilder.reflectionToString(form));
		
		CertificateForm cForm = (CertificateForm)form;
		
		String certType = cForm.getCertificateType();
		Long userId = cForm.getUserId();
		
		VOMSUser u = VOMSUserDAO.instance().findById(userId);
		storeUser(request, u);
		
		if ("dnCa".equals(certType)){
			VOMSUserDAO.instance().addCertificate(u, cForm.getDn(), cForm.getCa());
			
		}else if ("cert".equals(certType)){
			
			if (cForm.getCertificateFile() == null)
				throw new NullArgumentException("Certificate to be added is null!");
			
			X509Certificate cert = CertUtil.parseCertficate(cForm.getCertificateFile().getInputStream());
			VOMSUserDAO.instance().addCertificate(u, cert);
		}
		
		
		
		return findSuccess(mapping);
	}
	
	public ActionForward update(ActionMapping mapping, 
			ActionForm form, 
			HttpServletRequest request, 
			HttpServletResponse response) throws Exception {
		
				
		return findSuccess(mapping);
	}
	
	public ActionForward load(ActionMapping mapping, 
			ActionForm form, 
			HttpServletRequest request, 
			HttpServletResponse response) throws Exception {
		
		log.debug(ToStringBuilder.reflectionToString(form));
		
		CertificateForm cForm = (CertificateForm)form;
		
		Certificate cert = CertificateDAO.instance().findById(cForm.getCertificateId());
		
		if (cert == null)
			throw new NoSuchCertificateException("Certificate with id '"+cForm.getCertificateId()+"' not found!");
		
		request.setAttribute("certificate", cert);
		
		return mapping.findForward("manage-certificate");
	}
	
	
	public ActionForward delete(ActionMapping mapping, 
			ActionForm form, 
			HttpServletRequest request, 
			HttpServletResponse response) throws Exception {
		
		log.debug(ToStringBuilder.reflectionToString(form));
		
		CertificateForm cForm = (CertificateForm)form;
		Certificate cert = CertificateDAO.instance().findById(cForm.getCertificateId());
		
        if (cert == null)
            throw new NoSuchCertificateException("Certificate with id '"+cForm.getCertificateId()+"' not found!");
        
		storeUser(request, cert.getUser());
		
		VOMSUserDAO.instance().deleteCertificate(cert.getUser(), cert);
		
		return findSuccess(mapping);
	}
	
	
}
