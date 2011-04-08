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

package org.glite.security.voms.admin.core.tasks;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.glite.security.voms.admin.configuration.VOMSConfiguration;
import org.glite.security.voms.admin.configuration.VOMSConfigurationConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VOMSExecutorService {
	
	public static final long BACKGROUND_TASKS_INITIAL_DELAY = 10;
	
	private static final int DEFAULT_POOL_SIZE = 1;
	
	public static Logger log = LoggerFactory.getLogger(VOMSExecutorService.class);

	private static VOMSExecutorService INSTANCE = null;
	
	private ScheduledExecutorService executorService;
	
	
	
	private VOMSExecutorService(){
	    
	    	int threadPoolSize = VOMSConfiguration.instance().getInt(VOMSConfigurationConstants.THREAD_POOL_SIZE_PROPERTY, DEFAULT_POOL_SIZE);
		executorService = Executors.newScheduledThreadPool(threadPoolSize);
	}
	
	public static synchronized void shutdown(){
	    
	    if (INSTANCE == null){
		log.debug("Executor service has not been started and, as such, cannot be shut down");
		return;
	    }
	    
	    INSTANCE.shutdownNow();
	}
	
	public static synchronized VOMSExecutorService instance(){
		if (INSTANCE == null)
			INSTANCE = new VOMSExecutorService();
		
		return INSTANCE;	
	}
	
	
	private Long getPeriod(String propertyName, Long defaultPeriod){
		
		if (propertyName == null && defaultPeriod != null)
			return defaultPeriod;
		
		Long period = VOMSConfiguration.instance().getLong(propertyName, defaultPeriod);
		
		if (period == null){
			log.warn("{} not found in configuration!", propertyName);
		}
		
		return period;
	}
	
	
	public void startBackgroundTask(Runnable task, String periodPropertyName){
		startBackgroundTask(task, periodPropertyName, null);
	}
	
	
	public void startBackgroundTask(Runnable task, String periodPropertyName, Long defaultPeriod){
	
		boolean registrationEnabled = VOMSConfiguration.instance().getBoolean(
				VOMSConfigurationConstants.REGISTRATION_SERVICE_ENABLED, true);
		
		boolean readOnly = VOMSConfiguration.instance().getBoolean(VOMSConfigurationConstants.READONLY, false);
		
		if ( readOnly ) {
			log.info("Task {} not started since this instance is read-only.", task.getClass().getSimpleName());
			return;
		}
		
		if ( !registrationEnabled && task instanceof RegistrationServiceTask) {
		    log.info("Task {} not started since registration is DISABLED for this VO.", task.getClass().getSimpleName());
		    return;
		}
		
		Long period = getPeriod(periodPropertyName, defaultPeriod);
		
		if (period == null){
			log.error("No period specified for task {}. Task will not be started.", task.getClass().getSimpleName());
			return;
		}
		
		if (period < 0){
		    log.info("Task {} will not be started, as requested: {} < 0.", task.getClass().getSimpleName(), periodPropertyName);
		    return;
		}
		
		log.info("Scheduling task {} with period: {} seconds", new String[]{task.getClass().getSimpleName(), period.toString()});
		executorService.scheduleAtFixedRate(new DatabaseTransactionTaskWrapper(task, true), BACKGROUND_TASKS_INITIAL_DELAY, period, TimeUnit.SECONDS);
	}
	
	
	/**
	 * @return
	 * @see java.util.concurrent.ExecutorService#isShutdown()
	 */
	public boolean isShutdown() {
		return executorService.isShutdown();
	}

	/**
	 * @return
	 * @see java.util.concurrent.ExecutorService#isTerminated()
	 */
	public boolean isTerminated() {
		return executorService.isTerminated();
	}

	/**
	 * @param command
	 * @param initialDelay
	 * @param delay
	 * @param unit
	 * @return
	 * @see java.util.concurrent.ScheduledExecutorService#scheduleWithFixedDelay(java.lang.Runnable, long, long, java.util.concurrent.TimeUnit)
	 */
	public ScheduledFuture<?> scheduleWithFixedDelay(Runnable command,
			long initialDelay, long delay, TimeUnit unit) {
		return executorService.scheduleWithFixedDelay(command, initialDelay,
				delay, unit);
	}

	/**
	 * @return
	 * @see java.util.concurrent.ExecutorService#shutdownNow()
	 */
	protected List<Runnable> shutdownNow() {
		return executorService.shutdownNow();
	}

	/**
	 * @param <T>
	 * @param task
	 * @return
	 * @see java.util.concurrent.ExecutorService#submit(java.util.concurrent.Callable)
	 */
	public <T> Future<T> submit(Callable<T> task) {
		return executorService.submit(task);
	}

	/**
	 * @param <T>
	 * @param task
	 * @param result
	 * @return
	 * @see java.util.concurrent.ExecutorService#submit(java.lang.Runnable, java.lang.Object)
	 */
	public <T> Future<T> submit(Runnable task, T result) {
		return executorService.submit(task, result);
	}

	/**
	 * @param task
	 * @return
	 * @see java.util.concurrent.ExecutorService#submit(java.lang.Runnable)
	 */
	public Future<?> submit(Runnable task) {
		return executorService.submit(task);
	}

	/**
	 * @param command
	 * @param initialDelay
	 * @param period
	 * @param unit
	 * @return
	 * @see java.util.concurrent.ScheduledExecutorService#scheduleAtFixedRate(java.lang.Runnable, long, long, java.util.concurrent.TimeUnit)
	 */
	public ScheduledFuture<?> scheduleAtFixedRate(Runnable command,
			long initialDelay, long period, TimeUnit unit) {
		return executorService.scheduleAtFixedRate(command, initialDelay,
				period, unit);
	}

	/**
	 * @param <V>
	 * @param callable
	 * @param delay
	 * @param unit
	 * @return
	 * @see java.util.concurrent.ScheduledExecutorService#schedule(java.util.concurrent.Callable, long, java.util.concurrent.TimeUnit)
	 */
	public <V> ScheduledFuture<V> schedule(Callable<V> callable, long delay,
			TimeUnit unit) {
		return executorService.schedule(callable, delay, unit);
	}

	/**
	 * @param command
	 * @param delay
	 * @param unit
	 * @return
	 * @see java.util.concurrent.ScheduledExecutorService#schedule(java.lang.Runnable, long, java.util.concurrent.TimeUnit)
	 */
	public ScheduledFuture<?> schedule(Runnable command, long delay,
			TimeUnit unit) {
		return executorService.schedule(command, delay, unit);
	}
		
	
	
}
