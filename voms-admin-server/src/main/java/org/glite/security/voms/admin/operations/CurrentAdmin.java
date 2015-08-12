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
package org.glite.security.voms.admin.operations;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.glite.security.voms.admin.configuration.VOMSConfiguration;
import org.glite.security.voms.admin.configuration.VOMSConfigurationConstants;
import org.glite.security.voms.admin.persistence.dao.VOMSAdminDAO;
import org.glite.security.voms.admin.persistence.dao.VOMSUserDAO;
import org.glite.security.voms.admin.persistence.model.ACL;
import org.glite.security.voms.admin.persistence.model.VOMSAdmin;
import org.glite.security.voms.admin.persistence.model.VOMSCA;
import org.glite.security.voms.admin.persistence.model.VOMSUser;
import org.glite.security.voms.admin.util.DNUtil;
import org.italiangrid.utils.voms.CurrentSecurityContext;
import org.italiangrid.utils.voms.VOMSSecurityContext;
import org.italiangrid.voms.VOMSAttribute;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CurrentAdmin {

  private static final Logger log = LoggerFactory.getLogger(CurrentAdmin.class);

  private VOMSAdmin admin;

  public VOMSAdmin getAdmin() {

    return admin;
  }

  protected CurrentAdmin(VOMSAdmin a) {

    this.admin = a;
  }

  private static VOMSAdmin lookupAdmin() {

    VOMSSecurityContext ctxt = (VOMSSecurityContext) CurrentSecurityContext
      .get();

    boolean skipCACheck = VOMSConfiguration.instance().getBoolean(
      VOMSConfigurationConstants.SKIP_CA_CHECK, false);

    VOMSAdmin admin = null;

    if (skipCACheck) {
      admin = VOMSAdminDAO.instance().getBySubject(ctxt.getClientName());
    } else {
      admin = VOMSAdminDAO.instance().getByName(ctxt.getClientName(),
        ctxt.getIssuerName());
    }

    return admin;
  }

  public static CurrentAdmin instance() {

    VOMSAdmin admin = lookupAdmin();

    if (admin == null)
      admin = VOMSAdminDAO.instance().getAnyAuthenticatedUserAdmin();

    return new CurrentAdmin(admin);
  }

  public VOMSCA getCa() {

    return admin.getCa();
  }

  public String getDn() {

    return admin.getDn();
  }

  public boolean isAuthorizedAdmin() {

    return !getAdmin().equals(
      VOMSAdminDAO.instance().getAnyAuthenticatedUserAdmin());
  }

  public boolean isVoUser() {

    return (getVoUser() != null);

  }

  public boolean is(VOMSUser u) {

    if (getVoUser() == null)
      return false;

    return getVoUser().equals(u);

  }

  public VOMSUser getVoUser() {

    if (!isAuthorizedAdmin()) {

      return VOMSUserDAO.instance().getByDNandCA(getRealSubject(),
        getRealIssuer());
    }

    return VOMSUserDAO.instance().getByDNandCA(admin.getDn(), admin.getCa());
  }

  public void createVoUser() {

    VOMSUser usr = getVoUser();

    if (usr == null) {

      VOMSUserDAO.instance().create(getRealSubject(), getRealIssuer(),
        getRealCN(), null, getRealEmailAddress());
    }
  }

  public boolean isVOAdmin() {

    if (hasPermissions(VOMSContext.getVoContext(),
      VOMSPermission.getAllPermissions()))
      return true;

    if (hasPermissions(VOMSContext.getVoContext(),
      VOMSPermission.getRequestsRWPermissions()))
      return true;

    return false;
  }

  public boolean canBrowseVO() {

    return hasPermissions(VOMSContext.getVoContext(), VOMSPermission
      .getContainerReadPermission().setMembershipReadPermission());
  }

  public boolean hasPermissions(VOMSContext c, VOMSPermission p) {

    ACL acl = c.getACL();

    log.debug("Checking if admin " + getAdmin() + " has permission " + p
      + " in context " + c);

    log.debug("ACL for this context: ");
    log.debug(acl.toString());

    if (isUnauthenticated()) {

      VOMSPermission unauthPerms = acl.getUnauthenticatedClientPermissions();

      if (unauthPerms == null)
        return false;

      return unauthPerms.satisfies(p);

    }

    VOMSUser adminUser = getVoUser();

    log.debug("Admin user: " + adminUser);

    VOMSPermission personalPermissions = acl.getPermissions(admin);

    log.debug("Personal permissions for admin: " + personalPermissions);

    VOMSPermission anyAuthenticatedUserPermissions = acl
      .getAnyAuthenticatedUserPermissions();

    log.debug("Permissions for any authenticated user: "
      + anyAuthenticatedUserPermissions);

    VOMSPermissionList adminPerms = VOMSPermissionList.instance();

    VOMSPermission unauthenticatedClientPermissions = acl
      .getUnauthenticatedClientPermissions();
    log.debug("Permissions for unauthenticated clients: "
      + unauthenticatedClientPermissions);

    if (personalPermissions == null && adminUser == null
      && anyAuthenticatedUserPermissions == null
      && unauthenticatedClientPermissions == null)
      return false;

    if (personalPermissions != null)
      adminPerms.addPermission(personalPermissions);

    if (anyAuthenticatedUserPermissions != null)
      adminPerms.addPermission(anyAuthenticatedUserPermissions);

    if (unauthenticatedClientPermissions != null)
      adminPerms.addPermission(unauthenticatedClientPermissions);

    if (adminUser == null)
      return adminPerms.satifies(p);

    // AdminUser != null
    Map<VOMSAdmin, VOMSPermission> groupPermissions = acl.getGroupPermissions();
    Map<VOMSAdmin, VOMSPermission> rolePermissions = acl.getRolePermissions();

    log.debug("Group permissions empty? " + groupPermissions.isEmpty());
    log.debug("Role permissions empty? " + rolePermissions.isEmpty());

    if (!groupPermissions.isEmpty()) {

      for (Map.Entry<VOMSAdmin, VOMSPermission> entry : groupPermissions
        .entrySet()) {

        String groupName = entry.getKey().getDn();

        if (adminUser.isMember(groupName)) {
          adminPerms.addPermission((VOMSPermission) entry.getValue());
          log.debug("Adding group permission " + entry.getValue()
            + " to admin's permission set. admin is a member of the group '"
            + groupName + "'.");
        }

      }
    }

    if (!rolePermissions.isEmpty()) {

      for (Map.Entry<VOMSAdmin, VOMSPermission> entry : rolePermissions
        .entrySet()) {
        String roleName = entry.getKey().getDn();

        log.debug("Checking if current admin has role: " + roleName);
        if (adminUser.hasRole(roleName)) {

          adminPerms.addPermission(entry.getValue());
          log.debug("Adding role permission " + entry.getValue()
            + " to admin's permission set. admin has role '" + roleName + "'.");
        }
      }
    }

    log.debug("Admin permissions: " + adminPerms);

    return adminPerms.satifies(p);
  }

  public String getRealSubject() {

    VOMSSecurityContext theContext = (VOMSSecurityContext) CurrentSecurityContext
      .get();

    return theContext.getClientName();

  }

  public String getRealIssuer() {

    VOMSSecurityContext theContext = (VOMSSecurityContext) CurrentSecurityContext
      .get();

    return theContext.getIssuerName();

  }

  public String getRealCN() {

    VOMSSecurityContext theContext = (VOMSSecurityContext) CurrentSecurityContext
      .get();
    if (theContext.getClientCert() == null)
      return null;

    String name = DNUtil.getOpenSSLSubject(theContext.getClientCert()
      .getSubjectX500Principal());

    Matcher m = Pattern.compile("/CN=([^/]*)").matcher(name);
    if (m.find())
      return m.group(1); // get the CN field
    else
      return null;
  }

  public String getRealEmailAddress() {

    VOMSSecurityContext theContext = (VOMSSecurityContext) CurrentSecurityContext
      .get();

    if (theContext.getClientCert() == null)
      return null;

    String name = DNUtil.getOpenSSLSubject(theContext.getClientCert()
      .getSubjectX500Principal());

    String candidateEmail = DNUtil.getEmailAddressFromDN(DNUtil
      .normalizeEmailAddressInDN(name));

    if (candidateEmail == null)
      candidateEmail = DNUtil.getEmailAddressFromExtensions(theContext
        .getClientCert());

    return candidateEmail;

  }

  public List<VOMSAttribute> getVOMSAttributes() {

    VOMSSecurityContext theContext = (VOMSSecurityContext) CurrentSecurityContext
      .get();
    if (theContext.getClientCert() == null)
      return null;

    return theContext.getVOMSAttributes();
  }

  public String toString() {

    return admin.toString();
  }

  public boolean isUnauthenticated() {

    return admin.isUnauthenticated();

  }

  public String getName() {

    return String.format("(%s,%s)", getRealSubject(), getRealIssuer());
  }
}
