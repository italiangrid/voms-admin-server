package org.glite.security.voms.admin.operations.groups;

import org.glite.security.voms.admin.event.EventManager;
import org.glite.security.voms.admin.event.vo.group.GroupDescriptionUpdatedEvent;
import org.glite.security.voms.admin.operations.BaseVomsOperation;
import org.glite.security.voms.admin.operations.VOMSContext;
import org.glite.security.voms.admin.operations.VOMSPermission;
import org.glite.security.voms.admin.persistence.HibernateFactory;
import org.glite.security.voms.admin.persistence.model.VOMSGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class SetGroupDescriptionOperation extends BaseVomsOperation<VOMSGroup> {

  private static final Logger log = LoggerFactory
    .getLogger(SetGroupDescriptionOperation.class);
  
  VOMSGroup g;
  String groupDescription;
  
  private SetGroupDescriptionOperation(VOMSGroup g, String groupDescription) {
    
    super();
    this.g = g;
    this.groupDescription = groupDescription;
  }
  
  public static SetGroupDescriptionOperation instance(VOMSGroup g, String groupDescription) {

   return new SetGroupDescriptionOperation(g, groupDescription);
  }

  @Override
  protected void setupPermissions() {

    VOMSGroup parentGroup = g.getParent();

    // Add CONTAINER_READ permissions on the path from the root group to
    // the grandfather of the group that is being created
    addRequiredPermissionsOnPath(parentGroup,
      VOMSPermission.getContainerReadPermission());

    // Add CONTAINER_WRITE permissions on the parent group of the group that
    // is being created
    addRequiredPermission(VOMSContext.instance(parentGroup),
      VOMSPermission.getContainerRWPermissions());

    if (!parentGroup.isRootGroup()) {
      addRequiredPermission(VOMSContext.getVoContext(),
        VOMSPermission.getContainerRWPermissions());
    }

    if (log.isDebugEnabled())
      logRequiredPermissions();

  }

  @Override
  protected VOMSGroup doExecute() {
    
    g.setDescription(groupDescription);
    
    HibernateFactory.getSession().save(g);
    
    EventManager.instance().dispatch(new GroupDescriptionUpdatedEvent(g));
    
    return g;
  }

}
