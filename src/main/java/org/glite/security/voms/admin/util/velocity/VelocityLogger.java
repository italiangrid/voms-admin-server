package org.glite.security.voms.admin.util.velocity;

import org.apache.velocity.runtime.RuntimeServices;
import org.apache.velocity.runtime.log.LogChute;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VelocityLogger implements LogChute {

	public static final Logger logger = LoggerFactory
			.getLogger(VelocityLogger.class);

	public void init(RuntimeServices rs) throws Exception {

	}

	public boolean isLevelEnabled(int level) {
		
		boolean result = false;
        switch (level)
        {
                case DEBUG_ID:
                        result = logger.isDebugEnabled();
                        break;
                case INFO_ID:
                        result = logger.isInfoEnabled();
                        break;
                case WARN_ID:
                        result = logger.isWarnEnabled();
                        break;
                case ERROR_ID:
                        result = logger.isErrorEnabled();
                        break;
        }
        return result;
	}

	public void log(int level, String msg) {

		switch (level) {
		case DEBUG_ID:
			logger.debug(msg);
			break;
		case INFO_ID:
			logger.info(msg);
			break;
		case WARN_ID:
			logger.warn(msg);
			break;
		case ERROR_ID:
			logger.error(msg);
			break;
		}

	}

	public void log(int level, String msg, Throwable exception) {

		switch (level) {
		case DEBUG_ID:
			logger.debug(msg,exception);
			break;
		case INFO_ID:
			logger.info(msg,exception);
			break;
		case WARN_ID:
			logger.warn(msg,exception);
			break;
		case ERROR_ID:
			logger.error(msg,exception);
			break;
		}
	}

}
