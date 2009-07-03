package org.glite.security.voms.admin.dao.generic;

import java.net.URL;

import org.glite.security.voms.admin.model.AUP;
import org.glite.security.voms.admin.model.AUPVersion;


public interface AUPDAO extends NamedEntityDAO <AUP, Long>{

	
	public void setActiveVersion(AUP aup, String version);
	
    public void addVersion(AUP aup, String version, URL url);
    
    public void addVersion(AUP aup, String version, String text);
    
    public AUPVersion removeVersion(AUP aup, String version);
    
    public AUP getGridAUP();
    
    public AUP getVOAUP();
    
    public AUP createGridAUP(String description, String version, URL url);
    public AUP createGridAUP(String description, String version, String text);
    
    public AUP createVOAUP(String description, String version, URL url);
    public AUP createVOAUP(String description, String version, String text);
    
}
