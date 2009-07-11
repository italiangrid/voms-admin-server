package org.glite.security.voms.admin.model.request;

import javax.persistence.Entity;
import javax.persistence.Table;


@Entity
@Table(name="role_membership_req")
public class RoleMembershipRequest extends GroupMembershipRequest {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    String roleName;
    
    
    public RoleMembershipRequest() {

        // TODO Auto-generated constructor stub
    }


    
    /**
     * @return the roleName
     */
    public String getRoleName() {
    
        return roleName;
    }


    
    /**
     * @param roleName the roleName to set
     */
    public void setRoleName( String roleName ) {
    
        this.roleName = roleName;
    }
    
    @Override
    public String getTypeName() {
    	
    	return "Role membership request";
    }
}
