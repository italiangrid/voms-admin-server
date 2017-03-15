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
package org.glite.security.voms.admin.operations.util;

import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.glite.security.voms.admin.operations.CurrentAdmin;
import org.glite.security.voms.admin.operations.VOMSContext;
import org.glite.security.voms.admin.operations.VOMSPermission;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.CacheStats;
import com.google.common.cache.LoadingCache;

public enum CurrentAdminPermissionCache {
  
  INSTANCE;
  
  private Logger log = LoggerFactory.getLogger(CurrentAdminPermissionCache.class);
  
  private LoadingCache<PermissionCheck, Boolean> cache;

  private CurrentAdminPermissionCache() {
    cache = CacheBuilder.newBuilder()
      .maximumSize(1000)
      .expireAfterWrite(5, TimeUnit.MINUTES)
      .recordStats()
      .build(new CheckResultCacheLoader());
  }

  public boolean hasPermission(CurrentAdmin admin, VOMSContext c,
    VOMSPermission p) throws ExecutionException {

    PermissionCheck key = PermissionCheckImpl.instance(admin, c, p);
    return cache.get(key);
  }
  
  public void clean(){
    log.debug("Cleaning cache");
    cache.invalidateAll();
  }

  static class CheckResultCacheLoader
    extends CacheLoader<PermissionCheck, Boolean> {

    private static final Logger LOG = LoggerFactory
      .getLogger(CheckResultCacheLoader.class);

    @Override
    public Boolean load(PermissionCheck key) throws Exception {

      boolean result = key.getAdmin()
        .checkPermission(key.getContext(), key.getPermission());

      if (LOG.isDebugEnabled()) {
        LOG.debug("Permission check result: {} -> {}", key, result);
      }

      return result;
    }

  }
  
  public CacheStats stats(){
    return cache.stats();
  }
  
  public void dumpToLog() {
    log.debug("Cache size: {}", cache.size());
    log.debug("Cache dump:");
    for (Map.Entry<PermissionCheck, Boolean> e: cache.asMap().entrySet()){
      log.debug("{} -> {}", e.getKey(), e.getValue());
    }
  }

}
