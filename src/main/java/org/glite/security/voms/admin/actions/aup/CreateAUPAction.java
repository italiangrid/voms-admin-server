package org.glite.security.voms.admin.actions.aup;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.glite.security.voms.admin.actionforms.aup.AUPActionForm;
import org.glite.security.voms.admin.actions.BaseAction;
import org.glite.security.voms.admin.dao.generic.AUPDAO;
import org.glite.security.voms.admin.dao.generic.DAOFactory;
import org.glite.security.voms.admin.model.AUP;

public class CreateAUPAction extends BaseAction {

    
    @Override
    public ActionForward execute( ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response )
            throws Exception {
    
        AUPActionForm f = (AUPActionForm)form;
        
        AUPDAO dao = DAOFactory.instance().getAUPDAO();
        
        AUP a = dao.createAUP( f.getName(), f.getDescription(), f.getVersion(), f.getUrl() );
                    
        message(request,"confirm.aup.creation",a.getName());
                
        return findSuccess( mapping );
        
    }
}
