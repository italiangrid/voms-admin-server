/**
 * Copyright (c) Members of the EGEE Collaboration. 2006-2009.
 * See http://www.eu-egee.org/partners/ for details on the copyright holders.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Authors:
 * 	Andrea Ceccanti (INFN)
 */

package org.glite.security.voms.admin.view.actions.user;

import java.io.File;
import java.io.FileInputStream;
import java.security.cert.X509Certificate;

import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.glite.security.voms.admin.configuration.VOMSConfiguration;
import org.glite.security.voms.admin.configuration.VOMSConfigurationConstants;
import org.glite.security.voms.admin.event.EventManager;
import org.glite.security.voms.admin.event.registration.CertificateRequestSubmittedEvent;
import org.glite.security.voms.admin.persistence.dao.CertificateDAO;
import org.glite.security.voms.admin.persistence.dao.generic.DAOFactory;
import org.glite.security.voms.admin.persistence.dao.generic.RequestDAO;
import org.glite.security.voms.admin.persistence.model.request.CertificateRequest;
import org.glite.security.voms.admin.util.CertUtil;
import org.glite.security.voms.admin.util.DNUtil;

import com.opensymphony.xwork2.validator.annotations.RegexFieldValidator;
import com.opensymphony.xwork2.validator.annotations.ValidatorType;

@Results({

@Result(name = UserActionSupport.SUCCESS, location = "userHome"),
  @Result(name = UserActionSupport.ERROR, location = "certificateRequest.jsp"),
  @Result(name = UserActionSupport.INPUT, location = "requestCertificate") })
@InterceptorRef(value = "authenticatedStack", params = {
  "token.includeMethods", "execute" })
public class RequestCertificateAction extends UserActionSupport {

  /**
	 * 
	 */
  private static final long serialVersionUID = 1L;

  File certificateFile;

  String subject;
  String caSubject;

  public void validate() {

    CertificateDAO dao = CertificateDAO.instance();
    RequestDAO reqDAO = DAOFactory.instance().getRequestDAO();
    if (certificateFile != null) {

      X509Certificate cert = null;

      try {
        cert = CertUtil.parseCertficate(new FileInputStream(certificateFile));

      } catch (Throwable e) {

        addFieldError(
          "certificateFile",
          "Error parsing certificate passed as argument. Please upload a valid X509, PEM encoded certificate.");
        return;
      }

      if (cert == null) {
        addFieldError("certificateFile",
          "Error parsing certificate passed as argument!");
        return;
      }

      if (dao.find(cert) != null)
        addFieldError("certificateFile", "Certificate already bound!");

      subject = DNUtil.getOpenSSLSubject(cert.getSubjectX500Principal());
      caSubject = DNUtil.getOpenSSLSubject(cert.getIssuerX500Principal());

      if (reqDAO.userHasPendingCertificateRequest(model, subject, caSubject)) {
        addFieldError("certificateFile",
          "You already have a pending request for this certificate!");

      }

    } else if (subject != null && !"".equals(subject) && !caSubject.equals("-1")) {

      if (dao.findByDNCA(subject, caSubject) != null) {
        addFieldError("subject", "Certificate already bound!");
        addFieldError("caSubject", "Certificate already bound!");
        return;
      }

      if (reqDAO.userHasPendingCertificateRequest(model, subject, caSubject)) {
        addFieldError("subject",
          "You already have a pending request for this certificate!");
        addFieldError("caSubject",
          "You already have a pending request for this certificate!");
      }
    } else {

      addActionError("Please specify a Subject, CA couple or choose a certificate file that will be uploaded to the server!");
    }

  }

  public File getCertificateFile() {

    return certificateFile;
  }

  public void setCertificateFile(File certificateFile) {

    this.certificateFile = certificateFile;
  }

  @RegexFieldValidator(type = ValidatorType.FIELD,
    message = "The subject field name contains illegal characters!",
    expression = "^[^<>&;]*$")
  public String getSubject() {

    return subject;
  }

  public void setSubject(String subject) {

    this.subject = subject;
  }

  @RegexFieldValidator(type = ValidatorType.FIELD,
    message = "The subject field name contains illegal characters!",
    expression = "^[^<>&;]*$")
  public String getCaSubject() {

    return caSubject;
  }

  public void setCaSubject(String caSubject) {

    this.caSubject = caSubject;
  }

  @Override
  public String execute() throws Exception {

    if (!VOMSConfiguration.instance().getBoolean(
      VOMSConfigurationConstants.REGISTRATION_SERVICE_ENABLED, true))
      return "registrationDisabled";

    RequestDAO reqDAO = DAOFactory.instance().getRequestDAO();

    CertificateRequest req = reqDAO.createCertificateRequest(getModel(),
      getSubject(), getCaSubject(), getDefaultFutureDate());
    EventManager.dispatch(new CertificateRequestSubmittedEvent(req,
      getHomeURL()));

    refreshPendingRequests();

    return SUCCESS;
  }

}
