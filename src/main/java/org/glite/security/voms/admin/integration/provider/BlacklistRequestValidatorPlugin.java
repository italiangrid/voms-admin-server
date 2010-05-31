package org.glite.security.voms.admin.integration.provider;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.glite.security.voms.admin.configuration.VOMSConfiguration;
import org.glite.security.voms.admin.integration.Configurable;
import org.glite.security.voms.admin.integration.RequestValidationException;
import org.glite.security.voms.admin.integration.RequestValidator;
import org.glite.security.voms.admin.integration.VOMSPluginConfigurationException;
import org.glite.security.voms.admin.integration.ValidationManager;
import org.glite.security.voms.admin.persistence.model.request.NewVOMembershipRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BlacklistRequestValidatorPlugin implements
		RequestValidator<NewVOMembershipRequest>, Configurable {

	Logger log = LoggerFactory.getLogger(BlacklistRequestValidatorPlugin.class);
	
	private List<String> blacklistedDns;
	
	public void validateRequest(NewVOMembershipRequest r) throws RequestValidationException{
		
		String subject = r.getRequesterInfo().getCertificateSubject();
		
		if (blacklistedDns.contains(subject))
			throw new RequestValidationException("User with dn '"+subject+"' has been blacklisted!");
			
	}

	public void validateRequests(List<NewVOMembershipRequest> requests) {
		// TODO Auto-generated method stub
		
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
		
		// Register plugin for request validation
		ValidationManager.instance().registerRequestValidator(this);
		log.info("Blacklist plugin configured correctly");
		
			
		
	}

	

}
