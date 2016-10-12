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
package org.glite.security.voms.admin.view.actions.register.orgdb;

import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;
import org.glite.security.voms.admin.core.VOMSServiceConstants;
import org.glite.security.voms.admin.error.IllegalStateException;
import org.glite.security.voms.admin.integration.PluginConfigurator;
import org.glite.security.voms.admin.integration.PluginManager;
import org.glite.security.voms.admin.integration.orgdb.OrgDBConfigurator;
import org.glite.security.voms.admin.integration.orgdb.dao.OrgDBDAOFactory;
import org.glite.security.voms.admin.integration.orgdb.database.OrgDBError;
import org.glite.security.voms.admin.integration.orgdb.model.Institute;
import org.glite.security.voms.admin.integration.orgdb.model.VOMSOrgDBPerson;
import org.glite.security.voms.admin.view.actions.register.RegisterActionSupport;

public class OrgDbRegisterActionSupport extends RegisterActionSupport implements
  SessionAware {

  /**
	 * 
	 */
  private static final long serialVersionUID = 1L;

  public static final String ORGDB_RECORD_SESSION_KEY = "___voms.user-orgdb-record";

  Long vomsPersonId;

  Map<String, Object> session;

  protected void populateRequesterFromOrgdb() {

    if (requester == null)
      requester = requesterInfoFromCurrentAdmin();

    if (session == null)
      throw new IllegalStateException("Session is null for this user!");

    VOMSOrgDBPerson orgDbPerson = (VOMSOrgDBPerson) session
      .get(ORGDB_RECORD_SESSION_KEY);

    if (orgDbPerson == null) {

      if (vomsPersonId == null) {
        throw new IllegalArgumentException(
          "No id provided for the VOMS person orgdb record and no record found in session!");

      } else {

        orgDbPerson = OrgDBDAOFactory.instance().getVOMSPersonDAO()
          .findById(vomsPersonId, false);
        if (orgDbPerson == null) {
          throw new IllegalArgumentException("No orgb record found for id '"
            + vomsPersonId + "'.");
        }

        session.put(ORGDB_RECORD_SESSION_KEY, orgDbPerson);
      }

    }

    PluginConfigurator configuredPlugin = PluginManager.instance()
      .getConfiguredPlugin(OrgDBConfigurator.class.getName());

    String experimentName = configuredPlugin
      .getPluginProperty(OrgDBConfigurator.ORGDB_EXPERIMENT_NAME_PROPERTY);

    requester.setName(orgDbPerson.getFirstName());
    requester.setSurname(orgDbPerson.getName());
    requester.setEmailAddress(orgDbPerson.getPhysicalEmail());
    
    Institute institute = orgDbPerson.getValidParticipationForExperiment(
      experimentName).getInstitute();

    if (institute == null) {
      String errorMessage = String.format(
        "Null institute found for valid participation."
          + " Requester email address: %s. Experiment: %s",
        requester.getEmailAddress(), experimentName);

      throw new OrgDBError(errorMessage);

    }
    
    requester.setInstitution(institute.getName());
    requester.setPhoneNumber(orgDbPerson.getTel1());
    requester.setAddress(orgDbPerson.getAddressForVOMS());
    
    requester.addInfo(VOMSServiceConstants.ORGDB_ID_KEY, 
      orgDbPerson.getId().toString());

  }

  @Override
  public void prepare() throws Exception {

    super.prepare();
    populateRequesterFromOrgdb();
  }

  public Long getVomsPersonId() {

    return vomsPersonId;
  }

  public void setVomsPersonId(Long vomsPersonId) {

    this.vomsPersonId = vomsPersonId;
  }

  public void setSession(Map<String, Object> session) {

    this.session = session;

  }

}
