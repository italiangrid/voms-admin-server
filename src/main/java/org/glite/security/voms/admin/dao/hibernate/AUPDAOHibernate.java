package org.glite.security.voms.admin.dao.hibernate;

import java.net.URL;
import java.util.Date;

import org.glite.security.voms.admin.common.NullArgumentException;
import org.glite.security.voms.admin.common.VOMSException;
import org.glite.security.voms.admin.dao.generic.AUPDAO;
import org.glite.security.voms.admin.database.AlreadyExistsException;
import org.glite.security.voms.admin.database.NoSuchAUPVersionException;
import org.glite.security.voms.admin.model.AUP;
import org.glite.security.voms.admin.model.AUPVersion;


public class AUPDAOHibernate extends NamedEntityHibernateDAO <AUP, Long> implements AUPDAO {

	
	protected AUP newAUP(String name, String description){
		
		if ( name == null )
            throw new NullArgumentException( "name cannot be null!" );
		
		if (findByName(name) != null)
            throw new AlreadyExistsException("AUP named '"+name+"' already exists!");
		
		AUP aup = new AUP();
        aup.setName( name );
        aup.setDescription( description );
        
        // Default reacceptance period is 365 days
        aup.setReacceptancePeriod(365);
        
        return aup;
		
	}
    
	
	protected AUP createAUP(String name, String description, String version,
			String text) {
		
		if ( version == null )
            throw new NullArgumentException( "version cannot be null!" );
		
		if (text == null)
			throw new NullArgumentException( "text cannot be null!" );
		
		AUP aup = newAUP(name, description);
		AUPVersion firstVersion = newAUPVersion(aup, version);
		
		firstVersion.setText(text);
		firstVersion.setActive(true);
		aup.getVersions().add( firstVersion );
        
        makePersistent( aup );
    
        return aup;
		
	}
	
	protected AUP createAUP( String name, String description, String version,
            URL url ) {
        
        if ( version == null )
            throw new NullArgumentException( "version cannot be null!" );
        
        if ( url == null )
            throw new NullArgumentException( "url cannot be null!" );
        
        AUP aup = newAUP(name, description);
        
        AUPVersion firstVersion = newAUPVersion(aup, version);
        firstVersion.setUrl(url.toString());
        firstVersion.setActive(true);
        aup.getVersions().add( firstVersion );
        
        makePersistent( aup );
    
        return aup;
        
    }


    
    protected AUPVersion newAUPVersion(AUP aup, String version){
    	
    	if ( aup == null )
            throw new NullArgumentException( "aup cannot be null!" );
    	
    	AUPVersion aupVersion = aup.getVersion( version );
        
        if (aupVersion != null)
            throw new AlreadyExistsException("Version '"+version+"' already exists for AUP named '"+aup.getName()+"'.");
        
    	if (version == null)
    		throw new NullArgumentException("version cannot be null!");
    	
    	aupVersion = new AUPVersion();
    	
    	aupVersion.setVersion(version);
    	aupVersion.setCreationTime( new Date() );
        aupVersion.setAup( aup );
        
        return aupVersion;
    	
    	
    }
    public void addVersion(AUP aup, String version, URL url) {
	
    	AUPVersion v = newAUPVersion(aup, version);
    	
    	v.setUrl(url.toString());
        aup.getVersions().add( v ); 
        
        
        
	}
    
    public void addVersion( AUP aup, String version, String text ) {

        AUPVersion v = newAUPVersion(aup, version);
        v.setText(text);
        aup.getVersions().add( v );
    }

    public AUPVersion removeVersion( AUP aup, String version ) {

        if ( aup == null )
            throw new NullArgumentException( "aup cannot be null!" );
        
        AUPVersion aupVersion = aup.getVersion( version );
        
        if (aupVersion == null)
            throw new NoSuchAUPVersionException("No AUP version found for version '"+version+"'.");
        
        if (aupVersion.getActive())
        	throw new VOMSException("The currently active aup version cannot be removed!");
                    
        aup.getVersions().remove( aupVersion );
        return aupVersion;
    }


	public AUP getGridAUP() {
		return findByName(AUP.GRID_AUP_NAME);
	}


	public AUP getVOAUP() {
		return findByName(AUP.VO_AUP_NAME);
	}


	public AUP createGridAUP(String description, String version, URL url) {
		
		return createAUP(AUP.GRID_AUP_NAME, description, version, url);
	}


	public AUP createGridAUP(String description, String version, String text) {
		
		return createAUP(AUP.GRID_AUP_NAME, description, version, text);
	}


	public AUP createVOAUP(String description, String version, URL url) {
		
		return createAUP(AUP.VO_AUP_NAME, description, version, url);
	}


	public AUP createVOAUP(String description, String version, String text) {
		
		return createAUP(AUP.VO_AUP_NAME, description, version, text);
	}


	public void setActiveVersion(AUP aup, String version) {
		
		if (aup == null)
			throw new NullArgumentException("aup cannot be null!");
		
		if (version == null)
			throw new NullArgumentException("version cannot be null!");
		
		AUPVersion v = aup.getVersion(version);
		
		if (v == null)
			throw new NoSuchAUPVersionException("Aup version '"+version+"' not defined for AUP '"+aup.getName()+"'.");
		
		aup.setActiveVersion(v);
	}
    
    
	    
}
