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

package org.glite.security.voms.admin.view.actions.register.orgdb;

import java.util.ArrayList;
import java.util.List;

import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.glite.security.voms.admin.integration.PluginConfigurator;
import org.glite.security.voms.admin.integration.PluginManager;
import org.glite.security.voms.admin.integration.orgdb.OrgDBConfigurator;
import org.glite.security.voms.admin.integration.orgdb.dao.OrgDBDAOFactory;
import org.glite.security.voms.admin.integration.orgdb.dao.OrgDBVOMSPersonDAO;
import org.glite.security.voms.admin.integration.orgdb.model.VOMSOrgDBPerson;
import org.glite.security.voms.admin.operations.CurrentAdmin;
import org.glite.security.voms.admin.persistence.model.request.RequesterInfo;
import org.glite.security.voms.admin.view.actions.BaseAction;

import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import com.opensymphony.xwork2.validator.annotations.EmailValidator;
import com.opensymphony.xwork2.validator.annotations.RequiredStringValidator;
import com.opensymphony.xwork2.validator.annotations.ValidatorType;


@Results( { 
	
	@Result(name = BaseAction.SUCCESS, location = "searchResults.jsp"),
	@Result(name = BaseAction.INPUT, location = "searchResults.jsp")
	
})
public class QueryDatabaseAction extends BaseAction implements
		ModelDriven<List<VOMSOrgDBPerson>>, Preparable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	protected RequesterInfo requester;

	String emailAddress;

	String name;
	String surname;

	List<VOMSOrgDBPerson> searchResults;
	
	String experimentName;
	
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
		
	

	@Override
	public String execute() throws Exception {

		try {
			
			requester.setName(name);
			requester.setSurname(surname);
			requester.setEmailAddress(emailAddress);

			PluginConfigurator configuredPlugin = PluginManager.instance()
					.getConfiguredPlugin(OrgDBConfigurator.class.getName());

			experimentName = configuredPlugin
					.getPluginProperty(OrgDBConfigurator.ORGDB_EXPERIMENT_NAME_PROPERTY);
			
			OrgDBVOMSPersonDAO dao = OrgDBDAOFactory.instance()
					.getVOMSPersonDAO();

			searchResults = new ArrayList<VOMSOrgDBPerson>();
			
			VOMSOrgDBPerson exactMatch =  dao
					.findPersonWithValidExperimentParticipationByEmail(
							emailAddress, experimentName);

			if (exactMatch == null) {

				
				searchResults = dao
						.findPersonsWithValidExperimentParticipationByName(
								name, surname, experimentName);
				
				if (searchResults.isEmpty()) {
					addActionError(String
							.format(
									"No valid participation found for '%s %s' for experiment '%s'",
									name, surname, experimentName));
				}else{
					addActionMessage(String.format("Matches found for name '%s %s' for experiment '%s'", name, surname, experimentName)); 
				}
			
			}else{
				
				addActionMessage("Found the following match for email '"+emailAddress+"'.");
				searchResults.add(exactMatch);
			}

		} catch (Exception e) {
			addActionError(e.getMessage());
		}

		return SUCCESS;

	}

	

	
	
	@RequiredStringValidator(type = ValidatorType.FIELD, message = "Please enter your email address.")
	@EmailValidator(type = ValidatorType.FIELD, message = "Please enter a valid email address.")
	public String getEmailAddress() {
		return emailAddress;
	}

	/**
	 * @param emailAddress
	 *            the emailAddress to set
	 */
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}
	

	@RequiredStringValidator(type = ValidatorType.FIELD, message = "Please enter your name.")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@RequiredStringValidator(type = ValidatorType.FIELD, message = "Please enter your surname.")
	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public List<VOMSOrgDBPerson> getModel() {
		
		return searchResults;
	}

	public String getExperimentName() {
		return experimentName;
	}


	public RequesterInfo getRequester() {
		return requester;
	}
	
}
