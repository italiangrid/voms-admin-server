package org.glite.security.voms.admin.core.tasks;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.glite.security.voms.admin.configuration.VOMSConfiguration;
import org.glite.security.voms.admin.configuration.VOMSConfigurationConstants;
import org.glite.security.voms.admin.persistence.dao.VOMSUserDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class UserStatsTask implements Runnable {
	
	public static final Logger logger = LoggerFactory.getLogger(UserStatsTask.class);
	
	public static final String DEFAULT_USER_STATS_BASE_PATH = "/var/tmp/voms-admin";
	public static final String DEFAULT_USER_STATS_FILENAME = "stats.properties";
	
	public static final Long DEFAULT_PERIOD_IN_SECONDS = TimeUnit.MINUTES.toSeconds(60);
	
	Properties monitoredProperties;
	
	public UserStatsTask() {
		
		createStatsDir();
		monitoredProperties = new Properties();
		
	}
	
	void storeProperties(){
	
		try {
			FileOutputStream fos = new FileOutputStream(getStatsFileName());
			monitoredProperties.store(fos, null);
			fos.close();
		
		} catch (IOException e) {
			logger.error("Couldn't write user stats file: {}", e.getMessage(), e);
			createStatsDir();
		}
		
	}
	
	String getStatsDirPath(){
		String statsDirPath = VOMSConfiguration.instance().getString(VOMSConfigurationConstants.MONITORING_USER_STATS_BASE_PATH, DEFAULT_USER_STATS_BASE_PATH);
		String statsDir = String.format("%s/%s", statsDirPath, VOMSConfiguration.instance().getVOName());
		return statsDir;
	}
	
	String getStatsFileName(){
		
		return String.format("%s/%s", getStatsDirPath(), DEFAULT_USER_STATS_FILENAME);
		
	}
	
	void createStatsDir(){
		
		File statsDirFile = new File(getStatsDirPath());
		
		if (!statsDirFile.exists())
			statsDirFile.mkdirs();
		
	}
	
	public void run() {
		
		VOMSUserDAO dao = VOMSUserDAO.instance();
		
		Long expiredUsersCount = dao.countExpiredUsers();
		Long usersCount = dao.countUsers();
		Long suspendedUsersCount = dao.countSuspendedUsers();
				
		monitoredProperties.put("usersCount", usersCount.toString());
		monitoredProperties.put("expiredUsersCount", expiredUsersCount.toString());
		monitoredProperties.put("suspendUsersCount", suspendedUsersCount.toString());
		
		storeProperties();
		
	}

}
