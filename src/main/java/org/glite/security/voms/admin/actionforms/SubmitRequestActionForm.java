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
package org.glite.security.voms.admin.actionforms;

import org.apache.struts.validator.ValidatorActionForm;


public class SubmitRequestActionForm extends ValidatorActionForm {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    boolean aupAccepted;

    String emailAddress;
    String institute;
    String phoneNumber;
    String comments;
    
    
    public boolean isAupAccepted() {
    
        return aupAccepted;
    }

    
    public void setAupAccepted( boolean aupAccepted ) {
    
        this.aupAccepted = aupAccepted;
    }


    
    public String getComments() {
    
        return comments;
    }


    
    public void setComments( String comments ) {
    
        this.comments = comments;
    }


    
    public String getInstitute() {
    
        return institute;
    }


    
    public void setInstitute( String institute ) {
    
        this.institute = institute;
    }


    
    public String getPhoneNumber() {
    
        return phoneNumber;
    }


    
    public void setPhoneNumber( String phoneNumber ) {
    
        this.phoneNumber = phoneNumber;
    }


    
    public String getEmailAddress() {
    
        return emailAddress;
    }


    
    public void setEmailAddress( String emailAddress ) {
    
        this.emailAddress = emailAddress;
    }
    
    
    
    
}
