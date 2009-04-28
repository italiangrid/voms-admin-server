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
package org.glite.security.voms.admin.notification;

import java.util.List;
import java.util.Vector;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.glite.security.voms.admin.request.VOMSNotificationException;

public class NotificationService {

    private static final Log log = LogFactory
            .getLog( NotificationService.class );

    private static NotificationService singleton = null;

    private List <EmailNotification> outgoing = new Vector <EmailNotification>();

    private List <EmailNotification> undelivered = new Vector <EmailNotification>();

    private Object lock = new Object();

    private NotificationService() {

    }

    public static NotificationService instance() {

        if ( singleton == null )
            singleton = new NotificationService();

        return singleton;
    }

    
    
    public void scheduledSend(EmailNotification n){
        
        log
        .debug( "Adding notification '" + n
                + "' to outgoing message queue." );
        outgoing.add( n );
    }
    
    
    public void immediateSend( EmailNotification n ) {

        log
                .debug( "Adding notification '" + n
                        + "' to outgoing message queue." );
        outgoing.add( n );
        deliverNotifications();

    }
    

    public void deliverNotifications() {

        synchronized ( lock ) {

            for ( EmailNotification n : undelivered ) {

                try {

                    log.warn( "Trying to deliver undelivered notification '"
                            + n + "'." );
                    log.debug( "Delivery attempt #:"
                            + n.getDeliveryAttemptCount() + 1 );
                    n.send();

                    log.info( "Notification '" + n + "' delivered succesfully" );

                    undelivered.remove( n );

                } catch ( VOMSNotificationException e ) {

                    log.error( "Error sending notification '" + n + "': "
                            + e.getMessage() );
                    if ( log.isDebugEnabled() )
                        log.error( "Error sending notification '" + n + "': "
                                + e.getMessage(), e );

                    outgoing.remove( n );
                    undelivered.add( n );

                }

            }

            for ( EmailNotification n : outgoing ) {

                try {

                    log.info( "Sending notification '" + n + "'." );
                    n.send();
                    log.info( "Notification '" + n + "' delivered succesfully" );
                    outgoing.remove( n );

                } catch ( VOMSNotificationException e ) {

                    log.error( "Error sending notification '" + n + "': "
                            + e.getMessage() );
                    if ( log.isDebugEnabled() )
                        log.error( "Error sending notification '" + n + "': "
                                + e.getMessage(), e );

                    outgoing.remove( n );
                    undelivered.add( n );

                }

            }
        }

    }
}
