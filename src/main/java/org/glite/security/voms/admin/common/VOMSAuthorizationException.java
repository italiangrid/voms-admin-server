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

import org.glite.security.voms.admin.model.VOMSAdmin;
import org.glite.security.voms.admin.operations.VOMSOperation;


public class VOMSAuthorizationException extends VOMSSecurityException {

    private static final long serialVersionUID = 1L;

    private VOMSAdmin admin;

    private VOMSOperation operation;

    public VOMSAdmin getAdmin() {

        return admin;
    }

    public void setAdmin( VOMSAdmin admin ) {

        this.admin = admin;
    }

    public VOMSOperation getOperation() {

        return operation;
    }

    public void setOperation( VOMSOperation operation ) {

        this.operation = operation;
    }

    public VOMSAuthorizationException( VOMSAdmin a, VOMSOperation o,
            String message ) {

        super( message );

        this.admin = a;
        this.operation = o;

    }

    public VOMSAuthorizationException( String message ) {

        super( message );
        
    }

    public VOMSAuthorizationException( String message, Throwable t ) {

        super( message, t );
     
    }

    public String getMessage() {

    		return "Insufficient privileges to perform \""+operation.getName()+"\"";
       
    }

}
