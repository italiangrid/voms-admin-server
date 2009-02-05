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
package org.glite.security.voms.admin.database;

public class UserAlreadyExistsException extends AlreadyExistsException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public UserAlreadyExistsException( String message ) {

        super( message );
        // TODO Auto-generated constructor stub
    }

    public UserAlreadyExistsException( String message, Throwable t ) {

        super( message, t );
        // TODO Auto-generated constructor stub
    }

    public UserAlreadyExistsException( Throwable t ) {

        super( t );
        // TODO Auto-generated constructor stub
    }

}