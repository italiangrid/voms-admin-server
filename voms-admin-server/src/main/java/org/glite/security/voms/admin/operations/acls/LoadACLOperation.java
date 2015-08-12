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
package org.glite.security.voms.admin.operations.acls;

import org.glite.security.voms.admin.error.NullArgumentException;
import org.glite.security.voms.admin.operations.BaseVomsOperation;
import org.glite.security.voms.admin.operations.VOMSContext;
import org.glite.security.voms.admin.operations.VOMSPermission;
import org.glite.security.voms.admin.operations.groups.FindContextOperation;
import org.glite.security.voms.admin.util.PathNamingScheme;

public class LoadACLOperation extends BaseVomsOperation {

  String contextString;
  boolean defaultACL;

  public static LoadACLOperation instance(String contextString) {

    return new LoadACLOperation(contextString);
  }

  public static LoadACLOperation instance(String contextString,
    boolean defaultACL) {

    return new LoadACLOperation(contextString, defaultACL);
  }

  private LoadACLOperation(String contextString) {

    this(contextString, false);
  }

  private LoadACLOperation(String contextString, boolean defaultACL) {

    if (contextString == null)
      throw new NullArgumentException("contextString cannot be null!");

    PathNamingScheme.checkSyntax(contextString);

    this.contextString = contextString;
    this.defaultACL = defaultACL;
  }

  @Override
  protected Object doExecute() {

    VOMSContext context = (VOMSContext) FindContextOperation.instance(
      contextString).execute();

    if (defaultACL && PathNamingScheme.isQualifiedRole(contextString))
      throw new IllegalArgumentException(
        "Role contexts do not have default ACLs!");

    if (defaultACL)
      return context.getGroup().getDefaultACL();

    return context.getACL();
  }

  @Override
  protected void setupPermissions() {

    addRequiredPermission(VOMSContext.instance(contextString), VOMSPermission
      .getEmptyPermissions().setACLReadPermission());

    if (defaultACL)
      addRequiredPermission(VOMSContext.instance(contextString), VOMSPermission
        .getEmptyPermissions().setACLReadPermission().setACLDefaultPermission());
  }

}
