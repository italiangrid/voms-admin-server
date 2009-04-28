/*******************************************************************************
 *Copyright (c) Members of the EGEE Collaboration. 2006. 
 *See http://www.eu-egee.org/partners/ for details on the copyright
 *holders.  
 *
 *Licensed under the Apache License, Version 2.0 (the "License"); 
 *you may not use this file except in compliance with the License. 
 *You may obtain a copy of the License at 
 *
 *    http://www.apache.org/licenses/LICENSE-2.0 
 *
 *Unless required by applicable law or agreed to in writing, software 
 *distributed under the License is distributed on an "AS IS" BASIS, 
 *WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 *See the License for the specific language governing permissions and 
 *limitations under the License.
 *
 * Authors:
 *     Andrea Ceccanti - andrea.ceccanti@cnaf.infn.it
 *******************************************************************************/
package org.glite.security.voms.admin.tools;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.apache.commons.collections.MultiHashMap;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.PropertyConfigurator;
import org.glite.security.voms.admin.common.Constants;
import org.glite.security.voms.admin.common.VOMSConfiguration;
import org.glite.security.voms.admin.common.VOMSException;
import org.glite.security.voms.admin.common.tasks.DatabaseSetupTask;
import org.glite.security.voms.admin.common.tasks.UpdateCATask;
import org.glite.security.voms.admin.dao.ACLDAO;
import org.glite.security.voms.admin.dao.VOMSAdminDAO;
import org.glite.security.voms.admin.dao.VOMSGroupDAO;
import org.glite.security.voms.admin.dao.VOMSRoleDAO;
import org.glite.security.voms.admin.dao.VOMSUserDAO;
import org.glite.security.voms.admin.database.HibernateFactory;
import org.glite.security.voms.admin.model.ACL;
import org.glite.security.voms.admin.model.VOMSAdmin;
import org.glite.security.voms.admin.model.VOMSGroup;
import org.glite.security.voms.admin.model.VOMSRole;
import org.glite.security.voms.admin.model.VOMSUser;
import org.glite.security.voms.admin.operations.VOMSContext;
import org.glite.security.voms.admin.operations.VOMSPermission;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.cfg.Configuration;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.hibernate.type.LongType;
import org.hibernate.type.ShortType;



public class SchemaDeployer {

    public static final String ORACLE_PRODUCT_NAME = "Oracle";
    public static final String MYSQL_PRODUCT_NAME = "MySQL";
    
    private static final Log log = LogFactory.getLog( SchemaDeployer.class );

    protected CommandLineParser parser = new PosixParser();

    protected HelpFormatter helpFormatter = new HelpFormatter();

    protected Options options;

    String command;

    String vo;

    String hibernateConfigFile = null;

    String hibernatePropertiesFile = null;

    String adminDN = null;

    String adminCA = null;
    
    String adminEmailAddress = null;

    SessionFactory sf;

    public SchemaDeployer( String[] args ) {

        setupCLParser();
        checkArguments( args );
        execute();

    }

    private void execute() {

        System.setProperty( VOMSConfiguration.VO_NAME, vo );
        VOMSConfiguration.instance( false, null );

        if ( command.equals( "deploy" ) )
            doDeploy();
        else if ( command.equals( "undeploy" ) )
            doUndeploy();
        else if ( command.equals( "upgrade" ) )
            doUpgrade();
        else if ( command.equals( "add-admin" ) )
            doAddAdmin();
        else if (command.equals("remove-admin"))
        	doRemoveAdmin();
        else
        	throw new VOMSException("Unknown command: "+command);
            	
        System.exit( 0 );
    }

    private boolean isOracleBackend(){
        
        Session s = HibernateFactory.getSession();
        Transaction t = s.beginTransaction();
        
        DatabaseMetaData dbMetadata = null;
        String dbProductName = null;
        
        try {
            
            dbMetadata = s.connection().getMetaData();
            dbProductName = dbMetadata.getDatabaseProductName();
                    
        } catch ( HibernateException e ) {
            
            log.error("Hibernate error accessing database metadata from Hibernate connection!", e);
            System.exit( -1 );
            
        } catch ( SQLException e ) {
            
            log.error("SQL error while accessing database metadata from Hibernate connection!", e);
            System.exit( -1 );
            
        }
        
        log.debug( "Detected database: "+dbProductName );
        return dbProductName.trim().equals( ORACLE_PRODUCT_NAME );
     
    }
    
    
    private void printExceptions( List l ) {

        Iterator i = l.iterator();

        while ( i.hasNext() ) {

            Throwable t = (Throwable) i.next();
            log.fatal( t.getMessage() );

            if ( log.isDebugEnabled() )
                log.fatal( t, t );
        }

    }
    
