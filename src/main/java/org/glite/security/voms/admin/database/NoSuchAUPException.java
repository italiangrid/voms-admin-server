package org.glite.security.voms.admin.database;

import org.glite.security.voms.admin.common.NotFoundException;


public class NoSuchAUPException extends NotFoundException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public NoSuchAUPException( String message ) {

        super( message );
        // TODO Auto-generated constructor stub
    }

    public NoSuchAUPException( String message, Throwable t ) {

        super( message, t );
        // TODO Auto-generated constructor stub
    }

    public NoSuchAUPException( Throwable t ) {

        super( t );
        // TODO Auto-generated constructor stub
    }

}
