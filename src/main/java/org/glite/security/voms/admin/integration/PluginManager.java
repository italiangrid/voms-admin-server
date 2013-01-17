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

package org.glite.security.voms.admin.integration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.glite.security.voms.admin.configuration.VOMSConfiguration;
import org.glite.security.voms.admin.configuration.VOMSConfigurationConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PluginManager {

	public static final Logger log = LoggerFactory.getLogger(PluginManager.class);
	
	private static PluginManager INSTANCE;
	
	private Map<String, PluginConfigurator> configuredPluginsMap;
	
	
	private PluginManager(){
		
		configuredPluginsMap = new HashMap<String, PluginConfigurator>();
	}
	
	public static final PluginManager instance(){
		
		if (INSTANCE==null)
			INSTANCE = new PluginManager();
		
		return INSTANCE;
	}
	
	
	
	public void configurePlugins(){
		
		log.debug("Configuring external validation plugins.");
		
		boolean registrationEnabled = VOMSConfiguration.instance().getBoolean(
				VOMSConfigurationConstants.REGISTRATION_SERVICE_ENABLED, true);
		
		if (!registrationEnabled){
			log.info("Plugin configuration will be skipped since registration is DISABLED for this VO");
			return;
		}
		
		
		VOMSConfiguration conf = VOMSConfiguration.instance();	
		List<String> activePlugins = conf.getExternalValidators();
		
		if (activePlugins == null || activePlugins.isEmpty()){
			log.debug("No external validation plugins found to configure.");
			return;
		}
		
		for (String pluginName: activePlugins){
			
			String pluginConfClassName = conf.getExternalValidatorConfigClass(pluginName);
			if (pluginConfClassName == null || "".equals(pluginConfClassName.trim())){
				log.error("No configuration class defined for plugin '{}'. Please specify a class using the '{}' property in the service.properties file.");
				continue;
			}
			
			try {
				
				Class<?> confClazz = Class.forName(pluginConfClassName);
				
				// I could have used a constructor method to pass down the pluginName
				// to the actual plugin configurator, but the reflection constructor code
				// is not somethin I really love...
				PluginConfigurator pluginConfigurator = (PluginConfigurator) confClazz.newInstance();
				
				pluginConfigurator.setPluginName(pluginName);
				
				pluginConfigurator.configure();
					 
				configuredPluginsMap.put(pluginConfClassName, pluginConfigurator);
			
			}catch(VOMSPluginConfigurationException e){
				
				log.error("Error configuring '{}' plugin: {}", pluginName, e.getMessage());
				log.error(e.getMessage(),e);
				
				log.error("{} plugin configuration ABORTED.", pluginName);
				continue;
				
			}catch (Exception e) {
				log.error("Cannot instantiate the configuration class '{}' for plugin '{}'", new String[]{pluginConfClassName,pluginName});
				log.error(e.getMessage(),e);
				log.error("{} plugin configuration ABORTED.", pluginName);
				continue;
			} 
			
			
			log.info("'{}' plugin configured SUCCESSFULLY.", pluginName);
		}
		
		log.debug("External validation plugin configuration done.");
		
	}
	
	public PluginConfigurator getConfiguredPlugin(String pluginConfigurator){
		return configuredPluginsMap.get(pluginConfigurator);
	}
	
}
