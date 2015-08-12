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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.lang.builder.StandardToStringStyle;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.glite.security.voms.admin.error.NullArgumentException;
import org.glite.security.voms.admin.error.VOMSAuthorizationException;
import org.glite.security.voms.admin.error.VOMSFatalException;
import org.glite.security.voms.admin.persistence.dao.VOMSGroupDAO;
import org.glite.security.voms.admin.persistence.error.VOMSInconsistentDatabaseException;
import org.glite.security.voms.admin.persistence.model.ACL;
import org.glite.security.voms.admin.persistence.model.VOMSGroup;

public abstract class BaseVomsOperation<V> implements VOMSOperation<V> {

  static {

    StandardToStringStyle stss = new StandardToStringStyle();

    stss.setUseClassName(false);
    stss.setUseIdentityHashCode(false);

    ToStringBuilder.setDefaultStyle(stss);

  }

  protected Map<VOMSContext, VOMSPermission> __requiredPermissions = null;

  private static final Logger __log = LoggerFactory
    .getLogger(BaseVomsOperation.class);

  private boolean permissionsInitialized() {

    if (__requiredPermissions == null || __requiredPermissions.isEmpty())
      return false;

    return true;
  }

  protected final void addRequiredPermission(VOMSContext ctxt, VOMSPermission p) {

    if (ctxt == null)
      throw new NullArgumentException(
        "Cannot add a null context to required permissions");

    if (p == null)
      throw new NullArgumentException(
        "Cannot set a null permission for a context.");

    __requiredPermissions.put(ctxt, p);
  }

  protected final void addRequiredPermissionOnAllGroups(VOMSPermission p) {

    Iterator<VOMSGroup> allGroups = VOMSGroupDAO.instance().findAll()
      .iterator();

    while (allGroups.hasNext()) {
      addRequiredPermission(VOMSContext.instance((VOMSGroup) allGroups.next()),
        p);
    }
  }

  protected final void addRequiredPermissionOnPath(VOMSGroup leafGroup,
    VOMSPermission p) {

    if (leafGroup == null)
      throw new NullArgumentException(
        "Cannot add a null context to required permissions");

    if (p == null)
      throw new NullArgumentException(
        "Cannot set a null permission for a context.");

    addRequiredPermission(VOMSContext.instance(leafGroup), p);

    VOMSGroup parent = leafGroup.getParent();

    do {
      addRequiredPermission(VOMSContext.instance(parent), p);
      parent = parent.getParent();

    } while (!parent.isRootGroup());

  }

  protected BaseVomsOperation() {

    __requiredPermissions = new HashMap<VOMSContext, VOMSPermission>();
  }

  protected AuthorizationResponse isAllowed() {

    CurrentAdmin admin = CurrentAdmin.instance();

    if (!permissionsInitialized())
      setupPermissions();

    if (__requiredPermissions.isEmpty())
      throw new VOMSFatalException("Required permissions not defined for "
        + getName() + " operation!");

    Iterator<VOMSContext> contexts = __requiredPermissions.keySet().iterator();

    while (contexts.hasNext()) {

      VOMSContext ctxt = contexts.next();
      VOMSPermission requiredPerms = __requiredPermissions.get(ctxt);

      ACL acl = ctxt.getACL();

      if (acl == null)
        throw new VOMSInconsistentDatabaseException(
          "ACL not found for context \"" + ctxt + "\".");

      if (!admin.hasPermissions(ctxt, requiredPerms))
        return AuthorizationResponse.deny(ctxt, requiredPerms);

    }
    return AuthorizationResponse.permit();
  }

  public final V execute() {

    logOperation();

    AuthorizationResponse response = isAllowed();

    if (!response.isAllowed())
      throw new VOMSAuthorizationException(CurrentAdmin.instance().getAdmin(),
        this, response);

    return doExecute();

  }

  protected abstract void setupPermissions();

  protected abstract V doExecute();

  protected final void addRequiredPermissionsOnPath(VOMSGroup g, VOMSPermission p) {

    VOMSGroup parentGroup = g.getParent();

    do {
      __log.debug("Adding required permission " + p + " for group: "
        + parentGroup);
      addRequiredPermission(VOMSContext.instance(parentGroup), p);
      parentGroup = parentGroup.getParent();

    } while (!parentGroup.isRootGroup());

    addRequiredPermission(VOMSContext.instance(parentGroup), p);
  }

  protected final void logRequiredPermissions() {

    __log.debug("[" + this.getClass() + "] requiredPerms: "
      + ToStringBuilder.reflectionToString(__requiredPermissions) + "");
  }

  protected final String logOperationMessage() {

    StringBuffer logStr = new StringBuffer();

    String opName = getName();
    logStr.append(opName + "(");
    logStr.append(logArgs());
    logStr.append(")");

    return logStr.toString();

  }

  protected String logArgs() {

    String message = ToStringBuilder.reflectionToString(this);

    // FIXME: really a quick n' dirty trick to build these log messages...:(
    message = message.replaceAll(",?__\\p{Alpha}*=[^,\\]]*,?", "");
    return message;
  }

  protected final void logOperation() {

    String adminSubj = CurrentAdmin.instance().getRealSubject();
    String adminIssuer = CurrentAdmin.instance().getRealIssuer();

    String message = String.format("Operation: %s - (%s,%s)",
      logOperationMessage(), adminSubj, adminIssuer);

    __log.info(message);

  }

  public String getName() {

    String clazzName = this.getClass().getName();
    return clazzName.substring(clazzName.lastIndexOf('.') + 1);

  }

  public final Map<VOMSContext, VOMSPermission> getRequiredPermissions() {

    if (!permissionsInitialized())
      setupPermissions();

    return __requiredPermissions;
  }

}
