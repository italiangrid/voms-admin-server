package org.glite.security.voms.admin.database;


public class AlreadyConfirmedRequestException extends
        ConfirmationRequestException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public AlreadyConfirmedRequestException( String message ) {

        super( message );
        // TODO Auto-generated constructor stub
    }

    public AlreadyConfirmedRequestException( String message, Throwable t ) {

        super( message, t );
        // TODO Auto-generated constructor stub
    }

    public AlreadyConfirmedRequestException( Throwable t ) {

        super( t );
        // TODO Auto-generated constructor stub
    }

}
