package org.glite.security.voms.admin.actions.ca;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.glite.security.voms.admin.actionforms.SearchForm;
import org.glite.security.voms.admin.actions.BaseDispatchAction;
import org.glite.security.voms.admin.dao.VOMSCADAO;


public class CAActions extends BaseDispatchAction {

    public ActionForward load( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception {
    
        List knownCAs = VOMSCADAO.instance().getValid();
        
        request.setAttribute( "caList", knownCAs );
        
        return mapping.findForward( "list-cas" );
    }
    
    @Override
    public ActionForward execute( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception {
    
        return load(mapping,form, request,response);
    }
}
