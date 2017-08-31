/**
 * Copyright (c) Istituto Nazionale di Fisica Nucleare (INFN). 2006-2016
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package org.glite.security.voms.admin.operations;

import java.security.cert.X509Certificate;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.glite.security.voms.admin.configuration.VOMSConfiguration;
import org.glite.security.voms.admin.operations.util.CurrentAdminPermissionCache;
import org.glite.security.voms.admin.persistence.dao.VOMSAdminDAO;
import org.glite.security.voms.admin.persistence.dao.VOMSRoleDAO;
import org.glite.security.voms.admin.persistence.dao.VOMSUserDAO;
import org.glite.security.voms.admin.persistence.model.ACL;
import org.glite.security.voms.admin.persistence.model.Certificate;
import org.glite.security.voms.admin.persistence.model.VOMSAdmin;
import org.glite.security.voms.admin.persistence.model.VOMSCA;
import org.glite.security.voms.admin.persistence.model.VOMSGroup;
import org.glite.security.voms.admin.persistence.model.VOMSRole;
import org.glite.security.voms.admin.persistence.model.VOMSUser;
import org.glite.security.voms.admin.util.DNUtil;
import org.italiangrid.utils.voms.CurrentSecurityContext;
import org.italiangrid.utils.voms.SecurityContext;
import org.italiangrid.voms.VOMSAttribute;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CurrentAdmin {

  private static final Logger log = LoggerFactory.getLogger(CurrentAdmin.class);

  private static ThreadLocal<CurrentAdmin> currentAdmin = new ThreadLocal<>();

  private final VOMSAdmin admin;
  private final boolean permissionCacheDisabled;

  public VOMSAdmin getAdmin() {

    return admin;
  }

  protected CurrentAdmin(VOMSAdmin a, boolean permissionCacheDisabled) {
    this.admin = a;
    this.permissionCacheDisabled = permissionCacheDisabled;
  }

  private static VOMSAdmin lookupAdmin() {

    SecurityContext ctxt = (SecurityContext) CurrentSecurityContext.get();

    VOMSAdmin admin = VOMSAdminDAO.instance().lookup(ctxt.getClientName(), ctxt.getIssuerName());

    return admin;
  }

  public static CurrentAdmin instance() {
    if (currentAdmin.get() != null) {
      return currentAdmin.get();
    }

    VOMSAdmin admin = lookupAdmin();

    if (admin == null) {
      admin = VOMSAdminDAO.instance().getAnyAuthenticatedUserAdmin();
    }
    
    VOMSConfiguration configuration = VOMSConfiguration.instance();
    currentAdmin.set(new CurrentAdmin(admin, configuration.permissionCacheDisabled()));
    return currentAdmin.get();

  }

  public static void clear() {

    currentAdmin.set(null);
  }

  public VOMSCA getCa() {

    return admin.getCa();
  }

  public String getDn() {

    return admin.getDn();
  }

  public boolean isAuthorizedAdmin() {

    return !getAdmin().equals(VOMSAdminDAO.instance().getAnyAuthenticatedUserAdmin());
  }

  public boolean isVoUser() {

    return (getVoUser() != null);

  }

  public boolean is(VOMSUser u) {

    if (getVoUser() == null)
      return false;

    return getVoUser().equals(u);

  }

  public boolean is(Certificate c) {

    return getRealSubject().equals(c.getSubjectString())
        && getRealIssuer().equals(c.getCa().getSubjectString());
  }

  public boolean hasLinkedUser() {

    return (getVoUser() != null);
  }

  public VOMSUser getVoUser() {

    String lookupSubject, lookupIssuer;

    if (!isAuthorizedAdmin()) {
      lookupSubject = getRealSubject();
      lookupIssuer = getRealIssuer();
    } else {
      lookupSubject = admin.getDn();
      lookupIssuer = admin.getCa().getSubjectString();
    }

    VOMSUser user = VOMSUserDAO.instance().lookup(lookupSubject, lookupIssuer);

    return user;
  }

  public void createVoUser() {

    VOMSUser usr = getVoUser();

    if (usr == null) {

      VOMSUserDAO.instance().create(getRealSubject(), getRealIssuer(), getRealCN(), null,
          getRealEmailAddress());
    }
  }

  public boolean isVOAdmin() {

    if (hasPermissions(VOMSContext.getVoContext(), VOMSPermission.getAllPermissions()))
      return true;

    if (hasPermissions(VOMSContext.getVoContext(), VOMSPermission.getRequestsRWPermissions()))
      return true;

    return false;
  }

  public boolean canBrowseVO() {

    return hasPermissions(VOMSContext.getVoContext(),
        VOMSPermission.getContainerReadPermission().setMembershipReadPermission());
  }

  public boolean checkPermission(VOMSContext c, VOMSPermission p) {

    ACL acl = c.getACL();

    if (log.isDebugEnabled()) {
      log.debug("Checking if admin {} has permission {} in context {}",
          new Object[] {getAdmin(), p, c});
      log.debug("ACL for this context: ");
      log.debug(acl.toString());
    }

    if (isUnauthenticated()) {

      VOMSPermission unauthPerms = acl.getUnauthenticatedClientPermissions();

      if (unauthPerms == null)
        return false;

      return unauthPerms.satisfies(p);

    }

    VOMSUser adminVOUser = getVoUser();
    if (log.isDebugEnabled()) {
      log.debug("Admin VO user: {}", adminVOUser);
    }

    VOMSPermission personalPermissions = acl.getPermissions(admin);

    if (log.isDebugEnabled()) {
      log.debug("Personal permissions for admin: {}", personalPermissions);
    }

    VOMSPermission anyAuthenticatedUserPermissions = acl.getAnyAuthenticatedUserPermissions();

    if (log.isDebugEnabled()) {
      log.debug("Permissions for any authenticated user: {}", anyAuthenticatedUserPermissions);
    }

    int effectivePerms = 0;

    VOMSPermission unauthenticatedClientPermissions = acl.getUnauthenticatedClientPermissions();

    if (log.isDebugEnabled()) {
      log.debug("Permissions for unauthenticated clients: {}", unauthenticatedClientPermissions);
    }

    if (personalPermissions == null && adminVOUser == null
        && anyAuthenticatedUserPermissions == null && unauthenticatedClientPermissions == null)
      return false;

    if (personalPermissions != null) {
      effectivePerms = effectivePerms | personalPermissions.getBits();
    }

    if (anyAuthenticatedUserPermissions != null) {
      effectivePerms = effectivePerms | anyAuthenticatedUserPermissions.getBits();
    }

    if (unauthenticatedClientPermissions != null) {
      effectivePerms = effectivePerms | unauthenticatedClientPermissions.getBits();
    }

    if (adminVOUser == null) {
      VOMSPermission adminEffectivePerms = VOMSPermission.fromBits(effectivePerms);
      log.debug("Admin effective permissions {}", adminEffectivePerms);

      boolean result = adminEffectivePerms.satisfies(p);
      if (log.isDebugEnabled()) {
        log.debug("Does {} have permissions that satisfy {} in context {} ? {}", new String[] {
            getAdmin().toString(), p.toString(), c.toString(), Boolean.toString(result)});
      }
      return result;
    }

    // AdminUser != null
    Map<VOMSAdmin, VOMSPermission> groupPermissions = acl.getGroupPermissions();
    Map<VOMSAdmin, VOMSPermission> rolePermissions = acl.getRolePermissions();

    if (log.isDebugEnabled()) {
      log.debug("Group permissions empty? {}", groupPermissions.isEmpty());
      log.debug("Role permissions empty? {}", rolePermissions.isEmpty());
    }

    if (!groupPermissions.isEmpty()) {

      for (Map.Entry<VOMSAdmin, VOMSPermission> entry : groupPermissions.entrySet()) {

        String groupName = entry.getKey().getDn();

        if (adminVOUser.isMember(groupName)) {
          VOMSPermission groupPerm = entry.getValue();

          effectivePerms = effectivePerms | groupPerm.getBits();

          log.debug(
              "Adding group permission {} to admin's permission set. "
                  + "{} is a member of group {}",
              new Object[] {groupPerm, adminVOUser.getFullName(), groupName});
        }

      }
    }

    if (!rolePermissions.isEmpty()) {

      for (Map.Entry<VOMSAdmin, VOMSPermission> entry : rolePermissions.entrySet()) {
        String roleName = entry.getKey().getDn();

        log.debug("Checking if current admin has role: {}", roleName);

        if (adminVOUser.hasRole(roleName)) {

          effectivePerms = effectivePerms | entry.getValue().getBits();

          log.debug("Adding role permission {} to admin's permission set. " + "{} has role {}",
              new Object[] {entry.getValue(), adminVOUser.getFullName(), roleName});
        }
      }
    }

    VOMSPermission adminEffectivePerms = VOMSPermission.fromBits(effectivePerms);

    log.debug("Admin effective permissions: {}", adminEffectivePerms);

    boolean result = adminEffectivePerms.satisfies(p);

    if (log.isDebugEnabled()) {
      log.debug("Does {} have permissions that satisfy {} in context {} ? {}", new String[] {
          getAdmin().toString(), p.toString(), c.toString(), Boolean.toString(result)});
    }
    return result;
  }

  public boolean hasPermissions(VOMSContext c, VOMSPermission p) {

    boolean result = false;

    if (permissionCacheDisabled){
      return checkPermission(c, p);
    }
    
    try {
      result = CurrentAdminPermissionCache.INSTANCE.hasPermission(this, c, p);
    } catch (ExecutionException e) {
      log.error("Error loading permission check result from cache: " + e.getMessage(), e);
      result = checkPermission(c, p);
    }

    return result;
  }

  public String getRealSubject() {

    SecurityContext theContext = (SecurityContext) CurrentSecurityContext.get();

    return theContext.getClientName();

  }

  public String getRealIssuer() {

    SecurityContext theContext = (SecurityContext) CurrentSecurityContext.get();

    return theContext.getIssuerName();

  }

  public String getRealCN() {

    SecurityContext theContext = (SecurityContext) CurrentSecurityContext.get();

    if (theContext.getClientCert() == null)
      return null;

    String name = DNUtil.getOpenSSLSubject(theContext.getClientCert().getSubjectX500Principal());

    Matcher m = Pattern.compile("/CN=([^/]*)").matcher(name);
    if (m.find())
      return m.group(1); // get the CN field
    else
      return null;
  }

  public boolean hasRole(VOMSGroup group, String roleName) {

    VOMSRole role = VOMSRoleDAO.instance().findByName(roleName);

    if (role == null) {
      return false;
    }

    return hasRole(group, role);
  }

  public boolean hasRole(VOMSGroup group, VOMSRole role) {

    if (getVoUser() == null) {
      return false;
    }

    if (getVoUser().isMember(group)) {
      return getVoUser().hasRole(group, role);
    }

    return false;

  }

  public String getRealEmailAddress() {

    SecurityContext theContext = (SecurityContext) CurrentSecurityContext.get();

    if (theContext.getClientCert() == null)
      return null;

    String name = DNUtil.getOpenSSLSubject(theContext.getClientCert().getSubjectX500Principal());

    String candidateEmail = DNUtil.getEmailAddressFromDN(DNUtil.normalizeEmailAddressInDN(name));

    if (candidateEmail == null)
      candidateEmail = DNUtil.getEmailAddressFromExtensions(theContext.getClientCert());

    return candidateEmail;

  }

  public List<VOMSAttribute> getVOMSAttributes() {

    // FIXME: to be reimplemented
    return null;

  }

  public String toString() {

    return admin.toString();
  }

  public boolean isUnauthenticated() {

    return admin.isUnauthenticated();

  }

  public String getName() {

    return getRealSubject();
  }

  public X509Certificate getClientCert() {

    if (isUnauthenticated()) {
      return null;
    }

    SecurityContext theContext = (SecurityContext) CurrentSecurityContext.get();

    return theContext.getClientCert();

  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((admin == null) ? 0 : admin.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    CurrentAdmin other = (CurrentAdmin) obj;
    if (admin == null) {
      if (other.admin != null)
        return false;
    } else if (!admin.equals(other.admin))
      return false;
    return true;
  }


}
