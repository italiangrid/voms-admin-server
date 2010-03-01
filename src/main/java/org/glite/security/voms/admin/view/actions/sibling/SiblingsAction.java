/**
 * Copyright (c) Members of the EGEE Collaboration. 2006-2009.
 * See http://www.eu-egee.org/partners/ for details on the copyright holders.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Authors:
 * 	Andrea Ceccanti (INFN)
 */
package org.glite.security.voms.admin.view.actions.sibling;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.glite.security.voms.admin.configuration.VOMSConfiguration;
import org.glite.security.voms.admin.view.actions.BaseAction;

import com.opensymphony.xwork2.Preparable;

@ParentPackage("base")
@Result(name=BaseAction.SUCCESS, location="siblings")
public class SiblingsAction extends BaseAction implements Preparable{
	
	
	public static Logger log = LoggerFactory.getLogger(SiblingsAction.class);
	
	class VONameTransformer implements Transformer{
        String prefix;
        
        
        public VONameTransformer(String prefix) {
            this.prefix = prefix;
        }


        public Object transform( Object obj ) {
            return (String)prefix+(String)obj;
        }
        
    }


	List<String> configuredVOs;

	public List<String> getConfiguredVOs() {
		return configuredVOs;
	}

	public void setConfiguredVOs(List<String> configuredVOs) {
		this.configuredVOs = configuredVOs;
	}

	public void prepare() throws Exception {
		List<String> vos = VOMSConfiguration.instance().getLocallyConfiguredVOs();
		log.info("Locally configured vos: " + configuredVOs);
		CollectionUtils.transform(vos, new VONameTransformer(getAllVOsBaseURL()));
		setConfiguredVOs(vos);
		
	}
	
	
}
