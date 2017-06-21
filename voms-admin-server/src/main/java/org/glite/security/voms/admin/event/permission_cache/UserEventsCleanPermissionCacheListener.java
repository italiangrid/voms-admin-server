package org.glite.security.voms.admin.event.permission_cache;

import java.util.EnumSet;

import org.glite.security.voms.admin.event.AbstractEventLister;
import org.glite.security.voms.admin.event.EventCategory;
import org.glite.security.voms.admin.event.user.UserCreatedEvent;
import org.glite.security.voms.admin.event.user.UserDeletedEvent;
import org.glite.security.voms.admin.event.user.UserLifecycleEvent;
import org.glite.security.voms.admin.operations.util.CurrentAdminPermissionCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserEventsCleanPermissionCacheListener extends AbstractEventLister<UserLifecycleEvent>{

  public static final Logger LOG = LoggerFactory.getLogger(UserEventsCleanPermissionCacheListener.class);
  
  private UserEventsCleanPermissionCacheListener() {
    super(EnumSet.of(EventCategory.UserLifecycleEvent), UserLifecycleEvent.class);
 
  }

  @Override
  protected void doFire(UserLifecycleEvent e) {
    
    if (e instanceof UserDeletedEvent || e instanceof UserCreatedEvent){
      LOG.info("Cleaning permission cache as result of {}",e);
      CurrentAdminPermissionCache.INSTANCE.clean();
    } 
  }
  
  public static UserEventsCleanPermissionCacheListener instance(){
    return new UserEventsCleanPermissionCacheListener();
  }

}
