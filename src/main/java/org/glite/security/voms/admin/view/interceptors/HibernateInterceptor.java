package org.glite.security.voms.admin.view.interceptors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.StrutsStatics;
import org.glite.security.voms.admin.database.HibernateFactory;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;


public class HibernateInterceptor extends AbstractInterceptor implements
        StrutsStatics {

    
    private static final Log log = LogFactory
            .getLog( HibernateInterceptor.class );
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @Override
    public String intercept( ActionInvocation ai) throws Exception {

    	
        String result = ai.invoke();
        
        try {

            HibernateFactory.commitTransaction();

        } finally {

            HibernateFactory.closeSession();

        }
        
        return result;
        
    }

}
