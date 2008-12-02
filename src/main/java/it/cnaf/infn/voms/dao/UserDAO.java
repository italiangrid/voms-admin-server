package it.cnaf.infn.voms.dao;

import java.util.List;

import org.glite.security.voms.admin.common.UnimplementedFeatureException;
import org.glite.security.voms.admin.model.VOMSUser;


public class UserDAO extends BaseGenericDAO<VOMSUser, Long> implements UserDAOIF {

    
    private UserDAO() {
        super();
        getSession();

    }
    
    public static UserDAO instance() {

        return new UserDAO();
    }
    
    
    public VOMSUser findByCertificate( String subject ) {

        throw new UnimplementedFeatureException( "findByCertificate(...)" );
        
    }

    public VOMSUser findByCertificate( String subject, String issuerSubject ) {

        throw new UnimplementedFeatureException( "findByCertificate(...)" );
        
    }

    public VOMSUser findByEmailAddress( String emailAddress ) {

        VOMSUser u = new VOMSUser();
        u.setEmailAddress( emailAddress );
        
        List <VOMSUser> users = findByExample( u );
        
        if (users.isEmpty())
            return null;
        else 
            return users.get( 0 );
    }

    

}
