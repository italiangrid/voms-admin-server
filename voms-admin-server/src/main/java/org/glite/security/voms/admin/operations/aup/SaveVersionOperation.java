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
package org.glite.security.voms.admin.operations.aup;

import org.glite.security.voms.admin.event.EventManager;
import org.glite.security.voms.admin.event.vo.aup.AUPVersionUpdatedEvent;
import org.glite.security.voms.admin.operations.GenericSaveOrUpdateOperation;
import org.glite.security.voms.admin.operations.VOMSContext;
import org.glite.security.voms.admin.operations.VOMSPermission;
import org.glite.security.voms.admin.persistence.model.AUPVersion;

public class SaveVersionOperation extends
  GenericSaveOrUpdateOperation<AUPVersion> {

  String newURL;

  public SaveVersionOperation(AUPVersion theModel, String url) {

    super(theModel);
    this.newURL = url;
  }

  @Override
  protected Object doExecute() {

    model.setUrl(newURL);
    EventManager.instance().dispatch(new AUPVersionUpdatedEvent(model.getAup(), model));

    return super.doExecute();
  }

  @Override
  protected void setupPermissions() {

    addRequiredPermission(VOMSContext.getVoContext(), VOMSPermission
      .getContainerRWPermissions().setMembershipRWPermission());
  }

}
