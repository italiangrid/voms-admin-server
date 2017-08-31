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

import java.util.concurrent.Callable;

import org.glite.security.voms.admin.apiv2.AUPVersionJSON;
import org.glite.security.voms.admin.operations.VOAdminOperation;
import org.glite.security.voms.admin.persistence.dao.generic.AUPDAO;
import org.glite.security.voms.admin.persistence.dao.generic.AUPVersionDAO;
import org.glite.security.voms.admin.persistence.dao.generic.DAOFactory;
import org.glite.security.voms.admin.persistence.model.AUPVersion;

public class ImportAupVersionOperation implements Callable<AUPVersion> {

  final AUPVersionJSON aupVersion;

  private ImportAupVersionOperation(AUPVersionJSON v) {
    aupVersion = v;
  }

  @Override
  public AUPVersion call() throws Exception {
    AUPDAO aupDAO = DAOFactory.instance().getAUPDAO();
    
    AUPVersionDAO dao = DAOFactory.instance().getAUPVersionDAO();
    AUPVersion v = dao.findByVersion(aupVersion.getVersion());
    
    if (v == null) {
      v = new AUPVersion();
      v.setAup(aupDAO.getVOAUP());
    }
    
    v.setVersion(aupVersion.getVersion());
    v.setActive(aupVersion.getActive());
    v.setCreationTime(aupVersion.getCreationTime());
    v.setLastForcedReacceptanceTime(aupVersion.getLastForcedReacceptanceTime());
    v.setText(aupVersion.getText());
    v.setUrl(aupVersion.getUrl());
    
    dao.makePersistent(v);
    return v;
  }

  public static VOAdminOperation<AUPVersion> instance(AUPVersionJSON v) {
    return new VOAdminOperation<>(new ImportAupVersionOperation(v));
  }
}
