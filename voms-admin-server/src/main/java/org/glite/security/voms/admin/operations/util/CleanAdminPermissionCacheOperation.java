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

import java.util.concurrent.Callable;

import org.glite.security.voms.admin.operations.VOAdminOperation;

import com.google.common.cache.CacheStats;

public class CleanAdminPermissionCacheOperation
  implements Callable<CacheStats> {

  private CleanAdminPermissionCacheOperation() {
    // TODO Auto-generated constructor stub
  }
  
  @Override
  public CacheStats call() throws Exception {
    CacheStats stats = CurrentAdminPermissionCache.INSTANCE.stats();
    CurrentAdminPermissionCache.INSTANCE.clean();
    return stats;
  }

  
  public static VOAdminOperation<CacheStats> instance(){
    CleanAdminPermissionCacheOperation op = 
      new CleanAdminPermissionCacheOperation();
    
    return new VOAdminOperation<>(op);
  }
}
