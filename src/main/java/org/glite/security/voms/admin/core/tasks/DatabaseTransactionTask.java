package org.glite.security.voms.admin.core.tasks;

import java.util.TimerTask;

import org.glite.security.voms.admin.persistence.HibernateFactory;
import org.glite.security.voms.admin.persistence.error.VOMSDatabaseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class DatabaseTransactionTask extends TimerTask {

	private static final Logger log = LoggerFactory.getLogger(DatabaseTransactionTask.class);
		
	@Override
	public final void run() {
		
		try{
			
			HibernateFactory.getSession();
			HibernateFactory.beginTransaction();
			
			doRun();
			
			HibernateFactory.commitTransaction();
			HibernateFactory.closeSession();
		
		}catch (VOMSDatabaseException e) {
			
			log.error("Database exception caught while executing {} task: {}", new String[]{this.getClass().getName(),e.getMessage()});
			log.error(e.getMessage(),e);
			log.error("Swallowing the exception hoping it's a temporary failure.");
		}

	}

	protected abstract void doRun();
}
