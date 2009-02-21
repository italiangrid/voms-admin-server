package org.glite.security.voms.admin.actions.tags;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.glite.security.voms.admin.actions.BaseAction;
import org.glite.security.voms.admin.dao.VOMSAdminDAO;
import org.glite.security.voms.admin.model.VOMSAdmin;


public class LoadTagsAction extends BaseAction {

    public ActionForward execute( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception {
    
    
        
        return findSuccess( mapping );
    }
}
