package org.glite.security.voms.admin.actions.admins;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.glite.security.voms.admin.actions.BaseAction;
import org.glite.security.voms.admin.dao.VOMSAdminDAO;
import org.glite.security.voms.admin.model.VOMSAdmin;


public class LoadAdminsActions extends BaseAction {
    
    @Override
    public ActionForward execute( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception {
    
        // Put the non-internal voms-admins in the requests
        // FIXME: do this with an operation
        List<VOMSAdmin> admins = VOMSAdminDAO.instance().getNonInternalAdmins();
        
        request.setAttribute( "admins", admins );
        
        List<VOMSAdmin> tags = VOMSAdminDAO.instance().getTagAdmins();
        
        request.setAttribute( "tags", tags);
        
        return findSuccess( mapping );
    }

}
