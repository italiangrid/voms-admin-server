package org.glite.security.voms.admin.actions.aup;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.glite.security.voms.admin.actions.BaseAction;
import org.glite.security.voms.admin.dao.generic.AUPDAO;
import org.glite.security.voms.admin.dao.generic.DAOFactory;
import org.glite.security.voms.admin.model.AUP;


public class ListAUPsAction extends BaseAction {
    
    private static final Log log = LogFactory.getLog( ListAUPsAction.class );

    
    @Override
    public ActionForward execute( ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response )
            throws Exception {
        
        log.info(ListAUPsAction.class.getSimpleName()+".execute");
        
        AUPDAO dao  = DAOFactory.instance().getAUPDAO();
        
        List<AUP> aups = dao.findAll();
        
        request.setAttribute( "aups", aups );
    
        return findSuccess(mapping);
    }
    
    
}
