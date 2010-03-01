package org.glite.security.voms.admin.util;

import org.slf4j.Logger;

public class ExceptionUtils {
	
	
	public static void logError(Logger log, Throwable t){
		
		log.error(t.getMessage());
		if (log.isDebugEnabled())
			log.error(t.getMessage(),t);
		
	}
	
	public static void logWarning(Logger log, Throwable t){
		
		log.warn(t.getMessage());
		if (log.isDebugEnabled())
			log.warn(t.getMessage(),t);
		
	}

	public static void logInfo(Logger log, Throwable t){
		
		log.info(t.getMessage());
		if (log.isDebugEnabled())
			log.info(t.getMessage(),t);
	}

}
