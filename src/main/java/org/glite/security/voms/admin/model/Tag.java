package org.glite.security.voms.admin.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import org.glite.security.voms.admin.operations.VOMSPermission;


public class Tag implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    private Long id;
    
    private String name;
    
    private VOMSPermission permissions;
    
    private VOMSPermission permissionsOnPath;
    
    private Set <TagMapping> mappings = new HashSet <TagMapping>();
    
    public Tag() {

        
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


	public VOMSPermission getPermissions() {
		return permissions;
	}


	public void setPermissions(VOMSPermission permissions) {
		this.permissions = permissions;
	}


	public VOMSPermission getPermissionsOnPath() {
		return permissionsOnPath;
	}


	public void setPermissionsOnPath(VOMSPermission permissionsOnPath) {
		this.permissionsOnPath = permissionsOnPath;
	}


    
    /**
     * @return the mappings
     */
    public Set <TagMapping> getMappings() {
    
        return mappings;
    }


    
    /**
     * @param mappings the mappings to set
     */
    public void setMappings( Set <TagMapping> mappings ) {
    
        this.mappings = mappings;
    }
    
	
    
}
