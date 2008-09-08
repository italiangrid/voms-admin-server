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

import java.io.Serializable;
import java.util.Date;

public abstract class History implements Serializable {

    protected History() {

    }

    protected History( short operation, Date timestamp, VOMSAdmin who ) {

        this.operation = new Short( operation );
        this.timestamp = timestamp;
        this.who = who;
    }

    Long pkID;

    Short operation;

    Date timestamp;

    VOMSAdmin who;

    /**
     * @return Returns the pkID.
     */
    public Long getPkID() {

        return pkID;
    }

    /**
     * @param pkID
     *            The pkID to set.
     */
    public void setPkID( Long pkID ) {

        this.pkID = pkID;
    }

    /**
     * @return Returns the operation.
     */
    public Short getOperation() {

        return operation;
    }

    /**
     * @param operation
     *            The operation to set.
     */
    public void setOperation( Short operation ) {

        this.operation = operation;
    }

    /**
     * @return Returns the timestamp.
     */
    public Date getTimestamp() {

        return timestamp;
    }

    /**
     * @param timestamp
     *            The timestamp to set.
     */
    public void setTimestamp( Date timestamp ) {

        this.timestamp = timestamp;
    }

    /**
     * @return Returns the who.
     */
    public VOMSAdmin getWho() {

        return who;
    }

    /**
     * @param who
     *            The who to set.
     */
    public void setWho( VOMSAdmin admin ) {

        this.who = admin;
    }

}
