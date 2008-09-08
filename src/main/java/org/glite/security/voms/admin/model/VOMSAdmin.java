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
import java.util.Set;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.glite.security.voms.admin.common.Constants;
import org.glite.security.voms.admin.common.PathNamingScheme;
import org.glite.security.voms.admin.common.VOMSSyntaxException;
import org.glite.security.voms.admin.database.Auditable;


public class VOMSAdmin implements Serializable, Auditable, Cloneable {

    private static Log log = LogFactory.getLog( VOMSAdmin.class );

    /**
     * 
     */
    private static final long serialVersionUID = -5459874418491929253L;

    Long id;

    String dn;

    VOMSCA ca;

    String emailAddress;
    
    Set <VOMSAdmin> tags;
    
    public VOMSAdmin() {

    }

    /**
     * @return Returns the ca.
     */
    public VOMSCA getCa() {

        return ca;
    }

    /**
     * @param ca
     *            The ca to set.
     */
    public void setCa( VOMSCA ca ) {

        this.ca = ca;
    }

    /**
     * @return Returns the dn.
     */
    public String getDn() {

        return dn;
    }

    /**
     * @param dn
     *            The dn to set.
     */
    public void setDn( String dn ) {

        this.dn = dn;
    }

    /**
     * @return Returns the id.
     */
    public Long getId() {

        return id;
    }

    /**
     * @param id
     *            The id to set.
     */
    public void setId( Long id ) {

        this.id = id;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals( Object other ) {

        if ( this == other )
            return true;

        if ( !( other instanceof VOMSAdmin ) )
            return false;

        if ( other == null )
            return false;

        final VOMSAdmin that = (VOMSAdmin) other;

        return ( getDn().equals( that.getDn() ) );
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    public int hashCode() {

        return getDn().hashCode();
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#clone()
     */
    public Object clone() throws CloneNotSupportedException {

        VOMSAdmin newInstance = (VOMSAdmin) super.clone();
        newInstance.id = id;
        newInstance.dn = dn;
        newInstance.ca = ca;
        return newInstance;
    }

    /* Service methods */

    public String toString() {

    		ToStringBuilder builder = new ToStringBuilder(this);
    		
    		return builder.append(dn).append(ca.getSubjectString()).toString();
        
    }
    
    
    public String getEmailAddress() {
    
        return emailAddress;
    }

    
    public void setEmailAddress( String emailAddress ) {
    
        this.emailAddress = emailAddress;
    }

    public boolean isInternalAdmin(){
        return getCa().getSubjectString().startsWith( Constants.INTERNAL_DN_PREFIX );    
    }
    
    
    public boolean isGroupAdmin(){
        
        boolean result;
        try{
            
            result = PathNamingScheme.isGroup( getDn() );
            
        }catch (VOMSSyntaxException e) {
            
            return false;
        }
        
        return result ;
    }
    
    public boolean isRoleAdmin(){
        boolean result;
        
        try{
            
            result = PathNamingScheme.isQualifiedRole( getDn() );
            
        }catch (VOMSSyntaxException e) {
            
            return false;
        }
        return result;
    }

    public boolean isTagAdmin(){
     
        return getCa().getSubjectString().equals( Constants.TAG_CA );
        
    }

    
    public Set <VOMSAdmin> getTags() {
    
        return tags;
    }

    
    public void setTags( Set <VOMSAdmin> tags ) {
    
        this.tags = tags;
    }
    
    
}
