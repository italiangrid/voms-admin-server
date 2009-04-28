package org.glite.security.voms.admin.dao.generic;

import org.glite.security.voms.admin.model.Tag;
import org.glite.security.voms.admin.operations.VOMSPermission;


public interface TagDAO extends GenericDAO <Tag, Long> {

    public Tag findByName(String name);
    public Tag createTag(String name, VOMSPermission perm, VOMSPermission permOnPath, boolean implicit);
    
}