    private int checkDatabaseExistence(){
        
        log.info( "Checking database existence..." );
        Session s = HibernateFactory.getSession();
        Transaction t = s.beginTransaction();
        
            
        DatabaseMetaData dbMetadata = null;
        
        try {
            
            dbMetadata = s.connection().getMetaData();
        
        
        } catch ( HibernateException e ) {
            
            log.error("Hibernate error accessing database metadata from Hibernate connection!", e);
            System.exit( -1 );
            
        } catch ( SQLException e ) {
            
            log.error("SQL error while accessing database metadata from Hibernate connection!", e);
            System.exit( -1 );
            
        }
        
        String[] names = { "TABLE" };

        ResultSet tableNames = null;
        
        try {
            
            tableNames = dbMetadata.getTables( null, "%", "%", names );
                
        } catch ( SQLException e ) {
            log.error("Error reading table names from database metadata object!", e);
            System.exit( -1 );
        }
        
        
        boolean foundACL2 = false;
        boolean foundACL = false;

        try {
            
            
            while ( tableNames.next() ) {
                String tName = tableNames.getString( "TABLE_NAME" );
                if ( tName.equals( "ACL" ) || tName.equals( "acl" ))
                    foundACL = true;

                if ( tName.equals( "ACL2" ) || tName.equals( "acl2" ))
                    foundACL2 = true;
            }
        
            
            HibernateFactory.commitTransaction();
            
        } catch ( SQLException e ) {
            
            log.error( "Error accessing table names result set!", e );
            System.exit( -1 );   
        
        } catch (HibernateException e) {
            log.error( "Error committing read-only hibernate transaction!", e );
            System.exit( -1 );
            
            
        }
        
        if ( foundACL2 ) {
            log.info( "Found existing voms-admin 2.0.x database..." );
            return 2;
        }
        
        if ( foundACL ) {
            log.info( "Found existing voms-admin 1.2.x database..." );
            return 1;

        }

        return -1;        
    }

    private void doUpgrade() {

        Configuration hibernateConfig = loadHibernateConfiguration();
        
        int existingDB = checkDatabaseExistence();
        
        if (existingDB == -1){
            log.error("No voms-admin 1.2.x database found to upgrade!");
            System.exit(-1);
        }
            
        Session s = HibernateFactory.getSession();
        HibernateFactory.beginTransaction();
        
        try{
            
            log.info( "Upgrading voms database..." );

            renameOldTables();
            
        
            HibernateFactory.commitTransaction();
            
            HibernateFactory.beginTransaction();
            

            SchemaExport exporter = new SchemaExport( hibernateConfig );

            exporter.execute( true, true, false, true );

            log.info( "Deploying voms 2 database..." );

            List l = exporter.getExceptions();

            if ( !l.isEmpty() ) {
                log.fatal( "Error deploying voms 2 database!" );
                printExceptions( l );
                System.exit( 2 );

            }

            if (isOracleBackend())
                fixHibernateSequence();
            
            removeDuplicatedACLEntries();
            
            migrateDbContents();
            migrateMappings();
            migrateACLs();
            dropOldTables();
            
            if (isOracleBackend())
                dropOldSequences();
            
            HibernateFactory.commitTransaction();
            log.info( "Database upgraded succesfully!" );
            
        }catch(Throwable t){
            
            log.error("Database upgrade failed!");
            log.error( t, t );
            HibernateFactory.rollbackTransaction();
            HibernateFactory.closeSession();
            System.exit( 2 );
        }
        
        
        
        
    }

