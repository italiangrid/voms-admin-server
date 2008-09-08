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

package org.glite.security.voms.admin.actions;

import javax.servlet.http.HttpServletRequest;

import org.glite.security.voms.admin.common.Constants;
import org.glite.security.voms.admin.dao.SearchResults;
import org.glite.security.voms.admin.model.VOMSGroup;
import org.glite.security.voms.admin.model.VOMSRole;
import org.glite.security.voms.admin.model.VOMSUser;




public class ActionUtils {

    public static void storeUser( HttpServletRequest request, VOMSUser u ) {

        request.setAttribute( Constants.USER_KEY, u );

    }

    public static void storeGroup( HttpServletRequest request, VOMSGroup g ) {

        request.setAttribute( Constants.GROUP_KEY, g );

    }

    public static void storeRole( HttpServletRequest request, VOMSRole r ) {

        request.setAttribute( Constants.ROLE_KEY, r );

    }

    public static void storeResults( HttpServletRequest request, SearchResults r ) {

        request.setAttribute( Constants.RESULTS_KEY, r );
    }

    public static VOMSGroup getGroupFromRequest(HttpServletRequest request){
        
        return (VOMSGroup) request.getAttribute(Constants.GROUP_KEY);
        
    }

    public static VOMSRole getRoleFromRequest(HttpServletRequest request){
        
        return (VOMSRole) request.getAttribute(Constants.ROLE_KEY);
        
    }
    public static SearchResults getResultsFromRequest( HttpServletRequest request ) {

        
        return (SearchResults) request.getAttribute(Constants.RESULTS_KEY);
    }

    public static VOMSUser getUserFromRequest( HttpServletRequest request ) {

        return (VOMSUser) request.getAttribute(Constants.USER_KEY);
    }
}
