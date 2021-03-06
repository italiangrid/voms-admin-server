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

import org.glite.security.voms.admin.operations.util.CurrentAdminPermissionCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.cache.CacheStats;

public class PermissionCacheStatsLogger implements Runnable {
  
  private static final Logger LOG = LoggerFactory
    .getLogger(PermissionCacheStatsLogger.class);
  private final boolean dumpCache;
  
  public PermissionCacheStatsLogger() {
    dumpCache = false;
  }
  
  public PermissionCacheStatsLogger(boolean dumpCache) {
    this.dumpCache = dumpCache;
  }
  
  @Override
  public void run() {
    CacheStats stats = CurrentAdminPermissionCache.INSTANCE.stats();
    
    LOG.debug("Permission check cache stats: {}", stats);
    
    if (dumpCache){
      CurrentAdminPermissionCache.INSTANCE.dumpToLog();
    }
    
  }

}
