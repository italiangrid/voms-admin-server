package org.glite.security.voms.admin.integration;

import org.glite.security.voms.admin.configuration.VOMSConfiguration;

public abstract class AbstractPluginConfigurator implements PluginConfigurator{
	
	private String pluginName = "<undefined>";
	
	
	/**
	 * @return the pluginName
	 */
	public String getPluginName() {
		return pluginName;
	}

	/**
	 * @param pluginName the pluginName to set
	 */
	public void setPluginName(String pluginName) {
		this.pluginName = pluginName;
	}

	
	public String getPluginProperty(String propertyName, String defaultValue){
		
		return VOMSConfiguration.instance().getExternalValidatorProperty(pluginName, propertyName,defaultValue);
	}
	
	public String getPluginProperty(String propertyName){
		
		return VOMSConfiguration.instance().getExternalValidatorProperty(pluginName, propertyName);
		
	}
	public String getVomsConfigurationDirectoryPath(){
		
		return VOMSConfiguration.instance().getConfigurationDirectoryPath();
	}
	
}
