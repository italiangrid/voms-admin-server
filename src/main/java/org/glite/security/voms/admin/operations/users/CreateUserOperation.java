/*******************************************************************************
 *Copyright (c) Members of the EGEE Collaboration. 2006. 
 *See http://www.eu-egee.org/partners/ for details on the copyright
 *holders.  
 *
 *Licensed under the Apache License, Version 2.0 (the "License"); 
 *you may not use this file except in compliance with the License. 
 *You may obtain a copy of the License at 
 *
 *    http://www.apache.org/licenses/LICENSE-2.0 
 *
 *Unless required by applicable law or agreed to in writing, software 
 *distributed under the License is distributed on an "AS IS" BASIS, 
 *WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 *See the License for the specific language governing permissions and 
 *limitations under the License.
 *
 * Authors:
 *     Andrea Ceccanti - andrea.ceccanti@cnaf.infn.it
 *******************************************************************************/
package org.glite.security.voms.admin.operations.users;

import org.glite.security.voms.admin.actionforms.UserForm;
import org.glite.security.voms.admin.dao.VOMSUserDAO;
import org.glite.security.voms.admin.model.VOMSUser;
import org.glite.security.voms.admin.operations.BaseVomsOperation;
import org.glite.security.voms.admin.operations.VOMSContext;
import org.glite.security.voms.admin.operations.VOMSPermission;


public class CreateUserOperation extends BaseVomsOperation {

	VOMSUser usr = null;

    String caDN = null;
    
    private CreateUserOperation(UserForm form){
    	
    		usr = new VOMSUser();
            usr.setName( form.getName() );
            usr.setSurname( form.getSurname() );
            usr.setInstitution( form.getInstitution() );
            usr.setAddress( form.getAddress() );
            usr.setPhoneNumber( form.getPhoneNumber());
            usr.setEmailAddress( form.getEmailAddress() );
            
    }
    
    private CreateUserOperation(String username, String caName, String cn,String certUri,String email){
        usr = new VOMSUser();
        
        usr.setDn(username);
        usr.setCn(cn);
        usr.setCertURI(certUri);
        usr.setEmailAddress(email);
        caDN = caName;
        
    }
    
    
    protected Object doExecute() {
		
		return VOMSUserDAO.instance().create(usr);
	}

	public static CreateUserOperation instance(UserForm form){
		
		return new CreateUserOperation(form);
	}
    
    public static CreateUserOperation instance(String username, String caName, String cn,String certUri,String email){
        return new CreateUserOperation(username, caName, cn,certUri,email);
    }

	protected void setupPermissions() {
		
		addRequiredPermission(VOMSContext.getVoContext(),VOMSPermission.getContainerRWPermissions().setMembershipRWPermission());
		
	}
	
}
