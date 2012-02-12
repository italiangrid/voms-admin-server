package org.glite.security.voms.admin.util;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import org.glite.security.voms.admin.configuration.VOMSConfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SysconfigUtil {

	public static Logger log = LoggerFactory.getLogger(SysconfigUtil.class);
	
	public static final String SYSCONFIG_FILE = "/etc/sysconfig/voms-admin";
	public static final String SYSCONFIG_CONF_DIR = "CONF_DIR";
	public static final String SYSCONFIG_DEFAULT_VO_NAME = "DEFAULT_VO_NAME";
	
	public synchronized static Properties loadSysconfig(){
		
		Properties packagingProps = new Properties();
		
		String sysconfigFilePath=SYSCONFIG_FILE;
		
		try {
			packagingProps.load(SysconfigUtil.class.getClassLoader().getResourceAsStream("packaging.properties"));
			
			String prefix = packagingProps.getProperty("package.prefix");
			
			if (prefix != null){
				
				sysconfigFilePath = String.format("%s/etc/sysconfig/voms-admin", prefix);
				log.info("SYSCONFIG file: {}", sysconfigFilePath);
				
			}else{
				
				log.warn("Packaging properties do not specify package.prefix property...using default value for sysconfig location");
			}
		
		} catch (IOException e1) {
			log.warn("Packaging properties not found in classloader... using default value for sysconfig location");
		}
		
		FileReader sysconfigReader;
		
		Properties props = new Properties();
		
		try {
			
			sysconfigReader = new FileReader(sysconfigFilePath);
			props.load(sysconfigReader);
			
			return props;
		
		}catch (FileNotFoundException e) {
			
			log.error("Error opening VOMS Admin system configuration file "+ sysconfigFilePath);
			throw new VOMSConfigurationException("Error opening VOMS Admin system configuration file "+sysconfigFilePath, e);
		
		} catch (IOException e) {
			log.error("Error parsing VOMS Admin system configuration file "+ SYSCONFIG_FILE);
			throw new VOMSConfigurationException("Error parsing VOMS Admin system configuration file "+ sysconfigFilePath, e);
		}
	}
	
	
}
