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

package org.glite.security.voms.admin.request;

import java.util.Iterator;
import java.util.Vector;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ActionSequence implements Action {

    private static final Log log = LogFactory.getLog( ActionSequence.class );

    private Vector actions = new Vector();

    public ActionSequence() {

        super();
     
    }

    public void addAction( Action a ) {

        actions.add( a );
    }

    public void removeAction( Action a ) {

        actions.remove( a );
    }

    public Object execute() {

        if ( actions.isEmpty() ) {
            log.debug( "No actions defined, nothing to do!" );
            return null;
        }

        Vector returnValues = new Vector();
        Iterator i = actions.iterator();

        while ( i.hasNext() ) {

            Action a = null;

            try {
                a = (Action) i.next();
                log.debug( "Executing " + a );
                Object retValue = a.execute();
                returnValues.add( retValue );

            } catch ( Throwable t ) {

                log.error( "Error executing action " + a, t );
                return null;
            }

        }

        return returnValues;
    }

}