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
package org.glite.security.voms.admin.common.tasks;

import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.glite.security.voms.admin.common.VOMSConfiguration;
import org.glite.security.voms.admin.dao.RequestDAO;
import org.glite.security.voms.admin.database.HibernateFactory;
import org.glite.security.voms.admin.model.VOMembershipRequest;
import org.glite.security.voms.admin.notification.ExpiredRequestNotification;


public class ExpiredRequestsPurgerTask extends TimerTask {
    
    private static final Log log = LogFactory
            .getLog( ExpiredRequestsPurgerTask.class );
    
    private static final long period = 300;
    
    private Timer timer;
    
    private long requestLifetime;
    
    private boolean warnUsers;
    
    private ExpiredRequestsPurgerTask(Timer t) {

        timer = t;
        
        VOMSConfiguration conf = VOMSConfiguration.instance();
        
        requestLifetime = conf.getLong( "voms.request.vo_membership.lifetime", 300 );
        warnUsers = conf.getBoolean("voms.request.vo_membership.warn_when_expired",false);
        boolean registrationEnabled = conf.getBoolean("voms.request.webui.enabled", true);
        
        if (timer != null){
            
            if (!registrationEnabled)
                log.info( "Request purger not started since the registration is DISABLED for this vo." );
            else if (requestLifetime > 0){
                log.info( "Scheduling expired request reaper thread with period:" +period +" seconds.");
                timer.schedule( this, period * 1000, period * 1000 );
            }
            else
                log.info( "Request purger not started according to configuration (negative lifetime for requests!)." );
        }
        
        

    }

    public void run() {

        log.info( "Checking for expired requests..." );
        HibernateFactory.beginTransaction();
        purgeExpiredRequests();
        HibernateFactory.commitTransaction();
        HibernateFactory.closeSession();
        log.info("Done.");
        
    }
    
    public void purgeExpiredRequests(){
        
        List request = RequestDAO.instance().getUnconfirmed();
        
        Iterator  i = request.iterator();
        
        while (i.hasNext()){
            VOMembershipRequest r = (VOMembershipRequest)i.next();
            
            long currentTime = System.currentTimeMillis();
            long creationTime = r.getCreationDate().getTime();
            
             if ((currentTime - creationTime) > requestLifetime*1000){
                 
                 log.info("Purging expired request #"+r.getId());
                 log.debug( "Request: "+r.toString() );
                 
                 log.debug( "(currentTime - creationTime ):"+(currentTime-creationTime));
                 log.debug( "requestLifetime:" + requestLifetime*1000 );
                 
                 RequestDAO dao = RequestDAO.instance();
                 
                 dao.delete( r );
                 
                 if (warnUsers){
                     
                     ExpiredRequestNotification n = new ExpiredRequestNotification(r.getEmailAddress());
                     n.send();
                 }
                 
             }
            
        }
        
    }
    public static ExpiredRequestsPurgerTask instance(Timer t) {

        return new ExpiredRequestsPurgerTask(t);
    }

}
