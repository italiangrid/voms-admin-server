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
package org.glite.security.voms.admin.actions.attributes;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.glite.security.voms.admin.actionforms.AttributeDescriptionForm;
import org.glite.security.voms.admin.actions.BaseDispatchAction;
import org.glite.security.voms.admin.dao.VOMSAttributeDAO;
import org.glite.security.voms.admin.operations.attributes.CreateAttributeDescriptionOperation;
import org.glite.security.voms.admin.operations.attributes.DeleteAttributeDescriptionOperation;

public class AttributeDescriptionActions extends BaseDispatchAction {

	public ActionForward create(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		AttributeDescriptionForm aForm = (AttributeDescriptionForm)form;
		
		CreateAttributeDescriptionOperation.instance(aForm.getAttributeName(), aForm.getAttributeDescription(), aForm.getAttributeUnique()).execute();
		
		List attributeDescriptions = VOMSAttributeDAO.instance().getAllAttributeDescriptions();
		request.setAttribute("attributeDescriptions", attributeDescriptions);
		
		aForm.reset(mapping, request);
		return findSuccess(mapping);
	}
	
	public ActionForward delete(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		AttributeDescriptionForm aForm = (AttributeDescriptionForm)form;
		
		DeleteAttributeDescriptionOperation.instance(aForm.getAttributeName()).execute();
		List attributeDescriptions = VOMSAttributeDAO.instance().getAllAttributeDescriptions();
		request.setAttribute("attributeDescriptions", attributeDescriptions);
		
		aForm.reset(mapping, request);
		return findSuccess(mapping);
		
	}
	
	public ActionForward list(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception{
		
		return findSuccess(mapping);
	}
	
	public ActionForward update(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception{
		
		return findSuccess(mapping);
	}
}