    private void doRemoveAdmin(){
    	if (adminDN == null || adminCA == null)
    		throw new VOMSException( "adminDN or adminCA is not set!" );
    	
    	try{
    		
    		VOMSAdmin a = VOMSAdminDAO.instance().getByName( adminDN, adminCA );
            
            if (a == null){
                
                log.info( "Admin '"+adminDN+","+adminCA+"' does not exists in database..." );
                return;
            }
            
            ACLDAO.instance().deletePermissionsForAdmin(a);
            VOMSAdminDAO.instance().delete(a);
            
            HibernateFactory.commitTransaction();
    		
    	}catch(Throwable t){
            
            log.error( "Error removing administrator!" );
            log.error(t,t);
            
            System.exit( -1 );
        }
    }
    private void doAddAdmin() {

        if ( adminDN == null || adminCA == null || adminEmailAddress == null)
            throw new VOMSException( "adminDN or adminCA or adminEmailAddress is not set!" );

        

        try{
            
            VOMSAdmin a = VOMSAdminDAO.instance().getByName( adminDN, adminCA );
            
            if (a != null){
                
                log.info( "Admin '"+a.getDn()+","+a.getCa().getDn()+"' already exists in database..." );
                return;
            }
            
            // Admin does not exist, create it!
            a = VOMSAdminDAO.instance().create( adminDN, adminCA, adminEmailAddress );
            Iterator i = VOMSGroupDAO.instance().findAll().iterator();
            Iterator rolesIter = VOMSRoleDAO.instance().findAll().iterator();
        
        
        while (i.hasNext()){
            
            VOMSGroup g = (VOMSGroup)i.next();
            g.getACL().setPermissions( a, VOMSPermission.getAllPermissions() );
            while(rolesIter.hasNext()){
                
                VOMSRole r = (VOMSRole) rolesIter.next();
                VOMSContext.instance( g, r ).getACL().setPermissions( a, VOMSPermission.getAllPermissions() );
                HibernateFactory.getSession().update( r );
            }
            HibernateFactory.getSession().update( g );
            
        }

        HibernateFactory.commitTransaction();
        
        }catch(Throwable t){
            
            log.error( "Error adding new administrator!" );
            log.error(t,t);
            
            System.exit( -1 );
        }

    }

    private Configuration loadHibernateConfiguration() {

        Properties dbProperties = new Properties();

        try {

            if ( hibernatePropertiesFile == null ) {

                String f = System.getProperty( "GLITE_LOCATION_VAR" )
                        + "/etc/voms-admin/" + vo + "/voms.database.properties";

                log
                        .debug( "Loading database properties from: "
                                + f );

                dbProperties.load( new FileInputStream( f ) );

            } else
                dbProperties
                        .load( new FileInputStream( hibernatePropertiesFile ) );

        } catch ( IOException e ) {

            log.error( "Error loading hibernate properties: " + e.getMessage(),
                    e );
            throw new VOMSException( "Error loading hibernate properties: "
                    + e.getMessage(), e );

        }

        return new AnnotationConfiguration().addProperties( dbProperties ).configure();
    }

    private void doUndeploy() {

        log.info( "Undeploying voms database..." );
        Configuration hibernateConfig = loadHibernateConfiguration();
        
        int existingDB = checkDatabaseExistence();
        
        if (existingDB == 1){
            log.error("This tool cannot undeploy voms-admin 1.2.x database! Please upgrade to voms-admin 2 or use voms-admin-configure 1.2.x tools to undeploy this database.");
            System.exit( -1 );
        }
        
        if (existingDB < 0){
            
            log.error("No voms-admin database found!");
            System.exit(-1);
        }
        
        SchemaExport export = new SchemaExport( hibernateConfig );

        export.drop( false, true );

        List l = export.getExceptions();

        if ( !l.isEmpty() ) {
            log.fatal( "Error undeploying voms database!" );
            printExceptions( l );
            System.exit( 2 );
        }

        log.info( "Database undeployed correctly!" );

    }

    private void doDeploy() {

        Configuration hibernateConfig = loadHibernateConfiguration();

        int existingDb = checkDatabaseExistence();
        
        if (existingDb > 0){
            log.warn( "Existing voms database found. Will not overwrite the database!" );
            System.exit( 0 );
        }
            
        SchemaExport exporter = new SchemaExport( hibernateConfig );

        exporter.execute( true, true, false, true );

        log.info( "Deploying voms database..." );

        List l = exporter.getExceptions();

        if ( !l.isEmpty() ) {
            log.fatal( "Error deploying voms database!" );
            printExceptions( l );
            System.exit( 2 );

        }

        UpdateCATask caTask = UpdateCATask.instance( null );
        caTask.run();

        DatabaseSetupTask task = DatabaseSetupTask.instance();
        task.run();

        HibernateFactory.commitTransaction();
        log.info( "Database deployed correctly!" );

    }

