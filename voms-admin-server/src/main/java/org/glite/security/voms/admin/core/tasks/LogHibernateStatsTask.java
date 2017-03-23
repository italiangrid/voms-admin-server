/**
 * Copyright (c) Istituto Nazionale di Fisica Nucleare (INFN). 2006-2016
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
 */
package org.glite.security.voms.admin.core.tasks;

import org.glite.security.voms.admin.persistence.HibernateFactory;
import org.hibernate.stat.Statistics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogHibernateStatsTask implements Runnable {

  public static final Logger LOG = LoggerFactory
    .getLogger(LogHibernateStatsTask.class);
  
  
  @Override
  public void run() {

    Statistics stats = HibernateFactory.getFactory().getStatistics();
    if (stats !=  null){
      LOG.info("Logging statistics....");
      LOG.info("start time: {}", stats.getStartTime());
      LOG.info("sessions opened: {}", stats.getSessionOpenCount());
      LOG.info("sessions closed: {}", stats.getSessionCloseCount());
      LOG.info("transactions: {}", stats.getTransactionCount());
      LOG.info("successful transactions: {}", stats.getSuccessfulTransactionCount());
      LOG.info("optimistic lock failures: {}", stats.getOptimisticFailureCount());
      LOG.info("flushes: {}", stats.getFlushCount());
      LOG.info("connections obtained: {}", stats.getConnectCount());
      LOG.info("statements prepared: {}", stats.getPrepareStatementCount());
      LOG.info("statements closed: {}", stats.getCloseStatementCount());
      LOG.info("second level cache puts: {}", stats.getSecondLevelCachePutCount());
      LOG.info("second level cache hits: {}", stats.getSecondLevelCacheHitCount());
      LOG.info("second level cache misses: {}", stats.getSecondLevelCacheMissCount());
      LOG.info("entities loaded: {}", stats.getEntityLoadCount());
      LOG.info("entities updated: {}", stats.getEntityUpdateCount());
      LOG.info("entities inserted: {}", stats.getEntityInsertCount());
      LOG.info("entities deleted: {}", stats.getEntityDeleteCount());
      LOG.info("entities fetched (minimize this): {}", stats.getEntityFetchCount());
      LOG.info("collections loaded: {}", stats.getCollectionLoadCount());
      LOG.info("collections updated: {}", stats.getCollectionUpdateCount());
      LOG.info("collections removed: {}", stats.getCollectionRemoveCount());
      LOG.info("collections recreated: {}", stats.getCollectionRecreateCount());
      LOG.info("collections fetched (minimize this): {}", stats.getCollectionFetchCount());
      LOG.info("queries executed to database: {}",stats.getQueryExecutionCount());
      LOG.info("query cache puts: {}", stats.getQueryCachePutCount());
      LOG.info("query cache hits: {}", stats.getQueryCacheMissCount());
      LOG.info("query cache misses: {}", stats.getQueryCacheMissCount());
      LOG.info("max query time: {} ms, for query {}", stats.getQueryExecutionMaxTime(), 
        stats.getQueryExecutionMaxTimeQueryString());
    }
  }

}
