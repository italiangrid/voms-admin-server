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
 *     Akos Frohner - akos.frohner@cern.ch
 *     Karoly Lorentey - karoly.lorentey.@cern.ch
 *******************************************************************************/

package org.glite.security.voms.admin.api.compatibility;

import org.glite.security.voms.admin.api.VOMSException;



/** 
 * Virtual Organization Membership Service Compatibility
 * service for the <code>mkgridmap</code> utility.
 */
public interface VOMSCompatibility {

    /** 
     * Returns the DN of the users in the VOMS database. It is used by
     * mkgridmap++ to provide compatibility layer with the VO-LDAP database.
     * This method is equivalent to calling {@link #getGridmapUsers(String)
     * getGridmapUsers(String)} with the VO group name as its parameter.
     *
     * @return list of DNs
     */
    public String[] getGridmapUsers() throws VOMSException;

    /** 
     * Returns the DN of the users who have the given container in the VOMS
     * database. It is used by mkgridmap++ to provide compatibility layer with
     * the VO-LDAP database.
     *
     * @param container A fully qualified container name.
     * @return list of DNs
     */
    public String[] getGridmapUsers (String container) throws VOMSException;

    /**
     * Returns the major version number.
     */
    public int getMajorVersionNumber();
    /**
     * Returns the minor version number.
     */
    public int getMinorVersionNumber();
    /**
     * Returns the patch version number.
     */
    public int getPatchVersionNumber();
}

// Please do not change this line. 
// arch-tag: 32238025-6335-4663-8ea3-d33eff847d7a 