    protected void setupCLParser() {

        options = new Options();

        options.addOption( OptionBuilder.withLongOpt( "help" ).withDescription(
                "Displays helps and exits." ).create( "h" ) );

        options
                .addOption( OptionBuilder
                        .withLongOpt( "command" )
                        .withDescription(
                                "Specifies the command to be executed: deploy,undeploy,upgrade,add-admin" )
                        .hasArg().create( "command" ) );

        options.addOption( OptionBuilder.withLongOpt( "vo" ).withDescription(
                "Specifies the vo name." ).hasArg().create( "vo" ) );

        options.addOption( OptionBuilder.withLongOpt( "config" )
                .withDescription(
                        "Specifies the hibernate config file to be used." )
                .hasArg().create( "config" ) );

        options.addOption( OptionBuilder.withLongOpt( "properties" )
                .withDescription(
                        "Specifies the hibernate properties file to be used." )
                .hasArg().create( "properties" ) );

        options
                .addOption( OptionBuilder
                        .withLongOpt( "dn" )
                        .withDescription(
                                "Specifies the dn for the admin to add (valid only if add-admin command is given)." )
                        .hasArg().create( "dn" ) );

        options
                .addOption( OptionBuilder
                        .withLongOpt( "ca" )
                        .withDescription(
                                "Specifies the ca for the admin to add (valid only if add-admin command is given)." )
                        .hasArg().create( "ca" ) );
        
        options
        .addOption( OptionBuilder
                .withLongOpt( "email" )
                .withDescription(
                        "Specifies the email address for the admin to add (valid only if add-admin command is given)." )
                .hasArg().create( "email" ) );

    }

    protected void checkArguments( String[] args ) {

        try {

            CommandLine line = parser.parse( options, args );

            if ( line.hasOption( "h" ) )

                printHelpMessageAndExit( 0 );

            if ( !line.hasOption( "command" ) ) {

                System.err.println( "No command specified!" );
                printHelpMessageAndExit( 1 );

            }

            if ( !line.hasOption( "vo" ) ) {
                System.err.println( "No vo specified!" );
                printHelpMessageAndExit( 1 );
            }

            command = line.getOptionValue( "command" );

            if ( !command.equals( "deploy" ) && !command.equals( "upgrade" )
                    && !command.equals( "add-admin" )
                    && !command.equals( "remove-admin" )
                    && !command.equals( "undeploy" ) )
                throw new VOMSException( "Unknown command specified!" );

            vo = line.getOptionValue( "vo" );

            if ( line.hasOption( "hb-config" ) )
                hibernateConfigFile = line.getOptionValue( "hb-config" );

            if ( line.hasOption( "hb-properties" ) )
                hibernatePropertiesFile = line.getOptionValue( "hb-properties" );

            if ( line.hasOption( "dn" ) )
                adminDN = line.getOptionValue( "dn" );

            if ( line.hasOption( "ca" ) )
                adminCA = line.getOptionValue( "ca" );
            
            if ( line.hasOption( "email" ) )
                adminEmailAddress = line.getOptionValue( "email" );

        } catch ( ParseException e ) {

            throw new VOMSException( "Error parsing command-line arguments: "
                    + e.getMessage(), e );

        }

    }

    private void printHelpMessageAndExit( int exitStatus ) {

        helpFormatter.printHelp( "SchemaDeployer", options );
        System.exit( exitStatus );

    }
    
    private int dropTable(String tableName){
        
        Session s = HibernateFactory.getSession();
        String command = "drop table "+tableName;
        return s.createSQLQuery( command ).executeUpdate();
    }
    
    private void dropOldTables(){
        String[] dTables = new String[]{
          "acl_old",      
          "acld",
          "admins_old",
          "attributes_old",
          "ca_old",
          "capabilities_old",
          "capabilitiesd",
          "group_attrs_old",
          "groups_old",
          "groupsd",
          "m_old",
          "md",
          "periodicity",
          "realtime",
          "requests_old",
          "role_attrs_old",
          "roles_old",
          "rolesd",
          "seqnumber_old",
          "sequences",
          "usr_old",
          "usr_attrs_old",
          "usrd",
          "version_old",
          "validity"
        };
        
        for ( int i = 0; i < dTables.length; i++ ) 
            dropTable( dTables[i]);
        
        
    }
    
