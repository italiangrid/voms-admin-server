package org.glite.security.voms.admin.model.personal_info;


public class PersonalInformationType {
    
    Long id;
    
    String typeName;
    String description;
    
    
    
    /**
     * @return the id
     */
    public Long getId() {
    
        return id;
    }
    
    /**
     * @return the typeName
     */
    public String getTypeName() {
    
        return typeName;
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
     * @param typeName the typeName to set
     */
    public void setTypeName( String typeName ) {
    
        this.typeName = typeName;
    }
    
    /**
     * @param description the description to set
     */
    public void setDescription( String description ) {
    
        this.description = description;
    }
    
    
    
}
