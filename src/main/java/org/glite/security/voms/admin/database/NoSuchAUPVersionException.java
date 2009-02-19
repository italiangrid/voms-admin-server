package org.glite.security.voms.admin.database;

import org.glite.security.voms.admin.common.NotFoundException;


public class NoSuchAUPVersionException extends NotFoundException {

    public NoSuchAUPVersionException( String message ) {

        super( message );
        // TODO Auto-generated constructor stub
    }

    public NoSuchAUPVersionException( String message, Throwable t ) {

        super( message, t );
        // TODO Auto-generated constructor stub
    }

    public NoSuchAUPVersionException( Throwable t ) {

        super( t );
        // TODO Auto-generated constructor stub
    }

}
