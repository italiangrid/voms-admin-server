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

import org.apache.velocity.VelocityContext;
import org.glite.security.voms.admin.common.VOMSConfiguration;

public class RequestRejectedNotification extends VelocityEmailNotification {
    
    private static final String templateFileName = "RequestRejected.vm";
    String rejectReasons;
    
    
    public RequestRejectedNotification(String recipient, String rejectReasons) {
        
        setTemplateFile( templateFileName );
        addRecipient( recipient );
        this.rejectReasons = rejectReasons;
    }


    protected void buildMessage() {
        String voName = VOMSConfiguration.instance().getVOName();
        
        setSubject( "Your membership request for VO "+voName+" has been rejected." );
        VelocityContext context = new VelocityContext();
        
        context.put( "voName", voName );
        context.put( "recipient", getRecipientList().get( 0 ));
        context.put( "rejectReasons", rejectReasons);
        
        buildMessageFromTemplate( context );
        
    }

}
