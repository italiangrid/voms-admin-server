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

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;
import org.apache.struts.validator.ValidatorActionForm;

public class UserForm extends ValidatorActionForm{

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    Long id;

    String dn;
    String ca;

    String cn;

    String crlURI;

    String emailAddress;
    
    String name;
    String surname;
    String institution;
    String phoneNumber;
    String address;

    String certificateType;
    
    FormFile certificateFile;
    
        
    public String getCertificateType() {
		return certificateType;
	}

	public void setCertificateType(String certificateType) {
		this.certificateType = certificateType;
	}

	public FormFile getCertificateFile() {
		return certificateFile;
	}

	public void setCertificateFile(FormFile certificateFile) {
		this.certificateFile = certificateFile;
	}

	public String getCrlURI() {

        return crlURI;
    }

    public void setCrlURI( String crlURI ) {

        this.crlURI = crlURI;
    }

    public String getEmailAddress() {

        return emailAddress;
    }

    public void setEmailAddress( String emailAddress ) {

        this.emailAddress = emailAddress;
    }

    public String getCa() {

        return ca;
    }

    public void setCa( String ca ) {

        this.ca = ca;
    }

    public String getCn() {

        return cn;
    }

    public void setCn( String cn ) {

        this.cn = cn;
    }

    public String getDn() {

        return dn;
    }

    public void setDn( String dn ) {

        this.dn = dn;
    }

    public Long getId() {

        return id;
    }

    public void setId( Long id ) {

        this.id = id;
    }
    
    public String getAddress() {
    
        return address;
    }

    
    public void setAddress( String address ) {
    
        this.address = address;
    }

    
    public String getInstitution() {
    
        return institution;
    }

    
    public void setInstitution( String institution ) {
    
        this.institution = institution;
    }

    
    public String getName() {
    
        return name;
    }

    
    public void setName( String name ) {
    
        this.name = name;
    }

    
    public String getPhoneNumber() {
    
        return phoneNumber;
    }

    
    public void setPhoneNumber( String phoneNumber ) {
    
        this.phoneNumber = phoneNumber;
    }

    
    public String getSurname() {
    
        return surname;
    }

    
    public void setSurname( String surname ) {
    
        this.surname = surname;
    }

    public String toString() {

        return ToStringBuilder.reflectionToString(this);
        
    }
    
    public void reset( ActionMapping mapping, HttpServletRequest request ) {
    
        id = null;
        dn = null;
        ca = null;
        
    }

}
