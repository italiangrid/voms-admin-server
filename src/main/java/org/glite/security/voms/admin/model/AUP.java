package org.glite.security.voms.admin.model;

import java.io.Serializable;

import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;




public class AUP implements Serializable{

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    private static final Log log = LogFactory.getLog( AUP.class );
    
    Long id;
    String name;
    String description;
    
    SortedSet <AUPVersion> versions = new TreeSet <AUPVersion>();
    
    
    public AUP() {

        // TODO Auto-generated constructor stub
    }


    @Override
    public boolean equals( Object other) {
        
        if (this == other)
            return true;
        
        if ( !( other instanceof AUP ) )
            return false;

        
        AUP that = (AUP) other;
        
        if (that == null)
            return false;
        
        return (that.getName().equals( getName() ));
    }
    
    @Override
    public int hashCode() {
        
        if (this.getId()!= null)
            return getId().hashCode();
        
        return super.hashCode();
    }
        
    
    // Getters and setters
    /**
     * @return the id
     */
    public Long getId() {
    
        return id;
    }


    
    /**
     * @return the name
     */
    public String getName() {
    
        return name;
    }


    
    /**
     * @return the description
     */
    public String getDescription() {
    
        return description;
    }


    
    /**
     * @param id the id to set
     */
    public void setId( Long id ) {
    
        this.id = id;
    }


    
    /**
     * @param name the name to set
     */
    public void setName( String name ) {
    
        this.name = name;
    }


    
    /**
     * @param description the description to set
     */
    public void setDescription( String description ) {
    
        this.description = description;
    }


    
    /**
     * @return the versions
     */
    public Set <AUPVersion> getVersions() {
    
        return versions;
    }


    
    /**
     * @param versions the versions to set
     */
    public void setVersions( SortedSet <AUPVersion> versions ) {
    
        this.versions = versions;
    }
    
    public AUPVersion getVersion(String versionNumber){
        
        if (versions.isEmpty())
            return null;
        
        for (AUPVersion v: versions)   
            if (v.getVersion().equals( versionNumber ))
                return v;
        
        
        return null;
    }
    
    public AUPVersion getCurrentVersion(){
        
        if (versions.isEmpty())
            return null;
        
        return versions.last();
        
    }
    
    public AUPVersion getOldestVersion(){
        
        if (versions.isEmpty())
            return null;
        
        return versions.first();
    }
    
    
    @Override
    public String toString() {
        return String.format( "[id:%d, name:%s, desc:%s, versions:%s]", id, name, description, versions);
        
    }
    
}
