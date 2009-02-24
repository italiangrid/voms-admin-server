package org.glite.security.voms.admin.model.request;


public class VOMembershipRequest extends Request {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    
    String confirmId;
    
    public VOMembershipRequest() {

        // TODO Auto-generated constructor stub
    }

    
    /**
     * @return the confirmId
     */
    public String getConfirmId() {
    
        return confirmId;
    }

    
    /**
     * @param confirmId the confirmId to set
     */
    public void setConfirmId( String confirmId ) {
    
        this.confirmId = confirmId;
    }
    
    

}
