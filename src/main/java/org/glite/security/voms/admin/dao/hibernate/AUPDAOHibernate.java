package org.glite.security.voms.admin.dao.hibernate;

import java.util.Date;

import org.glite.security.voms.admin.common.NullArgumentException;
import org.glite.security.voms.admin.dao.generic.AUPDAO;
import org.glite.security.voms.admin.database.AlreadyExistsException;
import org.glite.security.voms.admin.database.NoSuchAUPException;
import org.glite.security.voms.admin.database.NoSuchAUPVersionException;
import org.glite.security.voms.admin.model.AUP;
import org.glite.security.voms.admin.model.AUPVersion;


public class AUPDAOHibernate extends NamedEntityHibernateDAO <AUP, Long> implements AUPDAO {

    public AUP createAUP( String name, String description, String version,
            String url ) {

        if ( name == null )
            throw new NullArgumentException( "name cannot be null!" );
        
        if ( version == null )
            throw new NullArgumentException( "version cannot be null!" );
        
        if ( url == null )
            throw new NullArgumentException( "url cannot be null!" );
        
        AUP aup = new AUP();
        aup.setName( name );
        aup.setDescription( description );
        
        if (findByName(name) != null)
            throw new AlreadyExistsException("AUP named '"+name+"' already exists!");
        
        aup.setDescription( description );
        
        AUPVersion firstVersion = new AUPVersion();
        
        firstVersion.setCreationTime( new Date() );
        
        firstVersion.setUrl( url );
        firstVersion.setVersion( version );
        firstVersion.setAup( aup );
        
        aup.getVersions().add( firstVersion );
        
        makePersistent( aup );
        
        return aup;
        
    }

    public void addVersion( String name, String version, String url ) {

        if ( name == null )
            throw new NullArgumentException( "name cannot be null!" );
        
        if ( version == null )
            throw new NullArgumentException( "version cannot be null!" );
        
        if ( url == null )
            throw new NullArgumentException( "url cannot be null!" );
        
        AUP aup = findByName( name );
        
        if (aup == null)
            throw new NoSuchAUPException("No AUP found for name '"+name+"'.");
        
        addVersion( aup, version, url );
        
    }

    public void addVersion( AUP aup, String version, String url ) {

        if ( aup == null )
            throw new NullArgumentException( "aup cannot be null!" );
        
        AUPVersion aupVersion = aup.getVersion( version );
        
        if (aupVersion != null)
            throw new AlreadyExistsException("Version '"+version+"' already exists for AUP named '"+aup.getName()+"'.");
        
        aupVersion = new AUPVersion();
        aupVersion.setVersion( version );
        aupVersion.setUrl( url );
        aupVersion.setCreationTime( new Date() );
        aupVersion.setAup( aup );
        aup.getVersions().add( aupVersion );
        
    }

    public AUPVersion removeVersion( String name, String version ) {

        if ( name == null )
            throw new NullArgumentException( "name cannot be null!" );
        
        AUP aup = findByName( name );
        
        if (aup == null)
            throw new NoSuchAUPException("No AUP found for name '"+name+"'.");
        
        return removeVersion( aup, version );
        
    }

    public AUPVersion removeVersion( AUP aup, String version ) {

        if ( aup == null )
            throw new NullArgumentException( "aup cannot be null!" );
        
        AUPVersion aupVersion = aup.getVersion( version );
        
        if (aupVersion == null)
            throw new NoSuchAUPVersionException("No AUP version found for version '"+version+"'.");
                    
        aup.getVersions().remove( aupVersion );
        return aupVersion;
    }

	    
}
