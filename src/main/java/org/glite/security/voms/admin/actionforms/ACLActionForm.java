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
 * Contributors:
 *     Andrea Ceccanti - andrea.ceccanti@cnaf.infn.it
 *******************************************************************************/


package org.glite.security.voms.admin.actionforms;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorActionForm;


public class ACLActionForm extends ValidatorActionForm{

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    Long aclId;
    Long adminId;
    String[] selectedPermissions;

	protected boolean propagated;
    
    public ACLActionForm() {

        super();
     
    }

    
    public Long getAclId() {
    
        return aclId;
    }

    
    public void setAclId( Long aclId ) {
    
        this.aclId = aclId;
    }

    
    public Long getAdminId() {
    
        return adminId;
    }

    
    public void setAdminId( Long adminId ) {
    
        this.adminId = adminId;
    }

    
    public String[] getSelectedPermissions() {
    
        return selectedPermissions;
    }

    
    public void setSelectedPermissions( String[] selectedPermissions ) {
    
        this.selectedPermissions = selectedPermissions;
    }

    public String toString() {
    
        return ToStringBuilder.reflectionToString(this);
        
    }
    
    
    public boolean getPropagated() {
		return propagated;
	}
    
    public boolean isPropagated() {
		return propagated;
	}


	public void setPropagated(boolean propagated) {
		this.propagated = propagated;
	}


	public void reset( ActionMapping mapping, HttpServletRequest request ) {
    
        super.reset( mapping, request );
        aclId = null;
        adminId = null;
        selectedPermissions = null;
        propagated = false;
    }
}
