package org.glite.security.voms.admin.dao;

import org.glite.security.voms.admin.model.AUP;
import org.glite.security.voms.admin.model.AUPVersion;


public interface AUPDAO extends GenericDAO <AUP, Long>{
    
    
    public AUP createAUP(String name, String description, String version, String url);
    
    public AUP findByName(String name);
    
    public void deleteAUPByName(String name);
    
    public void deleteAUPById(Long id);
    
    public void addVersion(String name, String version, String url);
    
    public void addVersion(AUP aup, String version, String url);
    
    public AUPVersion removeVersion(String name, String version);
    
    public AUPVersion removeVersion(AUP aup, String version);
    

}
