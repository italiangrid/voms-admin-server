package org.glite.security.voms.admin.database;

import org.glite.security.voms.admin.common.NotFoundException;


public class NoSuchTagException extends NotFoundException {

    public NoSuchTagException( String message ) {

        super( message );
    }

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

}
