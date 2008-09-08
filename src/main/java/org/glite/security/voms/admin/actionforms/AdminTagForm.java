package org.glite.security.voms.admin.actionforms;

import org.apache.struts.validator.ValidatorActionForm;


public class AdminTagForm extends ValidatorActionForm {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    Long adminId;
    Long tagId;
    
    public Long getAdminId() {
    
        return adminId;
    }
    
    public void setAdminId( Long adminId ) {
    
        this.adminId = adminId;
    }
    
    public Long getTagId() {
    
        return tagId;
    }
    
    public void setTagId( Long tagId ) {
    
        this.tagId = tagId;
    }
    
    

    
}
