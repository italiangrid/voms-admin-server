package org.glite.security.voms.admin.actions.ca;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.glite.security.voms.admin.actionforms.SearchForm;
import org.glite.security.voms.admin.actions.BaseAction;
import org.glite.security.voms.admin.dao.SearchResults;
import org.glite.security.voms.admin.dao.VOMSCADAO;


public class SearchCAsAction extends BaseAction {
    
    private static final Log log = LogFactory.getLog( SearchCAsAction.class );
    
    @Override
    public ActionForward execute( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception {
    
        if (log.isDebugEnabled()){
            
            log.debug( ToStringBuilder.reflectionToString( form ) );
        }
        
        SearchForm sForm = (SearchForm)form;
        
        
        SearchResults results = VOMSCADAO.instance().search( sForm.getText(),
                sForm.getFirstResults(), 15);
        
        storeResults( request, results );
        
        sForm.reset( mapping, request );
        
        return findSuccess( mapping );
    }

}
