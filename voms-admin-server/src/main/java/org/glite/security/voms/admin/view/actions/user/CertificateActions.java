/**
 * Copyright (c) Istituto Nazionale di Fisica Nucleare (INFN). 2006-2016
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
 */
package org.glite.security.voms.admin.view.actions.user;

import java.io.File;
import java.io.FileInputStream;
import java.security.cert.X509Certificate;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.glite.security.voms.admin.operations.users.AddUserCertificateOperation;
import org.glite.security.voms.admin.operations.users.RemoveUserCertificateOperation;
import org.glite.security.voms.admin.persistence.dao.CertificateDAO;
import org.glite.security.voms.admin.persistence.model.Certificate;
import org.glite.security.voms.admin.util.CertUtil;
import org.glite.security.voms.admin.view.actions.BaseAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Results({ @Result(name = BaseAction.SUCCESS, location = "userDetail"),
  @Result(name = BaseAction.INPUT, location = "addCertificate") })
@InterceptorRef(value = "authenticatedStack",
  params = { "token.includeMethods", "deleteCertificate,saveCertificate" })
public class CertificateActions extends UserActionSupport {

  public static final Logger log = LoggerFactory
    .getLogger(CertificateActions.class);

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

    Certificate cert = CertificateDAO.instance()
      .findById(getCertificateId());

    RemoveUserCertificateOperation
      .instance(cert)
      .execute();

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

      AddUserCertificateOperation.instance(getModel(), cert)
        .execute();

    } else {

      // Fix for bug https://savannah.cern.ch/bugs/?88019
      AddUserCertificateOperation
        .instance(getModel(), subject.trim(), caSubject.trim(), null)
        .execute();

    }

    return SUCCESS;
  }

  public void validateSaveCertificate() {

    CertificateDAO dao = CertificateDAO.instance();

    if (certificateFile != null) {

      X509Certificate cert = null;

      try {
        cert = CertUtil.parseCertficate(new FileInputStream(certificateFile));
      } catch (Throwable e) {

        addFieldError("certificateFile",
          "Error parsing certificate passed as argument: " + e.getMessage()
            + ". Please upload a valid X509, PEM encoded certificate.");
        return;
      }

      if (cert == null) {
        addFieldError("certificateFile",
          "Error parsing certificate passed as argument!");
        return;
      }

      if (dao.find(cert) != null)
        addFieldError("certificateFile", "Certificate already bound!");

    } else if (subject != null && !"".equals(subject)) {

      if (dao.lookup(subject, caSubject) != null) {
        addFieldError("subject", "Certificate already bound!");
        addFieldError("caSubject", "Certificate already bound!");
      }
    } else {

      addActionError(
        "Please specify a Subject, CA couple or choose a certificate file that will be uploaded to the server!");
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

  public Long getCertificateId() {

    return certificateId;
  }

  public void setCertificateId(Long certificateId) {

    this.certificateId = certificateId;
  }
}
