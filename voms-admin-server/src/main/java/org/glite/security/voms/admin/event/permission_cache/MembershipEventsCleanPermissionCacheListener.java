package org.glite.security.voms.admin.event.permission_cache;

import java.util.EnumSet;

import org.glite.security.voms.admin.event.AbstractEventLister;
import org.glite.security.voms.admin.event.EventCategory;
import org.glite.security.voms.admin.event.user.membership.UserMembershipEvent;
import org.glite.security.voms.admin.operations.util.CurrentAdminPermissionCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MembershipEventsCleanPermissionCacheListener extends AbstractEventLister<UserMembershipEvent> {

  public static final Logger LOG = LoggerFactory.getLogger(MembershipEventsCleanPermissionCacheListener.class);
  
  private MembershipEventsCleanPermissionCacheListener() {
    super(EnumSet.of(EventCategory.UserMembershipEvent), UserMembershipEvent.class);
  }

  @Override
  protected void doFire(UserMembershipEvent e) {
    LOG.info("Cleaning permission cache as result of {}",e);
    CurrentAdminPermissionCache.INSTANCE.clean();
  }

  public static MembershipEventsCleanPermissionCacheListener instance() {
    return new MembershipEventsCleanPermissionCacheListener();
  }
}
