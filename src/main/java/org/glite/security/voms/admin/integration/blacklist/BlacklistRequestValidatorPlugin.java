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

package org.glite.security.voms.admin.integration.blacklist;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.glite.security.voms.admin.configuration.VOMSConfiguration;
import org.glite.security.voms.admin.integration.AbstractPluginConfigurator;
import org.glite.security.voms.admin.integration.RequestValidator;
import org.glite.security.voms.admin.integration.VOMSPluginConfigurationException;
import org.glite.security.voms.admin.integration.ValidationManager;
import org.glite.security.voms.admin.integration.ValidationResult;
import org.glite.security.voms.admin.persistence.model.request.NewVOMembershipRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BlacklistRequestValidatorPlugin extends AbstractPluginConfigurator implements
		RequestValidator<NewVOMembershipRequest>{

	Logger log = LoggerFactory.getLogger(BlacklistRequestValidatorPlugin.class);
	
	private List<String> blacklistedDns;
	
	public ValidationResult validateRequest(NewVOMembershipRequest r) {
		
		String subject = r.getRequesterInfo().getCertificateSubject();
		
		if (blacklistedDns.contains(subject)){
			return ValidationResult.failure("User with dn '"+subject+"' has been blacklisted!");
		}
				
		
		return ValidationResult.success();
			
	}

	
	
	private void parseBlacklist() throws VOMSPluginConfigurationException {
		
		String confDir = VOMSConfiguration.instance().getConfigurationDirectoryPath();
		
		File f = new File(confDir+File.separator+"blacklist");
		
		try {
			BufferedReader reader = new BufferedReader(new FileReader(f));
			
			blacklistedDns = new ArrayList<String>();
			
			String line = null;
			
			do{
				line = reader.readLine();
				if (line != null){
					log.info("Adding {} to the list of blacklisted DNs", line);
					blacklistedDns.add(line);
				}
				
			}while (line != null);
			
			
			reader.close();
			
		} catch (FileNotFoundException e) {
			
			throw new VOMSPluginConfigurationException(e);
		
		} catch (IOException e) {
			
			throw new VOMSPluginConfigurationException(e);
			
		}
		
	}
	
	
	public void configure() throws VOMSPluginConfigurationException {
		
		parseBlacklist();
		
		ValidationManager.instance().registerRequestValidator(this);
		log.info("Blacklist plugin configured correctly");
		
			
		
	}

	

}
