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
package org.glite.security.voms.admin.view.actions.register.hr;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.interceptor.SessionAware;
import org.glite.security.voms.admin.core.VOMSServiceConstants;
import org.glite.security.voms.admin.integration.PluginManager;
import org.glite.security.voms.admin.integration.cern.HrDbApiService;
import org.glite.security.voms.admin.integration.cern.HrDbConfigurator;
import org.glite.security.voms.admin.integration.cern.HrDbError;
import org.glite.security.voms.admin.integration.cern.dto.ParticipationDTO;
import org.glite.security.voms.admin.integration.cern.dto.VOPersonDTO;
import org.glite.security.voms.admin.operations.CurrentAdmin;
import org.glite.security.voms.admin.persistence.model.request.RequesterInfo;
import org.glite.security.voms.admin.view.actions.BaseAction;

import com.google.common.collect.Lists;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;

@Results({

    @Result(name = BaseAction.SUCCESS, location = "searchResults.jsp"),
    @Result(name = BaseAction.INPUT, location = "searchResults.jsp")

})
public class ResolveHrIdAction extends BaseAction
    implements ModelDriven<List<VOPersonDTO>>, Preparable, SessionAware {

  public static final String VO_PERSON_KEY = "__voms.voPerson";
  public static final String REQUESTER_INFO_KEY = "__voms.requesterInfo";


  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  RequesterInfo requester;

  String emailAddress;

  List<VOPersonDTO> searchResults;

  String experimentName;

  Instant now;

  Map<String, Object> session;

  protected RequesterInfo requesterInfoFromCurrentAdmin() {

    RequesterInfo i = new RequesterInfo();
    CurrentAdmin admin = CurrentAdmin.instance();

    i.setCertificateSubject(admin.getRealSubject());
    i.setCertificateIssuer(admin.getRealIssuer());
    i.setEmailAddress(admin.getRealEmailAddress());

    return i;

  }

  public void prepare() throws Exception {
    requester = requesterInfoFromCurrentAdmin();
  }

  public RequesterInfo getRequester() {
    return requester;
  }

  public void setRequester(RequesterInfo requester) {
    this.requester = requester;
  }

  public String getEmailAddress() {
    return emailAddress;
  }

  public void setEmailAddress(String emailAddress) {
    this.emailAddress = emailAddress;
  }

  public List<VOPersonDTO> getSearchResults() {
    return searchResults;
  }

  @Override
  public String execute() throws Exception {

    searchResults = Lists.newArrayList();
    HrDbConfigurator hrPlugin = (HrDbConfigurator) PluginManager.instance()
      .getConfiguredPlugin(HrDbConfigurator.class.getName());

    experimentName = hrPlugin.getHrConfig().getExperimentName();
    now = Instant.now();

    HrDbApiService apiService = hrPlugin.getApiService();
    try {
      if (apiService.hasValidExperimentParticipationByEmail(emailAddress)) {
        Optional<VOPersonDTO> voPerson = apiService.getVoPersonRecordByEmail(emailAddress);

        // Can't use functional approach otherwise old struts bytecode gets confused??
        if (voPerson.isPresent()) {

          VOPersonDTO p = voPerson.get();
          session.put(VO_PERSON_KEY, p);
          searchResults.add(p);
          requester.setName(p.getFirstName());
          requester.setSurname(p.getName());
          requester.setEmailAddress(emailAddress);
          Optional<ParticipationDTO> pp =
              p.findValidParticipationForExperiment(now, experimentName);
          if (pp.isPresent()) {
            requester.setInstitution(pp.get().getInstitute().getName());
          } else {
            requester.setInstitution("N/A");
          }
          
          requester.addInfo(VOMSServiceConstants.ORGDB_ID_KEY, p.getId().toString());

          session.put(REQUESTER_INFO_KEY, requester);
        }
      } else {

        addActionError(
            String.format("No valid participation found for email '%s' in experiment '%s'",
                emailAddress, hrPlugin.getHrConfig().getExperimentName()));
      }
    } catch (HrDbError e) {
      addActionError(e.getMessage());
    }
    return SUCCESS;
  }

  @Override
  public List<VOPersonDTO> getModel() {
    return searchResults;
  }

  public String getExperimentName() {
    return experimentName;
  }

  public Instant getNow() {
    return now;
  }

  @Override
  public void setSession(Map<String, Object> session) {
    this.session = session;
  }
}
