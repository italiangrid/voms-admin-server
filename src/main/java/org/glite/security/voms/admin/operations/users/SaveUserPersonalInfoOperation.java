package org.glite.security.voms.admin.operations.users;

import org.glite.security.voms.admin.dao.VOMSUserDAO;
import org.glite.security.voms.admin.model.VOMSUser;
import org.glite.security.voms.admin.operations.BaseUserAdministrativeOperation;
import org.glite.security.voms.admin.operations.VOMSContext;
import org.glite.security.voms.admin.operations.VOMSPermission;

public class SaveUserPersonalInfoOperation extends
		BaseUserAdministrativeOperation {

	VOMSUser targetUser;
	
	String name;
	String surname;
	String institution;
	String address;
	String phoneNumber;
	String emailAddress;
	
	
	public SaveUserPersonalInfoOperation(VOMSUser targetUser,String name, String surname, String institution, String address, String phoneNumber, String emailAddress) {
		this.targetUser = targetUser;
		this.name = name;
		this.surname = surname;
		this.institution = institution;
		this.address = address;
		this.phoneNumber = phoneNumber;
		this.emailAddress = emailAddress;
	}
	
	@Override
	protected Object doExecute() {
		
		if (targetUser == null)
			targetUser = getAuthorizedUser();
		targetUser.setName(name);
		targetUser.setSurname(surname);
		
		targetUser.setInstitution(institution);
		targetUser.setAddress(address);
		targetUser.setPhoneNumber(phoneNumber);
		targetUser.setEmailAddress(emailAddress);
		
		VOMSUserDAO.instance().update(targetUser);
		
		return null;
	}


	@Override
	protected void setupPermissions() {
		addRequiredPermission(VOMSContext.getVoContext(), VOMSPermission.getContainerReadPermission().setMembershipReadPermission().setPersonalInfoReadPermission().setPersonalInfoWritePermission());		
	}

}
