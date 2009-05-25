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

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.axis.utils.DOM2Writer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.glite.security.voms.admin.actionforms.UIActionForm;
import org.glite.security.voms.admin.common.VOMSServiceConstants;
import org.w3c.dom.Document;



public class UIActions extends BaseDispatchAction {

    private static final Log log = LogFactory.getLog( UIActions.class );

    protected void writeXMLResponse( HttpServletResponse response, String elementName, Boolean status )
            
        throws ParserConfigurationException , FactoryConfigurationError, IOException {

        DocumentBuilderFactory factory= DocumentBuilderFactory.newInstance(); 
        factory.setNamespaceAware(false);
        factory.setValidating(false);
        
        Document doc = factory.newDocumentBuilder().getDOMImplementation().createDocument("http://c","panel",null);
                
        doc.getDocumentElement().setAttribute("id",elementName);
        
        if (status == null)
            doc.getDocumentElement().setAttribute("status","null");
        else
            doc.getDocumentElement().setAttribute("status",status.toString());
        
        response.getWriter().write(DOM2Writer.nodeToString(doc,true));
        

    }

    public ActionForward storeStatus( ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response ) {

        HttpSession session = request.getSession();

        Map statusMap = (Map) session.getAttribute( VOMSServiceConstants.STATUS_MAP_KEY );

        if ( statusMap == null )
            statusMap = new HashMap();

        UIActionForm uForm = (UIActionForm) form;

        log.debug( "uForm: " + uForm );

        statusMap.put( uForm.getPanelId(), uForm.getStatus() );

        session.setAttribute( VOMSServiceConstants.STATUS_MAP_KEY, statusMap );

        request.setAttribute("panelId",uForm.getPanelId());
        request.setAttribute("panelStatus", (uForm.getStatus() == null) ? "null": uForm.getStatus().toString());
        
        return findSuccess( mapping );
    }

    public ActionForward getStatus( ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response )
            throws IOException, ParserConfigurationException, FactoryConfigurationError {

        HttpSession session = request.getSession();

        Map statusMap = (Map) session.getAttribute( VOMSServiceConstants.STATUS_MAP_KEY );

        if ( statusMap == null )

            statusMap = new HashMap();

        UIActionForm uForm = (UIActionForm) form;

        log.debug( "uForm: " + uForm );

        Boolean status = (Boolean) statusMap.get( uForm.getPanelId() );

        request.setAttribute("panelId",uForm.getPanelId());
        request.setAttribute("panelStatus", (status == null) ? "null": status.toString());
        
        return findSuccess(mapping);
    }
    
    public ActionForward hasPermissions(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    		
    		return findSuccess(mapping);
    }

}
