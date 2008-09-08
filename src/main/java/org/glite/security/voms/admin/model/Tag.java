package org.glite.security.voms.admin.model;

import java.io.Serializable;
import java.util.Set;


public class Tag implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    
    private Long id;
    
    private String name;
    
    private String description;
    
    private Set <VOMSAdmin> admins;
    
    private Boolean implicit;
    
    public Tag() {

        
    }

    
    public String getDescription() {
    
        return description;
    }


    
    public void setDescription( String description ) {
    
        this.description = description;
    }


    
    public Long getId() {
    
        return id;
    }


    
    public void setId( Long id ) {
    
        this.id = id;
    }


    
    public String getName() {
    
        return name;
    }


    
    public void setName( String name ) {
    
        this.name = name;
    }


    
    public Set <VOMSAdmin> getAdmins() {
    
        return admins;
    }


    
    public void setAdmins( Set <VOMSAdmin> admins ) {
    
        this.admins = admins;
    }


    
    public Boolean getImplicit() {
    
        return implicit;
    }


    
    public void setImplicit( Boolean implicit ) {
    
        this.implicit = implicit;
    }


    public boolean isImplicit(){
        
        return getImplicit();
    }
    
    
    

}
