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
package org.glite.security.voms.admin.actions.siblings;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.glite.security.voms.admin.actions.BaseAction;
import org.glite.security.voms.admin.common.VOMSConfiguration;


public class LoadConfiguredVOsAction extends BaseAction{
    
    class VONameTransformer implements Transformer{
        String prefix;
        
        
        public VONameTransformer(String prefix) {
            this.prefix = prefix;
        }


        public Object transform( Object obj ) {
            return (String)prefix+(String)obj;
        }
        
    }
    
    public ActionForward execute( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception {
    
        List configuredVOs = VOMSConfiguration.instance().getLocallyConfiguredVOs();
        
        String baseURL = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+"/voms/";
        
        CollectionUtils.transform( configuredVOs, new VONameTransformer(baseURL) );
        
        request.setAttribute( "configuredVOs", configuredVOs);
        
        return findSuccess( mapping );
    }

}
