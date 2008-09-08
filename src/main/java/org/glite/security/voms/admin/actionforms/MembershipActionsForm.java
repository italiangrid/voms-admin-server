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
package org.glite.security.voms.admin.actionforms;

import org.apache.struts.action.ActionForm;

public class MembershipActionsForm extends ActionForm {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    Long userId;

    Long groupId;

    Long roleId;

    public MembershipActionsForm() {

        super();
        // TODO Auto-generated constructor stub
    }

    public Long getGroupId() {

        return groupId;
    }

    public void setGroupId( Long groupId ) {

        this.groupId = groupId;
    }

    public Long getRoleId() {

        return roleId;
    }

    public void setRoleId( Long roleId ) {

        this.roleId = roleId;
    }

    public Long getUserId() {

        return userId;
    }

    public void setUserId( Long userId ) {

        this.userId = userId;
    }

    public String toString() {

        return "u:" + userId + ",g:" + groupId + ",r:" + roleId;
    }

}
