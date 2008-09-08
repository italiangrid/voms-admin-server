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

package org.glite.security.voms.admin.model;

import java.util.Date;

import org.glite.security.voms.admin.operations.VOMSPermission;


public class VOMSAdminHistory extends History {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    Long id;

    String dn;

    VOMSCA ca;

    VOMSPermission permissions;

    public VOMSAdminHistory() {

    }

    public VOMSAdminHistory( short operation, Date timestamp, VOMSAdmin who ) {

        super( operation, timestamp, who );
    }

    /**
     * @return Returns the id.
     */
    public Long getId() {

        return id;
    }

    /**
     * @param id
     *            The id to set.
     */
    public void setId( Long id ) {

        this.id = id;
    }

    /**
     * @return Returns the dn.
     */
    public String getDn() {

        return dn;
    }

    /**
     * @param dn
     *            The dn to set.
     */
    public void setDn( String dn ) {

        this.dn = dn;
    }

    /**
     * @return Returns the ca.
     */
    public VOMSCA getCa() {

        return ca;
    }

    /**
     * @param ca
     *            The ca to set.
     */
    public void setCa( VOMSCA ca ) {

        this.ca = ca;
    }

    /**
     * @return Returns the permissions.
     */
    public VOMSPermission getPermissions() {

        return permissions;
    }

    /**
     * @param permissions
     *            The permissions to set.
     */
    public void setPermissions( VOMSPermission permissions ) {

        this.permissions = permissions;
    }

}
