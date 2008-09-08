package org.glite.security.voms.admin.actions.configuration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.glite.security.voms.admin.actions.BaseAction;
import org.glite.security.voms.admin.common.VOMSConfiguration;


public class LoadConfigurationAction extends BaseAction {

    @Override
    public ActionForward execute( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception {
    
        VOMSConfiguration conf = VOMSConfiguration.instance();
        
        // Put configuration info inside the request, so that the jsp can display it
        String vomsesConf =  conf.getVomsesConfigurationString();
        request.setAttribute( "vomsesConf", vomsesConf );
             
        // Build base URL for configuration interface
        String baseURL = request.getScheme() + "://"
        + request.getServerName() + ":" 
        + request.getServerPort() + "/voms/" + conf.getVOName();
        
        request.setAttribute("vomsAdminBaseURL", baseURL);
        
        // Build example mkgridmap configuration
        String mkgridmapConf = "group vomss://"
            + request.getServerName() + ":" 
            + request.getServerPort() + "/voms/" + conf.getVOName()
            + "   ."+conf.getVOName();
        
        request.setAttribute("mkgridmapConf", mkgridmapConf);
        
        return findSuccess( mapping );
    }
}
