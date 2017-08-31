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