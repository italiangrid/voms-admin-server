package org.glite.security.voms.admin.test;

import java.io.IOException;
import java.util.Date;
import java.util.Properties;
import java.util.Timer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.PropertyConfigurator;
import org.glite.security.voms.admin.common.VOMSConfiguration;
import org.glite.security.voms.admin.common.VOMSException;
import org.glite.security.voms.admin.common.tasks.DatabaseSetupTask;
import org.glite.security.voms.admin.common.tasks.UpdateCATask;
import org.glite.security.voms.admin.dao.AUPDAO;
import org.glite.security.voms.admin.dao.DAOFactory;
import org.glite.security.voms.admin.dao.VOMSUserDAO;
import org.glite.security.voms.admin.database.HibernateFactory;
import org.glite.security.voms.admin.model.AUP;
import org.glite.security.voms.admin.model.AUPVersion;
import org.glite.security.voms.admin.model.VOMSUser;
import org.hibernate.cfg.Configuration;


public class TestAUP {
    
    private static final Log log = LogFactory.getLog( TestAUP.class );
    private static final String vo = "test_vo_mysql";
    
    private static final Timer t = new Timer();
    
    private static final String myDn = "/C=IT/O=INFN/OU=Personal Certificate/L=CNAF/CN=Andrea Ceccanti";
    private static final String myCA = "/C=IT/O=INFN/CN=INFN CA";
    private static final String myEmail = "andrea.ceccanti@cnaf.infn.it";
    
    
    private Configuration loadHibernateConfiguration() {

        Properties dbProperties = new Properties();

        try {
            
            dbProperties.load( Object.class.getResourceAsStream( "/test/mysql.hibernate.properties" ) );
            log.debug( "db props: "+dbProperties );
        
        } catch ( IOException e ) {
            
            log.error("Error loading db properties!", e);
            throw new VOMSException("Error loading db properties!", e);
        }
        
        return new Configuration().addProperties( dbProperties ).configure();
    }
    
    private void setupVOMSConfiguration(){
        log.debug("Setting up voms configuration...");
        System.setProperty( VOMSConfiguration.VO_NAME, vo );
        System.setProperty( "GLITE_LOCATION_VAR", "./resources/debug/conf" );
        VOMSConfiguration.instance( false, null );  
        log.debug("Setting up voms configuration done!");
        
    }
    private void setupDb(){
        
        // Configuration conf = loadHibernateConfiguration();
        UpdateCATask caTask = UpdateCATask.instance( t );
        caTask.run();

        DatabaseSetupTask task = DatabaseSetupTask.instance();
        task.run();
        
        
    }
    
    private void createAndreaUser(){
        
        VOMSUserDAO userDAO = VOMSUserDAO.instance();
        
        VOMSUser u = userDAO.findByEmail( myEmail );
        
        userDAO.addCertificate( u, myDn, myCA );
        
        
    }
    
    private void createAUP(){
        
        DAOFactory daoFactory = DAOFactory.instance( DAOFactory.HIBERNATE );
        
        AUPDAO aupDAO = daoFactory.getAUPDAO();
        
        aupDAO.createAUP( "test", null, "1.0", "myURL" );
        HibernateFactory.commitTransaction();
        
        
    }
    public TestAUP() {

        PropertyConfigurator.configure( Object.class.getResource( "/test/log4j.properties" ) );        
        log.info("Starting up!");
        setupVOMSConfiguration();
        setupDb();
        
        // Do stuff here
        DAOFactory daoFactory = DAOFactory.instance( DAOFactory.HIBERNATE );
        VOMSUserDAO userDAO = VOMSUserDAO.instance();
       
        AUP aup = daoFactory.getAUPDAO().findByName( "test" );
        
        VOMSUser u = userDAO.findByEmail( myEmail );
        
        if (u != null){
            log.debug( "user aups: "+u.getAupAcceptanceRecords() );
            userDAO.acceptAUP( u, aup );
        }
        
        HibernateFactory.commitTransaction();
        
        
        // Shut down threads
        t.cancel();
    }
    
    public static void main( String[] args ) {
        try{
            new TestAUP();
        }catch (Throwable t) {
            log.error(t.getMessage(),t);
            TestAUP.t.cancel();
            System.exit(1);
        }
    }

}
