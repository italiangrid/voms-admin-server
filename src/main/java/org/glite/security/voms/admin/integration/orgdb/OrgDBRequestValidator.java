package org.glite.security.voms.admin.integration.orgdb;

import java.util.List;
import java.util.Vector;

import org.glite.security.voms.admin.integration.RequestValidator;
import org.glite.security.voms.admin.integration.ValidationResult;
import org.glite.security.voms.admin.integration.orgdb.dao.OrgDBDAOFactory;
import org.glite.security.voms.admin.integration.orgdb.dao.OrgDBVOMSPersonDAO;
import org.glite.security.voms.admin.integration.orgdb.database.OrgDBError;
import org.glite.security.voms.admin.integration.orgdb.model.VOMSPerson;
import org.glite.security.voms.admin.persistence.model.request.NewVOMembershipRequest;

public class OrgDBRequestValidator implements
		RequestValidator<NewVOMembershipRequest> {

	String experimentName;

	public OrgDBRequestValidator(String experimentName) {
		this.experimentName = experimentName;
	}

	protected void propertyEqualsIgnoreCase(String value1, String value2, String propertyName, List<String> errors){
		
		if (!value1.equalsIgnoreCase(value2)){
			
			String errorMessage = String.format("Property '"+propertyName+"' does not match (ignoring case) the OrgDB VOMS person record. You entered  '%s', while  '%s' was expected.",
					value1, value2);
			errors.add(errorMessage);
		}
		
	}
	protected List<String> checkRequestAgainstParticipation(NewVOMembershipRequest r, VOMSPerson p){
		
		List<String> errors = new Vector<String>();
		
		propertyEqualsIgnoreCase(r.getRequesterInfo().getName(), p.getFirstName(), "name", errors);
		propertyEqualsIgnoreCase(r.getRequesterInfo().getSurname(), p.getName(), "surname", errors);
				
		return errors;
		
	}
	public ValidationResult validateRequest(NewVOMembershipRequest r) {

		try {

			String email = r.getRequesterInfo().getEmailAddress();
			OrgDBVOMSPersonDAO dao = OrgDBDAOFactory.instance()
					.getVOMSPersonDAO();

			VOMSPerson p = dao
					.findPersonWithValidExperimentParticipationByEmail(email,
							experimentName);

			if (p != null) {
				
				List<String> errors = checkRequestAgainstParticipation(r, p);
				
				if (!errors.isEmpty()){
					ValidationResult result = ValidationResult.failure("OrgDb validation failed. The OrgDb VOMS person record linked to email address '"+email+"' did not match the data you entered.");
					result.setErrorMessages(errors);
					return result;
				}
				else{
					return ValidationResult.success();
				}
			
			} else {

				ValidationResult result = ValidationResult
						.failure("No OrgDB participation found matching email '"
								+ email
								+ "' for experiment '"
								+ experimentName
								+ "'.");
				
				return result;
			}

		} catch (OrgDBError e) {
			return ValidationResult.error(e.getMessage(), e);
		}
	}

	public String getExperimentName() {
		return experimentName;
	}

	public void setExperimentName(String experimentName) {
		this.experimentName = experimentName;
	}

}