    private int dropSequence(String sequenceName){
        
        log.debug( "Dropping sequence "+sequenceName );
        Session s = HibernateFactory.getSession();
        String command = "drop sequence "+sequenceName;
        
        try{
            return s.createSQLQuery( command ).executeUpdate();
        
        }catch(HibernateException e){
            if (e.getCause().getMessage().contains( "sequence does not exist" )){
                log.warn( "Error dropping sequence: "+sequenceName+"... such sequence doesn't exist." );
                log.warn( "This error may be ignored at this stage of the database upgrade...");
            }
            return 0;
        }
    }
    
    private void dropOldSequences(){
        log.info( "Dropping old sequences..." );
        
        String[] oldSequences = new String[]{
          "voms_seq_ca",
          "voms_seq_transaction",
          "voms_seq_admin",
          "voms_seq_acl",
          "voms_seq_role",
          "voms_seq_group",
          "voms_seq_user"     
        };
        
        for ( int i = 0; i < oldSequences.length; i++ ) 
            dropSequence( oldSequences[i] );
                                             
    }
    
    private int renameTable(String oldName){
        
        Session s = HibernateFactory.getSession();
        String command = "alter table "+oldName+" rename to "+oldName+"_old";
        return s.createSQLQuery( command ).executeUpdate();
    }

    private void renameOldTables(){
        
        String[] oldTables = new String[]{
                "ca",
                "acl",
                "admins",
                "attributes",
                "capabilities",
                "group_attrs",
                "groups",
                "role_attrs",
                "roles",
                "seqnumber",
                "usr",
                "usr_attrs",
                "m",
                "requests",
                "version"
        };
        
        for (int i=0; i < oldTables.length; i++)
            renameTable(oldTables[i] );
        
        
    }
    
    
    // See bug https://savannah.cern.ch/bugs/?36291
    private void fixHibernateSequence() {
        log.info( "Migrating sequences since on oracle backend..." );
        Session s = HibernateFactory.getSession();

        Long maxSeqValue = (Long) s
                .createSQLQuery(
                        "select max(last_number) as max from user_sequences where sequence_name like 'VOMS_%'" )
                .addScalar( "max", new LongType() ).uniqueResult();
        
        // Recreate hibernate sequence
        String dropHibSeqStatement = "drop sequence HIBERNATE_SEQUENCE";
        String createHibSeqStatement = "create sequence HIBERNATE_SEQUENCE MINVALUE 1 MAXVALUE 999999999999999999999999999 " +
            "INCREMENT BY 1 START WITH "+maxSeqValue+" CACHE 20 NOORDER NOCYCLE";
        
        s.createSQLQuery( dropHibSeqStatement ).executeUpdate();
        s.createSQLQuery( createHibSeqStatement ).executeUpdate();
        log.info("Sequences migration complete.");
    }
    
