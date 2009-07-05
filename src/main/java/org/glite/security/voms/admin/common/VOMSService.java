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
package org.glite.security.voms.admin.common;

import java.util.Properties;
import java.util.Timer;

import javax.servlet.ServletContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.velocity.app.Velocity;
import org.glite.security.voms.admin.common.tasks.MembershipValidityCheckTask;
import org.glite.security.voms.admin.common.tasks.ExpiredRequestsPurgerTask;
import org.glite.security.voms.admin.common.tasks.TaskStatusUpdater;
import org.glite.security.voms.admin.common.tasks.ThreadUncaughtExceptionHandler;
import org.glite.security.voms.admin.common.tasks.UpdateCATask;
import org.glite.security.voms.admin.database.HibernateFactory;
import org.glite.security.voms.admin.event.EventManager;
import org.glite.security.voms.admin.event.LogListener;
import org.glite.security.voms.admin.notification.NotificationService;
import org.glite.security.voms.admin.notification.ServiceNotificationDispatcher;



public final class VOMSService {

    static final Log log = LogFactory.getLog( VOMSService.class );

    static final Timer vomsTimer = new Timer( true );

    public static void start(ServletContext ctxt) {

    	
        VOMSConfiguration conf;
        try {
            
            conf = VOMSConfiguration.instance(ctxt);

        } catch ( VOMSConfigurationException e ) {
            log.fatal( "VOMS-Admin configuration failed!", e );
            throw new VOMSFatalException( e );
        }

        log.info( "VOMS-Admin starting for vo:" + conf.getVOName() );
        
        log.info( "Configuration setup ok." );
        
        Thread.setDefaultUncaughtExceptionHandler(new ThreadUncaughtExceptionHandler());
        
        // Starts event manager
        EventManager.instance();
        EventManager.instance().register(new LogListener());
        
        ServiceNotificationDispatcher.instance();
        
        UpdateCATask.instance( getTimer() );
                  
        try {
            
                Properties p = new Properties();
                
                p.put("resource.loader","file");
                p.put("file.resource.loader.class","org.apache.velocity.runtime.resource.loader.FileResourceLoader");
                p.put("file.resource.loader.path",conf.getTemplatePath());
                p.put( "runtime.log.logsystem.class","org.glite.security.voms.admin.velocity.VelocityLogger");
                 
                Velocity.init(p);
                log.info( "Velocity setup ok!" );
                
        } catch ( Exception e ) {
               
            log.error("Error initializing velocity template engine!");
            throw new VOMSFatalException(e);
        }
        
        ExpiredRequestsPurgerTask.instance( getTimer() );
        
        TaskStatusUpdater.instance(getTimer());
        
        MembershipValidityCheckTask.instance(getTimer());
        
        
        log.info( "VOMS-Admin started succesfully." );
    }

    public static void stop() {

        getTimer().cancel();
        
        NotificationService.instance().stop();
        
        // Close hibernate session factory
        HibernateFactory.getFactory().close();
        log.info( "VOMS admin stopped ." );
    }

    public static Timer getTimer() {

        return vomsTimer;
    }
}
