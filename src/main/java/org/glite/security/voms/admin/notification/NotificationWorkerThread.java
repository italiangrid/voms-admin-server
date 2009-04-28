package org.glite.security.voms.admin.notification;

import java.util.Timer;
import java.util.TimerTask;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.glite.security.voms.admin.common.VOMSConfiguration;


public class NotificationWorkerThread extends TimerTask {

    private static final Log log = LogFactory
            .getLog( NotificationWorkerThread.class );
    
    protected Timer theTimer;
    
    public static NotificationWorkerThread instance(Timer t) {

        return new NotificationWorkerThread(t);
    }
    
    private NotificationWorkerThread(Timer t) {

        this.
        theTimer = t;
        VOMSConfiguration config = VOMSConfiguration.instance();
        
        if (theTimer != null){
            
            
            // retry period in seconds, default 30 seconds
            int period = config.getInt( VOMSConfiguration.NOTIFICATION_RETRY_PERIOD, 30 );
            
            if (period > 0){
                
                log.info( "Notification retry thread period: "+period+" sec" );
                theTimer.schedule( this, period*1000);
                
            }else
                log.info( "Notification retry service not started." );
            
        }
        
        
    }
    @Override
    public void run() {
        
        log.info( "Notification retry thread started..." );
        
        NotificationService.instance().deliverNotifications();        

    }

}