    private void migrateDbContents(){
        
        log.info( "Migrating db contents..." );
        
        Session s = HibernateFactory.getSession();
                
        s.createSQLQuery( "insert into ca (cid, ca, cadescr) select cid, ca, cadescr from ca_old").executeUpdate();
        s.createSQLQuery( "insert into admins(adminid,dn,ca) select adminid, dn,ca from admins_old").executeUpdate();
        s.createSQLQuery( "insert into groups(gid,dn,parent,must) select gid,dn,parent,must from groups_old" ).executeUpdate();
        s.createSQLQuery( "insert into roles(rid,role) select rid, role from roles_old" ).executeUpdate();
        s.createSQLQuery( "insert into usr(userid,dn,ca,cn,mail,cauri) select userid, dn, ca, cn, mail, cauri from usr_old" ).executeUpdate();
                
        s.createSQLQuery( "insert into version values('2')").executeUpdate();
        
        // Generic attributes migration
        s.createSQLQuery( "insert into attributes(a_id, a_name, a_desc) select a_id,a_name,a_desc from attributes_old" ).executeUpdate();
        s.createSQLQuery( "insert into usr_attrs(u_id,a_id,a_value) select u_id,a_id,a_value from usr_attrs_old" ).executeUpdate();
        s.createSQLQuery( "insert into group_attrs(g_id,a_id,a_value) select g_id,a_id,a_value from group_attrs_old" ).executeUpdate();
        s.createSQLQuery( "insert into role_attrs(r_id,g_id,a_id,a_value) select r_id, g_id,a_id,a_value from role_attrs_old" ).executeUpdate();
        
        // Seqnumber migration
        s.createSQLQuery( "insert into seqnumber(seq) select seq from seqnumber_old").executeUpdate();
    }
    
    
    private void migrateMappings(){
        
        Session s = HibernateFactory.getSession();
        
        List oldMappings = s.createSQLQuery( "select userid,gid,rid,cid from m_old" )
            .addScalar( "userid",new LongType() )
            .addScalar( "gid",new LongType() )
            .addScalar( "rid",new LongType() )
            .addScalar( "cid",new LongType() )
            .list();
        
        Iterator i = oldMappings.iterator();
        
        VOMSUserDAO dao = VOMSUserDAO.instance();
        
        while (i.hasNext()){
            
            Object[] result = (Object[]) i.next();
            
            if (result == null)
                break;
            
            VOMSUser u = dao.findById( (Long) result[0]);
            VOMSGroup g = VOMSGroupDAO.instance().findById( (Long )result[1]);
            VOMSRole r = null;
            
            if (result[2]!= null)
                r = VOMSRoleDAO.instance().findById( (Long)result[2] );
            
            log.debug("Mapping: "+u+","+g+","+r);
            
            if (r == null){
                if (!u.isMember( g ))
                    dao.addToGroup( u, g );
            
            }else{
                if (!u.isMember( g ))
                    dao.addToGroup( u, g );
                
                VOMSUserDAO.instance().assignRole( u, g, r );
                
            }   
            
            s.save( u );
        }
        
    }
    
    private void removeDuplicatedACLEntries(){
        log.info("Removing eventual buggy duplicated ACL entries... ");
        
        Session s = HibernateFactory.getSession();
        s.createSQLQuery( "delete from admins_old where dn not like '/O=VOMS/%' and adminid not in (select adminid from acl_old)" ).executeUpdate();
        
    }
    private void migrateACLs() {
        
        Iterator adminIter = VOMSAdminDAO.instance().getAll().iterator();

        while ( adminIter.hasNext() ) {

            VOMSAdmin a = (VOMSAdmin) adminIter.next();

            long adminId = a.getId().longValue();

            if ( (a.getDn().equals( Constants.ANYUSER_ADMIN )) || (!a.getDn().startsWith( "/O=VOMS" )) ) {
                
                log.debug("Migrating acls for admin : "+a.getDn());

                MultiHashMap m = loadDefaultACLEntriesForAdmin( adminId );

                if ( m != null) {

                    Iterator keys = m.keySet().iterator();

                    while ( keys.hasNext() ) {
                        
                        List perms = (List) m.get( keys.next() );
                        setGlobalPermission( a, ACLMapper
                                .translatePermissions( perms ) );

                    }
                }

                m = loadGroupACLEntriesForAdmin( adminId );

                if ( m != null ) {

                    Iterator keys = m.keySet().iterator();

                    while ( keys.hasNext() ) {

                        Long groupId = (Long) keys.next();

                        List perms = (List) m.get( groupId );

                        VOMSGroup targetGroup = VOMSGroupDAO.instance()
                                .findById( groupId );

                        targetGroup.getACL().setPermissions( a,
                                ACLMapper.translatePermissions( perms ) );

                    }

                }

                m = loadRoleACLEntriesForAdmin( adminId );

                if ( m != null ) {

                    Iterator keys = m.keySet().iterator();

                    while ( keys.hasNext() ) {

                        Long roleId = (Long) keys.next();
                        List perms = (List) m.get( roleId );

                        VOMSRole targetRole = VOMSRoleDAO.instance().findById(
                                roleId );
                        setPermissionOnRole( a, targetRole, ACLMapper
                                .translatePermissions( perms ) );
                    }
                }
            }
        }

    }

