/**
 * Copyright (c) Istituto Nazionale di Fisica Nucleare (INFN). 2006-2015
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
package org.glite.security.voms.admin.test;

import java.util.Calendar;
import java.util.Date;

import org.apache.struts2.views.util.UrlHelper;
import org.glite.security.voms.admin.configuration.VOMSConfiguration;
import org.glite.security.voms.admin.configuration.VOMSConfigurationConstants;
import org.glite.security.voms.admin.persistence.DBUtil;
import org.glite.security.voms.admin.persistence.HibernateFactory;
import org.glite.security.voms.admin.persistence.dao.generic.DAOFactory;
import org.glite.security.voms.admin.persistence.dao.generic.RequestDAO;
import org.glite.security.voms.admin.persistence.model.request.NewVOMembershipRequest;
import org.glite.security.voms.admin.persistence.model.request.RequesterInfo;
import org.glite.security.voms.admin.persistence.model.request.Request.STATUS;
import org.glite.security.voms.admin.util.URLBuilder;

public class SubmitMembershipRequest implements Runnable {

  @Override
  public void run() {

    HibernateFactory.beginTransaction();

    RequesterInfo ri = new RequesterInfo();
    ri.setName("Ille ");
    ri.setSurname("Camughe ");
    ri.setEmailAddress("andrea.ceccanti@cnaf.infn.it");
    ri.setInstitution("INFN");
    ri.setCertificateSubject("/C=IT/O=INFN/OU=Personal Certificate/L=CNAF/CN=Andrea Ceccanti");
    ri.setCertificateIssuer("/C=IT/O=INFN/CN=INFN CA");

    long requestLifetime = VOMSConfiguration.instance().getLong(
      "voms.request.vo_membership.lifetime", 300);

    Calendar now = Calendar.getInstance();

    now.add(Calendar.SECOND, (int) requestLifetime);

    Date expirationDate = now.getTime();
    RequestDAO reqDAO = DAOFactory.instance().getRequestDAO();
    NewVOMembershipRequest request = reqDAO.createVOMembershipRequest(ri,
      expirationDate);

    String baseURL = "https://wilco.cnaf.infn.it:8443/voms/mysql";

    System.out.println(URLBuilder.buildRequestConfirmURL(baseURL, request));

    HibernateFactory.commitTransaction();

  }

  public static void main(String[] args) {

    System.setProperty(VOMSConfigurationConstants.VO_NAME, args[0]);
    VOMSConfiguration.load(null);
    new SubmitMembershipRequest().run();
  }
}
