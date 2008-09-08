package org.glite.security.voms.admin.actions.tags;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.glite.security.voms.admin.actionforms.TagForm;
import org.glite.security.voms.admin.actions.BaseDispatchAction;
import org.glite.security.voms.admin.dao.VOMSAdminDAO;
import org.glite.security.voms.admin.database.AlreadyExistsException;
import org.glite.security.voms.admin.database.NoSuchTagException;
import org.glite.security.voms.admin.model.VOMSAdmin;


public class TagActions extends BaseDispatchAction {

    public ActionForward load( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception {
    
        List<VOMSAdmin> tagAdmins = VOMSAdminDAO.instance().getTagAdmins();
        
        request.setAttribute( "tags", tagAdmins);
        
        return findSuccess( mapping );
    }
    
    public ActionForward delete( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception {
    
        TagForm tForm = (TagForm)form;
        
        // Load admin to be deleted
        VOMSAdmin tagAdmin = VOMSAdminDAO.instance().getById( tForm.getId() );
        
        if (tagAdmin ==  null)
            throw new NoSuchTagException("No Tag admin found for id '"+tForm.getId());
        
        if (!tagAdmin.isTagAdmin())
            throw new NoSuchTagException("Admin found for id '"+tForm.getId()+"' is not a tag admin!");
        
        // Delete the admin!
        
        // FIXME: implement this with an operation
        VOMSAdminDAO.instance().delete( tagAdmin );
        
        return findSuccess( mapping );
    }
    
    public ActionForward create( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception {
    
        TagForm tForm = (TagForm)form;
        
        // Check if the admin is already there
        VOMSAdmin tagAdmin = VOMSAdminDAO.instance().getFromTag( tForm.getName() );
        
        if (tagAdmin != null)
            throw new AlreadyExistsException("Tag '"+tagAdmin.getDn()+"' already exists!");
        
        VOMSAdminDAO.instance().createFromTag( tForm.getName() );
        
        return findSuccess( mapping );
    }
}
