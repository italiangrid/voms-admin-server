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
package org.glite.security.voms.admin.view.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.glite.security.voms.admin.configuration.VOMSConfiguration;
import org.glite.security.voms.admin.operations.CurrentAdmin;
import org.glite.security.voms.admin.operations.VOMSContext;
import org.glite.security.voms.admin.operations.VOMSPermission;
import org.glite.security.voms.admin.persistence.dao.generic.DAOFactory;
import org.glite.security.voms.admin.persistence.dao.generic.RequestDAO;
import org.glite.security.voms.admin.persistence.model.request.CertificateRequest;
import org.glite.security.voms.admin.persistence.model.request.GroupScopeRequest;
import org.glite.security.voms.admin.persistence.model.request.MembershipRemovalRequest;
import org.glite.security.voms.admin.persistence.model.request.NewVOMembershipRequest;
import org.glite.security.voms.admin.persistence.model.request.Request;

public class RequestUtil {

  private RequestUtil() {

  }

  private static boolean isVOScopeRequest(Request r) {

    return (r instanceof NewVOMembershipRequest)
      || (r instanceof CertificateRequest)
      || (r instanceof MembershipRemovalRequest);
  }

  private static boolean isGroupScopeRequest(Request r) {
    
    return (r instanceof GroupScopeRequest);
    
  }

  public static List<Request> findManageableRequests() {

    RequestDAO rDAO = DAOFactory.instance().getRequestDAO();

    List<Request> pendingRequests = rDAO.findPendingRequests();

    CurrentAdmin theAdmin = CurrentAdmin.instance();

    Iterator<Request> requestIter = pendingRequests.iterator();

    while (requestIter.hasNext()) {

      Request r = requestIter.next();
      
      if (!theAdmin.isVOAdmin() && isVOScopeRequest(r)) {
        requestIter.remove();
      }

      if (isGroupScopeRequest(r)) {
        String groupName = ((GroupScopeRequest) r).getGroupName();
        VOMSContext context = VOMSContext.instance(groupName);
        
        boolean adminHasRequestHandlingPermissions = theAdmin
          .hasPermissions(context,
          VOMSPermission.getRequestsRWPermissions());
        
        boolean adminHasGroupManagerRole = 
          theAdmin.hasRole(context.getGroup(), VOMSConfiguration
            .instance().getGroupManagerRoleName());
        
        /**
         * Group-manager role grants privileges only on non-root
         * groups. 
         */
        if (context.getGroup().isRootGroup()){
          if (!adminHasRequestHandlingPermissions){
              requestIter.remove();
            }
        } else {
          if (!adminHasRequestHandlingPermissions && !adminHasGroupManagerRole){
            requestIter.remove();
          }
          
        }
      }
    }

    return pendingRequests;
  }

  public static List<Request> findUnconfirmedRequests() {

    if (!CurrentAdmin.instance().isVOAdmin()) {
      return Collections.emptyList();
    }

    RequestDAO rDAO = DAOFactory.instance().getRequestDAO();
    List<Request> result = new ArrayList<Request>();

    result.addAll(rDAO.findPendingVOMembershipRequests());

    return result;

  }

}