    public static void main( String[] args ) throws ConfigurationException {

        if ( System.getProperty( "GLITE_LOCATION" ) == null )
            throw new VOMSException(
                    "Please set the GLITE_LOCATION system property before running this utility." );

        if ( System.getProperty( "GLITE_LOCATION_VAR" ) == null )
            throw new VOMSException(
                    "Please set the GLITE_LOCATION_VAR system property before running this utility." );

        PropertyConfigurator.configure( System.getProperty( "GLITE_LOCATION" )
                + "/share/voms-admin/tools/classes/log4j.properties" );
        new SchemaDeployer( args );

    }

    private MultiHashMap buildACLEntries( List acl ) {

        if ( acl.isEmpty() )
            return null;

        MultiHashMap map = new MultiHashMap();

        Iterator i = acl.iterator();
        
        while ( i.hasNext() ) {

            Object[] res = (Object[]) i.next();
            map.put( res[0], res[1] );

        }
        
        return map;

    }

    private MultiHashMap loadGroupACLEntriesForAdmin( long adminid ) {

        Session s = HibernateFactory.getSession();

        String query = "select groups.gid as gid, acl.operation as operation from acl_old acl, groups_old groups where acl.aid = groups.aclid and acl.allow = 1 and adminid = :adminId";

        List acls = s.createSQLQuery( query )
        .addScalar( "gid" , new LongType())
        .addScalar("operation", new ShortType())
        .setLong( "adminId", adminid ).list();

        return buildACLEntries( acls );

    }
    
    
    private MultiHashMap loadDefaultACLEntriesForAdmin( long adminid ) {
        
        Session s = HibernateFactory.getSession();

        String query = "select -1 as gid, acl.operation as operation from acl_old acl, groups_old groups where acl.aid = 0 and acl.allow = 1 and adminid = :adminId";

        List acls = s.createSQLQuery( query )
        .addScalar( "gid" , new LongType())
        .addScalar(  "operation", new ShortType() )
        .setLong( "adminId", adminid ).list();

        return buildACLEntries( acls );
    }

    private MultiHashMap loadRoleACLEntriesForAdmin( long adminid ) {

        Session s = HibernateFactory.getSession();

        String query = "select roles.rid as rid, acl.operation as operation from acl_old acl, roles_old roles where acl.aid = roles.aclid and acl.allow = 1 and adminid = :adminId";

        List acls = s.createSQLQuery( query )
        .addScalar( "rid", new LongType() )
        .addScalar( "operation", new ShortType() )
        .setLong( "adminId", adminid ).list();

        return buildACLEntries( acls );
    }

    private void setPermissionOnRole( VOMSAdmin a, VOMSRole r, VOMSPermission p ) {

        log.debug( "Setting permissions "+p.getCompactRepresentation()+" for admin "+a.getDn() +" on role "+r.getName());
        List groups = VOMSGroupDAO.instance().findAll();

        Iterator groupIter = groups.iterator();

        while ( groupIter.hasNext() ) {

            VOMSGroup g = (VOMSGroup) groupIter.next();
            ACL roleACL = r.getACL( g );
            if (roleACL == null){
                roleACL = new ACL(g,r,false);
                roleACL.setPermissions( a, p );
                r.getAcls().add( roleACL );
            }else
                roleACL.setPermissions( a, p );
        }

    }

    private void setGlobalPermission( VOMSAdmin a, VOMSPermission p ) {

        log.debug( "Setting global permissions "+p.getCompactRepresentation()+" for admin "+a.getDn() );
        List groups = VOMSGroupDAO.instance().findAll();
        List roles = VOMSRoleDAO.instance().getAll();

        Iterator groupIter = groups.iterator();

        while ( groupIter.hasNext() ) {

            VOMSGroup g = (VOMSGroup) groupIter.next();
            
            ACL acl = g.getACL();
            
            if (acl == null){
                acl = new ACL(g,false);
                acl.setPermissions( a, p );
                g.getAcls().add( acl );
            }else
                acl.setPermissions( a, p );

            Iterator roleIter = roles.iterator();

            while ( roleIter.hasNext() ) {

                VOMSRole r = (VOMSRole) roleIter.next();
                ACL roleACL = r.getACL( g );
                
                if (roleACL == null){
                    roleACL = new ACL(g,r,false);
                    roleACL.setPermissions( a, p );
                    r.getAcls().add( roleACL );
                }else
                    roleACL.setPermissions( a, p );
            }
        }
    }

}
