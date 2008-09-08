package org.glite.security.voms.admin.tools;

import java.util.List;


public class SigningPolicyDescriptor {
    
    private String signingPolicyFileName;
    private String caHash;
    
    private String accessIdCA;
    private String posRights;
    private List<String> conditionalSubjects;
    
    public String getAccessIdCA() {
    
        return accessIdCA;
    }
    
    public void setAccessIdCA( String accessIdCA ) {
    
        this.accessIdCA = accessIdCA;
    }
    
    public String getPosRights() {
    
        return posRights;
    }
    
    public void setPosRights( String posRights ) {
    
        this.posRights = posRights;
    }
    
    public List <String> getConditionalSubjects() {
    
        return conditionalSubjects;
    }
    
    public void setConditionalSubjects( List <String> conditionalSubjects ) {
    
        this.conditionalSubjects = conditionalSubjects;
    }
    
}
