package org.glite.security.voms.admin.dao.hibernate;

import java.io.Serializable;
import java.util.List;

import org.glite.security.voms.admin.common.NotFoundException;
import org.glite.security.voms.admin.common.NullArgumentException;
import org.glite.security.voms.admin.dao.generic.NamedEntityDAO;
import org.hibernate.criterion.Restrictions;


public class NamedEntityHibernateDAO<T, ID extends Serializable> extends GenericHibernateDAO<T,ID>
    implements NamedEntityDAO <T, ID>
{

    public T findByName(String name){
        
        if ( name == null )
            throw new NullArgumentException(
                    "Please provide a value for the 'name' argument! null is not a valid value in this context." );
        
        if ( name == null )
            throw new NullArgumentException( "name cannot be null!" );
        
        List <T> retVal = findByCriteria( Restrictions.eq( "name", name ) );
        
        if (retVal.isEmpty())
            return null;
        
        return retVal.get( 0 );
        
    }

    public T deleteByName( String name ) {
        
        if ( name == null )
            throw new NullArgumentException(
                    "Please provide a value for the 'name' argument! null is not a valid value in this context." );
        
        T instance = findByName( name );
        if (instance == null)
            throw new NotFoundException("No '"+ instance.getClass().getSimpleName()+"' instance found for name '"+name+"'.");
        
        makeTransient( instance );
        
        return instance;
        
    }

    
}
