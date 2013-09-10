package org.italiangrid.voms.aa.x509.stats;

import org.apache.commons.math3.stat.descriptive.SummaryStatistics;


public enum ACEndpointStats {
	
	INSTANCE;
	
	private SummaryStatistics stats;
	
	private ACEndpointStats() {
		stats = new SummaryStatistics();
	}
	
	public synchronized void addValue(double value) {
		stats.addValue(value);
	}

	public synchronized ExecutionTimeStats getStats(){
		return ExecutionTimeStats.fromSummaryStats(stats);
	}
	
	public synchronized double getAverageExecutionTime(){
		return stats.getMean();
	}
	
	public synchronized double getMaximumExecutionTime(){
		return stats.getMax();
	}
	
	public synchronized double getMinimumExecutionTime(){
		return stats.getMin();
	}
	
	public synchronized void clear(){
		stats.clear();
	}
}