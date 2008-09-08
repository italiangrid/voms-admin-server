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
package org.glite.security.voms.admin.operations.users;

import org.glite.security.voms.User;
import org.glite.security.voms.admin.dao.VOMSUserDAO;
import org.glite.security.voms.admin.database.NoSuchUserException;
import org.glite.security.voms.admin.model.VOMSUser;
import org.glite.security.voms.admin.operations.BaseVomsOperation;
import org.glite.security.voms.admin.operations.VOMSContext;
import org.glite.security.voms.admin.operations.VOMSPermission;
import org.glite.security.voms.service.attributes.AttributeValue;

public class SetUserAttributeOperation extends BaseVomsOperation {

    VOMSUser user;

    String attributeName;

    String attributeDescription;

    String attributeValue;

    private SetUserAttributeOperation( VOMSUser u, String aName, String aDesc,
            String aValue ) {

        user = u;
        attributeName = aName;
        attributeDescription = aDesc;
        attributeValue = aValue;
    }

    public Object doExecute() {

        return VOMSUserDAO.instance().setAttribute( user, attributeName,
                attributeValue );

    }

    public static SetUserAttributeOperation instance( VOMSUser u, String aName,
            String aDesc, String aValue ) {

        return new SetUserAttributeOperation( u, aName, aDesc, aValue );
    }

    public static SetUserAttributeOperation instance( User user,
            AttributeValue val ) {

        VOMSUser u = (VOMSUser) FindUserOperation.instance( user.getDN(),
                user.getCA() ).execute();

        if (u == null)
            throw new NoSuchUserException("User '"+user.getDN()+","+user.getCA()+"' not found in this vo.");
        
        return new SetUserAttributeOperation( u, val.getAttributeClass()
                .getName(), val.getAttributeClass().getDescription(), val
                .getValue() );

    }

    protected void setupPermissions() {

        addRequiredPermission( VOMSContext.getVoContext(), VOMSPermission
                .getAttributesRWPermissions() );
    }

}
