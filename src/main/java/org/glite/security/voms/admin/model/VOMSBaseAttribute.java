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
package org.glite.security.voms.admin.model;

import java.io.Serializable;

import org.glite.security.voms.service.attributes.AttributeClass;
import org.glite.security.voms.service.attributes.AttributeValue;


public abstract class VOMSBaseAttribute implements VomsAttributeValue,Serializable {

    VOMSAttributeDescription attributeDescription;
    String value;
    String context;
    
    public AttributeValue asAttributeValue() {

        AttributeClass aClass = getAttributeDescription().asAttributeClass();
        
        AttributeValue val = new AttributeValue();
        val.setAttributeClass( aClass );
        
        val.setContext( getContext() );
        
        val.setValue( getValue() );
        
        return val;
    }
     
    
    protected VOMSBaseAttribute() {

        
    }
    
    protected VOMSBaseAttribute(VOMSAttributeDescription desc, String value) {

        this.attributeDescription = desc;
        this.value = value;
    }


    
    public VOMSAttributeDescription getAttributeDescription() {
    
        return attributeDescription;
    }


    
    public void setAttributeDescription(
            VOMSAttributeDescription attributeDescription ) {
    
        this.attributeDescription = attributeDescription;
    }


    
    
    public void setContext( String context ) {
    
        this.context = context;
    }


    
    public String getValue() {
    
        return value;
    }


    
    public void setValue( String value ) {
    
        this.value = value;
    }
    
    public String getName() {

        return attributeDescription.getName();
    }

    public void setName( String name ) {

        attributeDescription.setName( name );
    }
}
