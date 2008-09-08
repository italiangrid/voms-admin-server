package org.glite.security.voms.admin.actionforms;

import java.util.Date;

import org.apache.struts.action.ActionForm;
import org.apache.struts.upload.FormFile;


public class CAForm extends ActionForm {
    
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    
    Long id;
    String subjectString;      
    String certificateType;    
    FormFile certificateFile;

    
    public FormFile getCertificateFile() {
    
        return certificateFile;
    }

    
    public void setCertificateFile( FormFile certificateFile ) {
    
        this.certificateFile = certificateFile;
    }

    
    public String getCertificateType() {
    
        return certificateType;
    }

    
    public void setCertificateType( String certificateType ) {
    
        this.certificateType = certificateType;
    }

    
    public Long getId() {
    
        return id;
    }

    
    public void setId( Long id ) {
    
        this.id = id;
    }

    
    public String getSubjectString() {
    
        return subjectString;
    }

    
    public void setSubjectString( String subjectString ) {
    
        this.subjectString = subjectString;
    }
    
    
    

}
