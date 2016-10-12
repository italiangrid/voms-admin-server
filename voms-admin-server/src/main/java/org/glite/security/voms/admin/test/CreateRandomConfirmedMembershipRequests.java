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
package org.glite.security.voms.admin.test;

import java.util.Calendar;
import java.util.Date;

import org.glite.security.voms.admin.configuration.VOMSConfiguration;
import org.glite.security.voms.admin.configuration.VOMSConfigurationConstants;
import org.glite.security.voms.admin.persistence.HibernateFactory;
import org.glite.security.voms.admin.persistence.dao.generic.DAOFactory;
import org.glite.security.voms.admin.persistence.dao.generic.RequestDAO;
import org.glite.security.voms.admin.persistence.model.request.NewVOMembershipRequest;
import org.glite.security.voms.admin.persistence.model.request.Request.STATUS;
import org.glite.security.voms.admin.persistence.model.request.RequesterInfo;

public class CreateRandomConfirmedMembershipRequests implements Runnable {

  public static final int NUM_REQUEST = 20;
  public static final String EMAIL = "andrea.ceccanti@cnaf.infn.it";

  public void run() {

    long requestLifetime = VOMSConfiguration.instance().getLong(
      "voms.request.vo_membership.lifetime", 300);

    Calendar now = Calendar.getInstance();

    now.add(Calendar.SECOND, (int) requestLifetime);

    Date expirationDate = now.getTime();

    RequestDAO reqDAO = DAOFactory.instance().getRequestDAO();

    for (int i = 0; i < NUM_REQUEST; i++) {

      RequesterInfo ri = new RequesterInfo();
      ri.setName("Test");
      ri.setSurname("User " + i);
      ri.setEmailAddress(EMAIL);
      ri.setInstitution("IGI");
      ri.setAddress("Via Ranzani 13/2 C\n40127 Bologna");
      ri.setPhoneNumber("+39 051 60927777");
      
      ri.setCertificateSubject("/C=IT/O=INFN/CN=test user " + i);
      ri.setCertificateIssuer("/C=IT/O=INFN/CN=INFN CA");

      ri.addInfo(RequesterInfo.MULTIVALUE_COUNT_PREFIX + "requestedGroup", "15");

      for (int gi=0; gi < 15; gi++){
        ri.addInfo("requestedGroup"+gi, "/test/g"+gi);
      }
      

      NewVOMembershipRequest request = reqDAO.createVOMembershipRequest(ri,
        expirationDate);

      request.setStatus(STATUS.CONFIRMED);

    }

  }

  public static void main(String[] args) {

    System.setProperty(VOMSConfigurationConstants.VO_NAME, args[0]);
    VOMSConfiguration.load(null);
    HibernateFactory.beginTransaction();
    new CreateRandomConfirmedMembershipRequests().run();
    HibernateFactory.commitTransaction();

  }
}
