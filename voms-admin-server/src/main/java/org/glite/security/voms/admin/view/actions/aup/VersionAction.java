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
package org.glite.security.voms.admin.view.actions.aup;

import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.glite.security.voms.admin.persistence.dao.generic.AUPVersionDAO;
import org.glite.security.voms.admin.persistence.dao.generic.DAOFactory;
import org.glite.security.voms.admin.persistence.model.AUPVersion;
import org.glite.security.voms.admin.view.actions.BaseAction;

import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;

@Results({ @Result(name = BaseAction.INPUT, location = "aupVersionDetail"),
  @Result(name = BaseAction.SUCCESS, location = "aupVersionDetail") })
public class VersionAction extends BaseAction implements
  ModelDriven<AUPVersion>, Preparable {

  /**
	 * 
	 */
  private static final long serialVersionUID = 1L;

  Long aupVersionId;
  AUPVersion version;

  public AUPVersion getModel() {

    return version;
  }

  public void prepare() throws Exception {

    if (version == null) {

      AUPVersionDAO dao = DAOFactory.instance().getAUPVersionDAO();
      version = dao.findById(aupVersionId, false);

    }

  }

  public Long getAupVersionId() {

    return aupVersionId;
  }

  public void setAupVersionId(Long aupVersionId) {

    this.aupVersionId = aupVersionId;
  }

}
