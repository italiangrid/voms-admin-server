package org.glite.security.voms.admin.actions.admins;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.glite.security.voms.admin.actionforms.AdminTagForm;
import org.glite.security.voms.admin.actions.BaseDispatchAction;
import org.glite.security.voms.admin.dao.VOMSAdminDAO;
import org.glite.security.voms.admin.model.VOMSAdmin;


public class AdminTagsActions extends BaseDispatchAction {

    
    public ActionForward assignTag( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception {
        
        
        AdminTagForm aForm = (AdminTagForm)form;
        
        VOMSAdmin admin = VOMSAdminDAO.instance().getById( aForm.getAdminId() );
        VOMSAdmin tag = VOMSAdminDAO.instance().getById( aForm.getTagId() );
        
        //FIXME: Do this with an operation
        VOMSAdminDAO.instance().assignTag( admin, tag );
        
        return findSuccess( mapping );
    }

    
    public ActionForward removeTag( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception {
        
        AdminTagForm aForm = (AdminTagForm)form;
        
        VOMSAdmin admin = VOMSAdminDAO.instance().getById( aForm.getAdminId() );
        VOMSAdmin tag = VOMSAdminDAO.instance().getById( aForm.getTagId() );
        
        //FIXME: Do this with an operation
        VOMSAdminDAO.instance().removeTag( admin, tag );
        
        return findSuccess( mapping );
    }
    
}
