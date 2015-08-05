package org.glite.security.voms.admin.operations.requests;

import org.glite.security.voms.admin.configuration.VOMSConfiguration;
import org.glite.security.voms.admin.error.IllegalStateException;
import org.glite.security.voms.admin.operations.AuthorizationResponse;
import org.glite.security.voms.admin.operations.CurrentAdmin;
import org.glite.security.voms.admin.persistence.model.VOMSGroup;
import org.glite.security.voms.admin.persistence.model.VOMSRole;
import org.glite.security.voms.admin.persistence.model.VOMSUser;
import org.glite.security.voms.admin.persistence.model.request.GroupScopeRequest;
import org.glite.security.voms.admin.persistence.model.request.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class GroupManagerRoleHolderOperation<T extends Request> extends
  BaseHandleRequestOperation<T> {

  public static final Logger LOGGER = LoggerFactory
    .getLogger(GroupManagerRoleHolderOperation.class);

  public GroupManagerRoleHolderOperation(T request, DECISION decision) {

    super(request, decision);

  }

  @Override
  final protected AuthorizationResponse isAllowed() {

    CurrentAdmin admin = CurrentAdmin.instance();

    if (!admin.isVoUser()) {
      LOGGER.debug("Current admin has no corresponding VO user.");
      return super.isAllowed();
    }

    final VOMSUser user = admin.getVoUser();
    
    String groupManagerRoleName = VOMSConfiguration
      .instance().getGroupManagerRoleName();

    VOMSRole gmRole = findRoleByName(groupManagerRoleName);

    if (gmRole == null) {
      LOGGER.debug("{} role is not defined, falling back to ACL authz.",
        groupManagerRoleName);

      return super.isAllowed();
    }

    if (!(request instanceof GroupScopeRequest)) {
      throw new IllegalStateException(
        "This is a bug: this class should only be instantiated "
          + "to handle group scoped requests.");
    }

    GroupScopeRequest gr = (GroupScopeRequest) request;
    VOMSGroup requestGroup = findGroupByName(gr.getGroupName());
    
    if (requestGroup.isRootGroup()){
      return super.isAllowed();
    }
    
    if (!user.isMember(requestGroup)){
      return super.isAllowed();
    }
    
    if (!user.hasRole(requestGroup, gmRole)) {
      return super.isAllowed();
    }

    return AuthorizationResponse.permit();
  }

}
