package org.italiangrid.voms.aa.x509.stats;

import org.apache.commons.math3.stat.descriptive.SummaryStatistics;


public class ExecutionTimeStats {

	double min;
	double max;
	double mean;
	long count;
	
	private ExecutionTimeStats() {}

	public static ExecutionTimeStats fromSummaryStats(SummaryStatistics stats){
		
		ExecutionTimeStats v = new ExecutionTimeStats();
		
		v.setMax(stats.getMax());
		v.setMin(stats.getMin());
		v.setMean(stats.getMean());
		v.setCount(stats.getN());
		return v;
	}
	
	/**
	 * @return the min
	 */
	public double getMin() {
	
		return min;
	}

	
	/**
	 * @param min the min to set
	 */
	public void setMin(double min) {
	
		this.min = min;
	}

	
	/**
	 * @return the max
	 */
	public double getMax() {
	
		return max;
	}

	
	/**
	 * @param max the max to set
	 */
	public void setMax(double max) {
	
		this.max = max;
	}

	
	/**
	 * @return the mean
	 */
	public double getMean() {
	
		return mean;
	}

	
	/**
	 * @param mean the mean to set
	 */
	public void setMean(double mean) {
	
		this.mean = mean;
	}

	
	/**
	 * @return the count
	 */
	public long getCount() {
	
		return count;
	}

	
	/**
	 * @param count the count to set
	 */
	public void setCount(long count) {
	
		this.count = count;
	}
	
}
