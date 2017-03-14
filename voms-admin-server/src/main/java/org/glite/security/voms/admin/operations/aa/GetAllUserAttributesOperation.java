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
package org.glite.security.voms.admin.operations.aa;

import it.infn.cnaf.voms.aa.VOMSAA;
import it.infn.cnaf.voms.aa.VOMSAttributeAuthority;
import it.infn.cnaf.voms.aa.VOMSAttributes;

import org.glite.security.voms.admin.operations.BaseVomsOperation;
import org.glite.security.voms.admin.operations.VOMSContext;
import org.glite.security.voms.admin.operations.VOMSPermission;

public class GetAllUserAttributesOperation extends BaseVomsOperation {

  String voMemberCertSubject;

  public GetAllUserAttributesOperation(String certSubject) {

    voMemberCertSubject = certSubject;
  }

  @Override
  protected Object doExecute() {

    VOMSAttributeAuthority aa = VOMSAA.getVOMSAttributeAuthority();

    VOMSAttributes attrs = aa.getAllVOMSAttributes(voMemberCertSubject);

    return attrs;
  }

  @Override
  protected void setupPermissions() {

    addRequiredPermission(VOMSContext.getVoContext(), VOMSPermission
      .getContainerReadPermission().setMembershipReadPermission()
      .setAttributesReadPermission());

  }

}
