package org.glite.security.voms.admin.actions.tags;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.glite.security.voms.admin.actionforms.TagForm;
import org.glite.security.voms.admin.actions.BaseDispatchAction;


public class TagActions extends BaseDispatchAction {

    public ActionForward load( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception {
        
        return findSuccess( mapping );
    }
    
    public ActionForward delete( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception {
    
        TagForm tForm = (TagForm)form;
        
        return findSuccess( mapping );
    }
    
    public ActionForward create( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception {
    
        TagForm tForm = (TagForm)form;
              
        return findSuccess( mapping );
    }
}
