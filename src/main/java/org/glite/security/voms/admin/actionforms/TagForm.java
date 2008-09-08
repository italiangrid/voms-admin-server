package org.glite.security.voms.admin.actionforms;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorActionForm;


public class TagForm extends ValidatorActionForm {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    
    Long id;
    String name;
    
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
    
    @Override
    public void reset( ActionMapping mapping, HttpServletRequest request ) {
    
        id = null;
        name = null;
        
    }
    
}
