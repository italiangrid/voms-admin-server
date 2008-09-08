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
package org.glite.security.voms.admin.actions.user;

import java.util.List;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.glite.security.voms.admin.actions.BaseAction;
import org.glite.security.voms.admin.dao.VOMSCADAO;
import org.glite.security.voms.admin.dao.VOMSGroupDAO;
import org.glite.security.voms.admin.model.VOMSCA;
import org.glite.security.voms.admin.model.VOMSGroup;
import org.glite.security.voms.admin.model.VOMSRole;
import org.glite.security.voms.admin.model.VOMSUser;
import org.glite.security.voms.admin.operations.groups.AddMemberOperation;
import org.glite.security.voms.admin.operations.groups.CreateGroupOperation;
import org.glite.security.voms.admin.operations.roles.CreateRoleOperation;
import org.glite.security.voms.admin.operations.users.AssignRoleOperation;
import org.glite.security.voms.admin.operations.users.CreateUserOperation;


public class CreateRandomUsersAction extends BaseAction {

	private static final int NUM_USERS = 50;

	private static final int NUM_GROUPS = 5;

	private static final int NUM_ROLES = 5;

	private static final String CA = "/C=IT/ST=Test/L=Bologna/O=Voms-Admin/OU=Voms-Admin testing/CN=Test CA";

	public ActionForward execute(
			ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response) throws Exception {

        List cas = VOMSCADAO.instance().getValid();
        
        VOMSCA[] caArray = (VOMSCA[]) cas.toArray( new VOMSCA[cas.size()] );
        
        
		Random r = new Random();
		VOMSGroup voGroup = VOMSGroupDAO.instance().getVOGroup();

		VOMSGroup[] groups = new VOMSGroup[NUM_GROUPS];
		VOMSRole[] roles = new VOMSRole[NUM_ROLES];

		for (int i = 0; i < NUM_GROUPS; i++) {
			String groupName = voGroup.getName() + "/g" + i;

			groups[i] = (VOMSGroup) CreateGroupOperation.instance(groupName)
					.execute(); 

		}

		for (int i = 0; i < NUM_ROLES; i++) {

			roles[i] = (VOMSRole) CreateRoleOperation.instance("R" + i).execute();

		}

		
		for (int i = 0; i < NUM_USERS; i++) {

            
			String username = "user-" + i+"_";
            
            for (int iii=0; iii < r.nextInt( 12 ); iii++){
                username = username+new Character((char)(65+ r.nextInt( 57 )));
            }
			String email = username + "@test.org";
            
			int caIndex = r.nextInt( caArray.length );

			VOMSUser u = (VOMSUser) CreateUserOperation.instance(username, caArray[caIndex].getSubjectString(), null,
                    null, email).execute(); 
                               
			for (int j = 0; j < r.nextInt(NUM_GROUPS); j++) {

                AddMemberOperation.instance(u,groups[j]).execute();
				
				for (int k = 0; k < r.nextInt(NUM_ROLES); k++)
                    AssignRoleOperation.instance(u,groups[j],roles[k]).execute();
			}
		}

		return findSuccess(mapping);
	}

}
