package org.glite.security.voms.admin.view.util;

import java.util.Iterator;
import java.util.List;

import org.glite.security.voms.admin.operations.CurrentAdmin;
import org.glite.security.voms.admin.operations.VOMSContext;
import org.glite.security.voms.admin.operations.VOMSPermission;
import org.glite.security.voms.admin.persistence.dao.generic.DAOFactory;
import org.glite.security.voms.admin.persistence.dao.generic.RequestDAO;
import org.glite.security.voms.admin.persistence.model.request.CertificateRequest;
import org.glite.security.voms.admin.persistence.model.request.GroupMembershipRequest;
import org.glite.security.voms.admin.persistence.model.request.GroupScopeRequest;
import org.glite.security.voms.admin.persistence.model.request.MembershipRemovalRequest;
import org.glite.security.voms.admin.persistence.model.request.NewVOMembershipRequest;
import org.glite.security.voms.admin.persistence.model.request.Request;
import org.glite.security.voms.admin.persistence.model.request.RoleMembershipRequest;

public class RequestsUtil {

  private RequestsUtil() {

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
        if (!theAdmin.hasPermissions(context,
          VOMSPermission.getRequestsRWPermissions())) {
          requestIter.remove();
        }
      }
    }

    return pendingRequests;
  }

}
