package org.glite.security.voms.admin.dao.hibernate;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.glite.security.voms.admin.dao.AUPDAO;
import org.glite.security.voms.admin.dao.AUPVersionDAO;
import org.glite.security.voms.admin.dao.DAOFactory;
import org.glite.security.voms.admin.model.AUPVersion;

/**
 * Returns Hibernate-specific instances of DAOs.
 * <p/>
 * If for a particular DAO there is no additional non-CRUD functionality, we use
 * a nested static class to implement the interface in a generic way. This allows clean
 * refactoring later on, should the interface implement business data access
 * methods at some later time. Then, we would externalize the implementation into
 * its own first-level class.
 *
 * @author Christian Bauer
 * @author Andrea Ceccanti
 */
public class HibernateDAOFactory extends DAOFactory {

    private static Log log = LogFactory.getLog(HibernateDAOFactory.class);

    private GenericHibernateDAO instantiateDAO(Class daoClass) {
        try {
            log.debug("Instantiating DAO: " + daoClass);
            return (GenericHibernateDAO)daoClass.newInstance();
        } catch (Exception ex) {
            throw new RuntimeException("Can not instantiate DAO: " + daoClass, ex);
        }
    }

    @Override
    public AUPDAO getAUPDAO() {
        return (AUPDAO)instantiateDAO( AUPDAOHibernate.class);
    }

    
    @Override
    public AUPVersionDAO getAUPVersionDAO() {
        // TODO Auto-generated method stub
        return (AUPVersionDAO)instantiateDAO( AUPVersionDAOHibernate.class );
    }
    
    // Inline concrete DAO implementations with no business-related data access methods.
    // If we use public static nested classes, we can centralize all of them in one source file.
    public static class AUPVersionDAOHibernate 
        extends GenericHibernateDAO <AUPVersion, Long>
        implements AUPVersionDAO{};
        
        
    

}
