package org.glite.security.voms.admin.actions.aup;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.glite.security.voms.admin.actionforms.aup.AUPActionForm;
import org.glite.security.voms.admin.actions.BaseAction;
import org.glite.security.voms.admin.common.VOMSException;
import org.glite.security.voms.admin.dao.generic.AUPDAO;
import org.glite.security.voms.admin.dao.generic.DAOFactory;
import org.glite.security.voms.admin.model.AUP;


public class LoadAUPAction extends BaseAction {

    @Override
    public ActionForward execute( ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response )
            throws Exception {
    
        AUPActionForm f = (AUPActionForm)form;
        
        if (f == null)
            throw new VOMSException("No form found in context!");
            
        if (f.getId() == null)
            throw new VOMSException("Cannot load a AUP without a valid 'id'!");
        
        AUPDAO dao = DAOFactory.instance().getAUPDAO();
        
        AUP aup = dao.findById( f.getId(), false );
        
        request.setAttribute( "aup", aup );
        
        return findSuccess( mapping );
    }    
}
