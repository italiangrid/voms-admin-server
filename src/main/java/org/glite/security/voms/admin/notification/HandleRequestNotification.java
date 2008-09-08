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
import org.glite.security.voms.admin.model.VOMembershipRequest;


public class HandleRequestNotification extends VelocityEmailNotification {

    private static final String templateFileName = "HandleRequest.vm";
    
    VOMembershipRequest request;
    String requestManagementURL;
    
    public HandleRequestNotification(VOMembershipRequest request, 
                String requestManagementURL) {
        
        setTemplateFile( templateFileName );
        this.request = request;
        this.requestManagementURL = requestManagementURL;
    }

    protected void buildMessage() {
        
        String voName = VOMSConfiguration.instance().getVOName();
        setSubject( "A membership request for VO "+voName+" requires your approval." );
        
        VelocityContext context = new VelocityContext();
        
        context.put( "voName", voName );
        context.put( "recipient", "VO Admin");
        context.put( "req", request);
        context.put( "requestManagementURL", requestManagementURL);
        
        buildMessageFromTemplate( context );
        
    }
}
