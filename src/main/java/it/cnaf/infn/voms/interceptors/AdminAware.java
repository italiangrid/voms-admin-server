package it.cnaf.infn.voms.interceptors;

import java.io.Serializable;

import org.glite.security.voms.admin.operations.CurrentAdmin;


public interface AdminAware extends Serializable {
    
    
    public void setCurrentAdmin(CurrentAdmin admin);
    public CurrentAdmin getCurrentAdmin();
    

}
