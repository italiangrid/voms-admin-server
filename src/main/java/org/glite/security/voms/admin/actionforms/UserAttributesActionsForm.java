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

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.struts.validator.ValidatorActionForm;

public class UserAttributesActionsForm extends ValidatorActionForm{

    Long userId;

    Long attributeId;

    String attributeName;

    String attributeDescription;

    String attributeValue;

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public UserAttributesActionsForm() {

        super();
        // TODO Auto-generated constructor stub
    }

    public Long getAttributeId() {

        return attributeId;
    }

    public void setAttributeId( Long attributeId ) {

        this.attributeId = attributeId;
    }

    public String getAttributeValue() {

        return attributeValue;
    }

    public void setAttributeValue( String attributeValue ) {

        this.attributeValue = attributeValue;
    }

    public Long getUserId() {

        return userId;
    }

    public void setUserId( Long userId ) {

        this.userId = userId;
    }

    public String getAttributeDescription() {

        return attributeDescription;
    }

    public void setAttributeDescription( String attributeDescription ) {

        this.attributeDescription = attributeDescription;
    }

    public String toString() {

        return ToStringBuilder.reflectionToString(this);

    }

    public String getAttributeName() {

        return attributeName;
    }

    public void setAttributeName( String attributeName ) {

        this.attributeName = attributeName;
    }

}
