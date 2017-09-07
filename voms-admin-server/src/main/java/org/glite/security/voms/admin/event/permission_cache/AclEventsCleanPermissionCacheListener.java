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
package org.glite.security.voms.admin.event.permission_cache;
import java.util.EnumSet;

import org.glite.security.voms.admin.event.AbstractEventLister;
import org.glite.security.voms.admin.event.EventCategory;
import org.glite.security.voms.admin.event.vo.acl.ACLEvent;
import org.glite.security.voms.admin.operations.util.CurrentAdminPermissionCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AclEventsCleanPermissionCacheListener extends AbstractEventLister<ACLEvent>{

  public static final Logger LOG = LoggerFactory.getLogger(AclEventsCleanPermissionCacheListener.class);
  
  public AclEventsCleanPermissionCacheListener() {
    super(EnumSet.of(EventCategory.VOLifecycleEvent), ACLEvent.class);
  
  }

  @Override
  protected void doFire(ACLEvent e) {
    LOG.info("Cleaning permission cache as result of {}",e);
    CurrentAdminPermissionCache.INSTANCE.clean();
  }

  public static AclEventsCleanPermissionCacheListener instance(){
    return new AclEventsCleanPermissionCacheListener();
  }
}