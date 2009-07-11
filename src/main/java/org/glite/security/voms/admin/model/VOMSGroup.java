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
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.glite.security.voms.admin.database.Auditable;
import org.glite.security.voms.admin.database.NoSuchAttributeException;


/**
 * 
 * 
 * @author andrea
 * 
 */
public class VOMSGroup implements Serializable, Auditable, Comparable {

    public static final Log log = LogFactory.getLog( VOMSGroup.class );

    private static final long serialVersionUID = -4693441755811017977L;

    public VOMSGroup() {

        must = new Boolean( true );
    }

    Long id;

    String name;

    VOMSGroup parent;

    Boolean must;

    Set attributes = new HashSet();

    Set mappings = new TreeSet();

    Set acls = new HashSet();
    
    Set<TagMapping> tagMappings = new HashSet <TagMapping> ();
    
    Boolean restricted;
    
    String description;

    public String getName() {

        return name;
    }

    public void setName( String name ) {

        this.name = name;
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

    /**
     * @return Returns the must.
     */
    public Boolean getMust() {

        return must;
    }

    /**
     * @param must
     *            The must to set.
     */
    public void setMust( Boolean must ) {

        this.must = must;
    }

    /**
     * @return Returns the parent.
     */
    public VOMSGroup getParent() {

        return parent;
    }

    /**
     * @param parent
     *            The parent to set.
     */
    public void setParent( VOMSGroup parent ) {

        this.parent = parent;
    }

    public Set getAttributes() {

        return attributes;
    }

    public void setAttributes( Set attributes ) {

        this.attributes = attributes;
    }

    public boolean isRootGroup() {

        return this.equals( parent );
    }

    public void addAttribute( VOMSGroupAttribute val ) {
        attributes.add( val );
    }

    public VOMSGroupAttribute getAttributeByName( String name ) {

        Iterator i = attributes.iterator();

        while ( i.hasNext() ) {
            VOMSGroupAttribute tmp = (VOMSGroupAttribute) i.next();

            if ( tmp.getName().equals( name ) )
                return tmp;
        }

        return null;

    }

    public void deleteAttribute( VOMSGroupAttribute val ) {

        if ( !attributes.contains( val ) )
            throw new NoSuchAttributeException( "Attribute \"" + val.getName()
                    + "\" undefined for group" + this );

        attributes.remove( val );
    }

    public void deleteAttributeByName( String attrName ) {

        deleteAttribute( getAttributeByName( attrName ) );

    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals( Object other ) {

        if ( this == other )
            return true;

        if ( !( other instanceof VOMSGroup ) )
            return false;

        VOMSGroup that = (VOMSGroup) other;

        if ( that == null )
            return false;

        return ( getName().equals( that.getName() ) );

    }

    public boolean hasMember( VOMSUser u ) {

        VOMSMapping m = new VOMSMapping( u, this, null );
        return mappings.contains( m );

    }

    public Set getMembers() {

        SortedSet res = new TreeSet();

        Iterator mIter = mappings.iterator();
        while ( mIter.hasNext() ) {
            VOMSMapping m = (VOMSMapping) mIter.next();
            if ( m.isGroupMapping() )
                res.add( m.getUser() );
        }

        return Collections.unmodifiableSortedSet( res );

    }
    
    public Set getMembersEmailAddresses(){
        
        SortedSet res = new TreeSet();

        Iterator mIter = mappings.iterator();
        while ( mIter.hasNext() ) {
            VOMSMapping m = (VOMSMapping) mIter.next();
            if ( m.isGroupMapping() )
                res.add( m.getUser().getEmailAddress() );
        }

        return Collections.unmodifiableSortedSet( res );
        
    }

    public Set getMappings() {

        return mappings;
    }

    public void setMappings( Set mappings ) {

        this.mappings = mappings;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    public int hashCode() {
            
        return getName().hashCode();
    }

    public String toString() {

        return getName();
    }

    public int compareTo( Object o ) {

        if ( this.equals( o ) )
            return 0;

        VOMSGroup that = (VOMSGroup) o;

        if ( that == null )
            return 1;

        return this.getName().compareTo( that.getName() );
    }

    public Set getAcls() {

        return acls;
    }

    public void setAcls( Set acls ) {

        this.acls = acls;
    }

    protected ACL getACL( boolean defaultACL ) {

        if ( getAcls().isEmpty() )
            return null;

        Iterator i = getAcls().iterator();
        while ( i.hasNext() ) {
            ACL a = (ACL) i.next();
            if ( a.getDefaultACL().booleanValue() == defaultACL && a.getContext().isGroupContext())
                return a;
        }

        return null;
    }

    public ACL getACL() {

        return getACL( false );
    }

    public ACL getDefaultACL() {

        return getACL( true );
    }

    public void importACL( ACL acl ) {

        ACL importedACL = new ACL( this, false );

        importedACL.getPermissions().putAll( acl.getPermissions() );

        getAcls().add( importedACL );

    }

    
    /**
     * @return the tagMappings
     */
    public Set <TagMapping> getTagMappings() {
    
        return tagMappings;
    }

    
    /**
     * @param tagMappings the tagMappings to set
     */
    public void setTagMappings( Set <TagMapping> tagMappings ) {
    
        this.tagMappings = tagMappings;
    }

	public Boolean getRestricted() {
		return restricted;
	}

	public void setRestricted(Boolean restricted) {
		this.restricted = restricted;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
    
}
