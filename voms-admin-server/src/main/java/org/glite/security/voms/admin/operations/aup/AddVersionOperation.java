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
package org.glite.security.voms.admin.operations.aup;

import java.net.MalformedURLException;
import java.net.URL;

import org.glite.security.voms.admin.error.VOMSException;
import org.glite.security.voms.admin.event.EventManager;
import org.glite.security.voms.admin.event.vo.aup.AUPVersionCreatedEvent;
import org.glite.security.voms.admin.operations.BaseVomsOperation;
import org.glite.security.voms.admin.operations.VOMSContext;
import org.glite.security.voms.admin.operations.VOMSPermission;
import org.glite.security.voms.admin.persistence.dao.generic.AUPDAO;
import org.glite.security.voms.admin.persistence.dao.generic.DAOFactory;
import org.glite.security.voms.admin.persistence.model.AUP;
import org.glite.security.voms.admin.persistence.model.AUPVersion;

public class AddVersionOperation extends BaseVomsOperation {

  AUP aup;

  String version;

  String url;

  public AddVersionOperation(AUP aup, String version, String url) {

    this.aup = aup;
    this.version = version;
    this.url = url;
  }

  @Override
  protected Object doExecute() {

    AUPDAO dao = DAOFactory.instance().getAUPDAO();

    try {

      AUPVersion v  = dao.addVersion(aup, version, new URL(url));
      
      EventManager.instance().dispatch(new AUPVersionCreatedEvent(aup, v));

    } catch (MalformedURLException e) {

      throw new VOMSException("Malformed URL passed as argument: " + url, e);
    }

    return aup;
  }

  @Override
  protected void setupPermissions() {

    addRequiredPermission(VOMSContext.getVoContext(), VOMSPermission
      .getContainerRWPermissions().setMembershipRWPermission());
  }

}
