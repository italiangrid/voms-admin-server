package org.glite.security.voms.admin.actionforms.requests;

import org.apache.struts.validator.ValidatorActionForm;



public class VOMembershipForm extends ValidatorActionForm {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    String emailAddress;
    
    String name;
    String surname;
    
    String institute;
    String phoneNumber;
    String comments;
    
    boolean gridAUPAccepted;
    
    public VOMembershipForm() {

        // TODO Auto-generated constructor stub
    }

    
    /**
     * @return the name
     */
    public String getName() {
    
        return name;
    }

    
    /**
     * @return the surname
     */
    public String getSurname() {
    
        return surname;
    }

    
    /**
     * @return the institute
     */
    public String getInstitute() {
    
        return institute;
    }

    
    /**
     * @return the phoneNumber
     */
    public String getPhoneNumber() {
    
        return phoneNumber;
    }

    
    /**
     * @return the comments
     */
    public String getComments() {
    
        return comments;
    }

    
    /**
     * @return the gridAUPAccepted
     */
    public boolean isGridAUPAccepted() {
    
        return gridAUPAccepted;
    }

    
    /**
     * @param name the name to set
     */
    public void setName( String name ) {
    
        this.name = name;
    }

    
    /**
     * @param surname the surname to set
     */
    public void setSurname( String surname ) {
    
        this.surname = surname;
    }

    
    /**
     * @param institute the institute to set
     */
    public void setInstitute( String institute ) {
    
        this.institute = institute;
    }

    
    /**
     * @param phoneNumber the phoneNumber to set
     */
    public void setPhoneNumber( String phoneNumber ) {
    
        this.phoneNumber = phoneNumber;
    }

    
    /**
     * @param comments the comments to set
     */
    public void setComments( String comments ) {
    
        this.comments = comments;
    }

    
    /**
     * @param gridAUPAccepted the gridAUPAccepted to set
     */
    public void setGridAUPAccepted( boolean gridAUPAccepted ) {
    
        this.gridAUPAccepted = gridAUPAccepted;
    }


    
    /**
     * @return the emailAddress
     */
    public String getEmailAddress() {
    
        return emailAddress;
    }


    
    /**
     * @param emailAddress the emailAddress to set
     */
    public void setEmailAddress( String emailAddress ) {
    
        this.emailAddress = emailAddress;
    }

    
}
