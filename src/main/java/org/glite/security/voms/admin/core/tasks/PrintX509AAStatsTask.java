package org.glite.security.voms.admin.core.tasks;

import org.italiangrid.voms.aa.x509.stats.ACEndpointStats;
import org.italiangrid.voms.aa.x509.stats.ExecutionTimeStats;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PrintX509AAStatsTask implements Runnable{

	public static final Logger log = 
		LoggerFactory.getLogger(PrintX509AAStatsTask.class);

	public static final long DEFAULT_PERIOD_IN_SECS = 60;
	
	public PrintX509AAStatsTask() {}

	@Override
	public void run() {
		ExecutionTimeStats stats = ACEndpointStats.INSTANCE.getStats();
		log.info("AC endpoint execution time (msecs): N={}, max={}, min={}, avg={}.",
			new Object[]{stats.getCount(),
			stats.getMax(), 
			stats.getMin(), 
			stats.getMean()});
	}

}
