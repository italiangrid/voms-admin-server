package org.glite.security.voms.admin.test;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.PropertyConfigurator;
import org.glite.security.voms.admin.common.VOMSConfiguration;
import org.glite.security.voms.admin.common.tasks.DatabaseSetupTask;
import org.glite.security.voms.admin.common.tasks.UpdateCATask;
import org.glite.security.voms.admin.dao.VOMSAdminDAO;
import org.glite.security.voms.admin.dao.VOMSUserDAO;
import org.glite.security.voms.admin.dao.generic.AUPDAO;
import org.glite.security.voms.admin.dao.generic.DAOFactory;
import org.glite.security.voms.admin.dao.generic.TagDAO;
import org.glite.security.voms.admin.dao.generic.TaskDAO;
import org.glite.security.voms.admin.database.HibernateFactory;
import org.glite.security.voms.admin.model.AUP;
import org.glite.security.voms.admin.model.Tag;
import org.glite.security.voms.admin.model.VOMSAdmin;
import org.glite.security.voms.admin.model.VOMSUser;
import org.glite.security.voms.admin.model.task.SignAUPTask;
import org.glite.security.voms.admin.model.task.Task;
import org.glite.security.voms.admin.operations.VOMSPermission;



public class TestAUP {
    
    private static final Log log = LogFactory.getLog( TestAUP.class );
    private static final String vo = "test_vo_mysql";
    
    private static final Timer t = new Timer();
    
    private static final String myDn = "/C=IT/O=INFN/OU=Personal Certificate/L=CNAF/CN=Andrea Ceccanti";
    private static final String myCA = "/C=IT/O=INFN/CN=INFN CA";
    private static final String myEmail = "andrea.ceccanti@cnaf.infn.it";
    
    
    
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
        
        if (u == null){
            
            u = new VOMSUser();
            
            u.setName( "Andrea" );
            u.setSurname( "Ceccanti" );
            u.setAddress( "" );
            u.setInstitution( "" );
            u.setPhoneNumber( "" );
            u.setEmailAddress( myEmail );
            
            userDAO.create( u );
            userDAO.addCertificate( u, myDn, myCA );
        }
        
        HibernateFactory.commitTransaction();
        
        
        
    }
    
    private void createAUP(){
        
        DAOFactory daoFactory = DAOFactory.instance( DAOFactory.HIBERNATE );
        
        AUPDAO aupDAO = daoFactory.getAUPDAO();
        
        aupDAO.createAUP( "test", null, "1.0", "myURL" );
        HibernateFactory.commitTransaction();
        
        
    }
    
    
    public void testTagDAO(){
        
        DAOFactory daoFac = DAOFactory.instance( DAOFactory.HIBERNATE );
        
        TagDAO tDao = daoFac.getTagDAO();
        
        Tag t = tDao.createTag( "TEST", 
                VOMSPermission.getAllPermissions(), 
                null, false );
        
        VOMSAdmin a = VOMSAdminDAO.instance().create( myDn, myCA, myEmail );
        
        VOMSAdminDAO.instance().assignTagInAllGroups( a, t );
        HibernateFactory.commitTransaction();
        
        
    }
    
    
    public void testSignAUPTask(){
        
        DAOFactory daoFac = DAOFactory.instance( DAOFactory.HIBERNATE );
        
        AUPDAO aupDAO = daoFac.getAUPDAO();
        VOMSUserDAO userDAO = VOMSUserDAO.instance();
        TaskDAO taskDAO = daoFac.getTaskDAO();
        
        AUP aup = aupDAO.findByName( "test" );
        VOMSUser u = userDAO.findByCertificate( myDn, myCA );
        
        Calendar cal = Calendar.getInstance();
        cal.add( Calendar.HOUR, 24 );
        
        Task t = taskDAO.createSignAUPTask( aup, cal.getTime() );
        taskDAO.makePersistent( t );
        
        u.getTasks().add( t );
        
        HibernateFactory.commitTransaction();
        
    }
    
    private void testGetAUPToBeSigned() {
        
        DAOFactory daoFac = DAOFactory.instance( DAOFactory.HIBERNATE );
        VOMSUserDAO userDAO = VOMSUserDAO.instance();
        TaskDAO taskDAO = daoFac.getTaskDAO();
                
        VOMSUser u = userDAO.findByCertificate( myDn, myCA );
        
        Long taskId = 1L;
        
        if (!u.getTasks().isEmpty()){
            
            log.info( "User '"+u+"' tasks: "+u.getTasks() );
            for (Task t: u.getTasks()){
                
                taskId = t.getId();
                if (t instanceof SignAUPTask){
                    
                    SignAUPTask tt = (SignAUPTask)t;
                    AUP aup = tt.getAup();
                    
                    userDAO.acceptAUP( u, aup );
                    taskDAO.setSignAUPTaskCompleted( tt, u );
                    
                    u.getTasks().remove( t );
                    t.setUser( null );
                    
                    taskDAO.flush();
                }
            }
            
        }
        
        HibernateFactory.commitTransaction();
    }
    
    public void testRemoveAllTasks(){
        
        TaskDAO taskDAO =  DAOFactory.instance( ).getTaskDAO();
                    
        for (Task t: taskDAO.findAll()){
            log.info( "t.getLogRecords(): "+t.getLogRecords() );
        }
        
        taskDAO.removeAllTasks();
        
        HibernateFactory.commitTransaction();
        
    }
    
    
    public TestAUP() {

        PropertyConfigurator.configure( Object.class.getResource( "/test/log4j.properties" ) );        
        log.info("Starting up!");
        setupVOMSConfiguration();
        setupDb();
        
        // Do stuff here
        createAndreaUser();
        createAUP();
        testSignAUPTask();
        testGetAUPToBeSigned();
        
        testRemoveAllTasks();
        
        HibernateFactory.commitTransaction();
        
                        
        TestAUP.t.cancel();
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
