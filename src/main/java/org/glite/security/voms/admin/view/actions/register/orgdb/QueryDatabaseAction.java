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
import org.glite.security.voms.admin.integration.orgdb.model.VOMSPerson;
import org.glite.security.voms.admin.view.actions.BaseAction;

import com.opensymphony.xwork2.ModelDriven;

@ParentPackage("base")
@Results( { @Result(name = BaseAction.SUCCESS, location = "searchResults.jsp") })
public class QueryDatabaseAction extends BaseAction implements
		ModelDriven<List<VOMSPerson>> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	String emailAddress;

	String name;
	String surname;

	List<VOMSPerson> searchResults;
	
	String experimentName;
	
		
	protected void validateAjaxInput() {

		if (emailAddress == null || "".equals(emailAddress.trim())) {
			addActionError("Please provide email address!");
		}

		if (name == null || "".equals(name.trim())) {
			addActionError("Please enter your name!");
		}

		if (surname == null || "".equals(surname.trim())) {
			addActionError("Please enter your surname!");
		}

	}

	@Override
	public String execute() throws Exception {

		try {

			validateAjaxInput();

			if (hasActionErrors())
				return SUCCESS;

			PluginConfigurator configuredPlugin = PluginManager.instance()
					.getConfiguredPlugin(OrgDBConfigurator.class.getName());

			experimentName = configuredPlugin
					.getPluginProperty(OrgDBConfigurator.ORGDB_EXPERIMENT_NAME_PROPERTY);
			OrgDBVOMSPersonDAO dao = OrgDBDAOFactory.instance()
					.getVOMSPersonDAO();

			searchResults = new ArrayList<VOMSPerson>();
			
			VOMSPerson exactMatch =  dao
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

	

	/**
	 * @return the emailAddress
	 */
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
	

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public List<VOMSPerson> getModel() {
		
		return searchResults;
	}

	public String getExperimentName() {
		return experimentName;
	}
	
}
