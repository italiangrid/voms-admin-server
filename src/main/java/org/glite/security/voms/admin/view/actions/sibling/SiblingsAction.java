package org.glite.security.voms.admin.view.actions.sibling;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.glite.security.voms.admin.common.VOMSConfiguration;
import org.glite.security.voms.admin.view.actions.BaseAction;

import com.opensymphony.xwork2.Preparable;

@ParentPackage("base")
@Result(name=BaseAction.SUCCESS, location="siblings")
public class SiblingsAction extends BaseAction implements Preparable{
	
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
		configuredVOs = VOMSConfiguration.instance().getLocallyConfiguredVOs();
		CollectionUtils.transform(configuredVOs, new VONameTransformer(getAllVOsBaseURL())); 		
	}
	
	
}
