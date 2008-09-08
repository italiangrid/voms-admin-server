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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.glite.security.voms.admin.actionforms.SearchForm;
import org.glite.security.voms.admin.actions.BaseAction;
import org.glite.security.voms.admin.common.VOMSConfiguration;
import org.glite.security.voms.admin.dao.SearchResults;
import org.glite.security.voms.admin.dao.VOMSCADAO;
import org.glite.security.voms.admin.operations.users.SearchUsersOperation;




public class SearchUserAction extends BaseAction {

	private static final Log log = LogFactory.getLog(SearchUserAction.class);

	public ActionForward execute(
			ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		int resultsPerPage = VOMSConfiguration.instance().getInt(
				VOMSConfiguration.USER_MAX_RESULTS_PER_PAGE, 15);

		SearchForm sForm = (SearchForm) form;
		log.debug("sForm: " + sForm);

		SearchResults results = (SearchResults) SearchUsersOperation.instance(
				sForm.getText(), sForm.getFirstResults(), resultsPerPage)
				.execute();

		log.debug("results: " + results);
		storeResults(request, results);

		// Load CAs into org.glite.security.voms.admin.request for user creation pane!
		
		List caList = VOMSCADAO.instance().getValid();
		request.setAttribute("caList", caList);

		sForm.reset(mapping, request);

		return findSuccess(mapping);

	}

}
