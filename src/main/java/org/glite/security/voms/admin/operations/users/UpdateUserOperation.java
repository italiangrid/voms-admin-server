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

import org.glite.security.voms.admin.actionforms.UserForm;
import org.glite.security.voms.admin.dao.VOMSUserDAO;
import org.glite.security.voms.admin.model.VOMSUser;
import org.glite.security.voms.admin.operations.BaseVoRWOperation;


public class UpdateUserOperation extends BaseVoRWOperation{

    VOMSUser user;
    UserForm form;

    private UpdateUserOperation( VOMSUser u ) {

        user = u;

    }

    private UpdateUserOperation( VOMSUser u, UserForm uf ) {

        user = u;
        form = uf;

    }
    
    public Object doExecute() {
        
        if (form != null){
            
            // user.setCn(form.getCn());
            user.setEmailAddress(form.getEmailAddress());
            
        }

        VOMSUserDAO.instance().update( user );
        return null;
    }

    public static UpdateUserOperation instance( VOMSUser usr ) {

        return new UpdateUserOperation( usr ) ;
    }
    
    public static UpdateUserOperation instance(VOMSUser usr, UserForm form){
        
        return new UpdateUserOperation(usr, form);
    }

}
