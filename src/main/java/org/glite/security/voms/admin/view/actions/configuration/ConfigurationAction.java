package org.glite.security.voms.admin.view.actions.configuration;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.glite.security.voms.admin.common.VOMSConfiguration;
import org.glite.security.voms.admin.view.actions.BaseAction;

import com.opensymphony.xwork2.Preparable;

@ParentPackage("base")
@Result(name=BaseAction.SUCCESS, location="configuration")
public class ConfigurationAction extends BaseAction implements Preparable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	String vomsesConf;
	
	String mkGridmapConf;
	
	String contactString;
	
	
	public void prepare() throws Exception {
		
		contactString = getBaseURL();
		
		vomsesConf = VOMSConfiguration.instance().getVomsesConfigurationString();
		
		HttpServletRequest request = ServletActionContext.getRequest();
		
		// Build mkgridmap configuration
		

        mkGridmapConf = "group vomss://"
            + request.getServerName() + ":" 
            + request.getServerPort() + "/voms/" + VOMSConfiguration.instance().getVOName()
            + "   ."+VOMSConfiguration.instance().getVOName();

	}

	public String getVomsesConf() {
		return vomsesConf;
	}

	public void setVomsesConf(String vomsesConf) {
		this.vomsesConf = vomsesConf;
	}

	public String getMkGridmapConf() {
		return mkGridmapConf;
	}

	public void setMkGridmapConf(String mkGridmapConf) {
		this.mkGridmapConf = mkGridmapConf;
	}

	public String getContactString() {
		return contactString;
	}

	public void setContactString(String contactString) {
		this.contactString = contactString;
	}
	

	
}
