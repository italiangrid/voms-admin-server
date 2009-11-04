package it.infn.cnaf.voms.x509;

import javax.servlet.http.HttpServlet;


public abstract class BaseServlet extends HttpServlet{

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    protected int parseInt(String intString){
        
        return new Integer(intString).intValue();
    }
    
    protected long parseLong(String longString){
        
        return new Long(longString).longValue();
    }

}
