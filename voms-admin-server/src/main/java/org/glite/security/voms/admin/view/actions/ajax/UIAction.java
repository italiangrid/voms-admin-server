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
package org.glite.security.voms.admin.view.actions.ajax;

import java.util.HashMap;
import java.util.Map;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.interceptor.SessionAware;
import org.glite.security.voms.admin.core.VOMSServiceConstants;
import org.glite.security.voms.admin.view.actions.BaseAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ParentPackage("json-light")
@Results({ @Result(name = BaseAction.SUCCESS, type = "json") })
public class UIAction extends BaseAction implements SessionAware {

  public static final Logger log = LoggerFactory.getLogger(UIAction.class);

  /**
	 * 
	 */
  private static final long serialVersionUID = 1L;

  String panelId;
  Boolean visible;

  Map<String, Boolean> statusMap;

  private transient Map<String, Object> session;

  @Action("store")
  public String store() {

    log.debug("panelId: " + panelId);
    log.debug("visible" + visible);

    statusMap = (HashMap<String, Boolean>) session
      .get(VOMSServiceConstants.STATUS_MAP_KEY);

    if (statusMap == null)
      statusMap = new HashMap<String, Boolean>();

    if (panelId != null)
      statusMap.put(panelId, visible);

    session.put(VOMSServiceConstants.STATUS_MAP_KEY, statusMap);

    return SUCCESS;
  }

  @Action("status")
  public String status() {

    statusMap = (HashMap<String, Boolean>) session
      .get(VOMSServiceConstants.STATUS_MAP_KEY);

    return SUCCESS;
  }

  public void setSession(Map<String, Object> session) {

    this.session = session;
  }

  public String getPanelId() {

    return panelId;
  }

  public void setPanelId(String panelId) {

    this.panelId = panelId;
  }

  public Boolean getVisible() {

    return visible;
  }

  public void setVisible(Boolean visible) {

    this.visible = visible;
  }

  public Map<String, Boolean> getStatusMap() {

    return statusMap;
  }

  public void setStatusMap(Map<String, Boolean> statusMap) {

    this.statusMap = statusMap;
  }

}
