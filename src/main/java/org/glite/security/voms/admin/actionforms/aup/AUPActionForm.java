package org.glite.security.voms.admin.actionforms.aup;

import org.apache.struts.validator.ValidatorActionForm;


public class AUPActionForm extends ValidatorActionForm {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    Long id;
    
    String name;
    
    String description;
    
    String version;
    
    String url;
    
    
    public AUPActionForm() {

        // TODO Auto-generated constructor stub
    }

    
    /**
     * @return the id
     */
    public Long getId() {
    
        return id;
    }

    
    /**
     * @param id the id to set
     */
    public void setId( Long id ) {
    
        this.id = id;
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
     * @return the version
     */
    public String getVersion() {
    
        return version;
    }


    
    /**
     * @return the url
     */
    public String getUrl() {
    
        return url;
    }


    
    /**
     * @param version the version to set
     */
    public void setVersion( String version ) {
    
        this.version = version;
    }


    
    /**
     * @param url the url to set
     */
    public void setUrl( String url ) {
    
        this.url = url;
    }

    
    
}
