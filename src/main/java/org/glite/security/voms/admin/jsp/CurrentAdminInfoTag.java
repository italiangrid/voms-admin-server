/*******************************************************************************
 *Copyright (c) Members of the EGEE Collaboration. 2006. 
 *See http://www.eu-egee.org/partners/ for details on the copyright
 *holders.  
 *
 *Licensed under the Apache License, Version 2.0 (the "License"); 
 *you may not use this file except in compliance with the License. 
 *You may obtain a copy of the License at 
 *
 *    http://www.apache.org/licenses/LICENSE-2.0 
 *
 *Unless required by applicable law or agreed to in writing, software 
 *distributed under the License is distributed on an "AS IS" BASIS, 
 *WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 *See the License for the specific language governing permissions and 
 *limitations under the License.
 *
 * Authors:
 *     Andrea Ceccanti - andrea.ceccanti@cnaf.infn.it
 *******************************************************************************/
package org.glite.security.voms.admin.jsp;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.TagSupport;

import org.glite.security.voms.admin.operations.CurrentAdmin;



public class CurrentAdminInfoTag extends TagSupport {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;


    String var;
    
    public CurrentAdminInfoTag() {

        super();
        // TODO Auto-generated constructor stub
    }

    public int doStartTag() throws JspException {

        CurrentAdmin admin = CurrentAdmin.instance();
        try {

            if (var != null){
                
                pageContext.getRequest().setAttribute( var, admin);
                
            }else{
            
                if ( admin == null )
                    pageContext.getOut().print( "unknown admin" );
                else
                    pageContext.getOut().print( admin.getRealSubject() );
            }
            
            
        } catch ( IOException e ) {
            throw new JspTagException( "Error writing CurrentAdminInfo!" );
        }

        return SKIP_BODY;
    }

    
    public String getVar() {
    
        return var;
    }

    
    public void setVar( String var ) {
    
        this.var = var;
    }

    
}
