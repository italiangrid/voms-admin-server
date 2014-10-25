/**
 * Copyright (c) Members of the EGEE Collaboration. 2006-2009.
 * See http://www.eu-egee.org/partners/ for details on the copyright holders.
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
 *
 * Authors:
 * 	Andrea Ceccanti (INFN)
 */
package org.glite.security.voms.admin.api.admin;

import org.glite.security.voms.admin.api.ACLEntry;
import org.glite.security.voms.admin.api.User;
import org.glite.security.voms.admin.api.VOMSException;

/**
 * Virtual Organisation Membership Service Administration interface.
 * 
 * 
 * @author <a href="mailto:andrea.ceccanti@cnaf.infn.it">Andrea Ceccanti</a>
 * 
 * @author <a href="mailto:Akos.Frohner@cern.ch">Akos Frohner</a>
 * @author <a href="mailto:lorentey@elte.hu">Karoly Lorentey</a>
 * 
 * 
 * 
 */
public interface VOMSAdmin {

  /**
   * Return the name of this VO.
   * 
   * <p>
   * <b>Permission:</b>LIST on the VO group.
   * 
   * @return The name of this VO.
   */
  public String getVOName() throws VOMSException;

  /**
   * Returns information about a user in the VOMS database. The user attributes
   * are returned in a <code>User</code> object.
   * 
   * <p>
   * <b>Permission:</b> LIST on the VO group.
   * 
   * @see org.glite.security.voms.service.UserAction
   * @param username
   *          The name of the user to look up.
   * @param userca
   *          The certificate authority of the user.
   * @return All information about the user that is known to VOMS.
   */
  public User getUser(String username, String userca) throws VOMSException;

  /**
   * Updates auxiliary information about a user in the VOMS database. The new
   * attributes are passed in the <code>User</code> object.
   * 
   * <p>
   * <b>Permission:</b> ADD on the VO group.
   * 
   * @see org.glite.security.voms.service.UserAction
   * @param user
   *          The user to update.
   */
  public void setUser(User user) throws VOMSException;

  /**
   * Creates a new user in the VOMS database.
   * 
   * <p>
   * <b>Permission:</b> ADD on the VO group.
   * 
   * @param user
   *          The user to be added.
   */
  public void createUser(User user) throws VOMSException;

  /**
   * Removes a user from the VOMS database. Deletes all the associated group,
   * role membership information and corresponding ACL entries as well. It is
   * basically a call to <code>removeMember(VO, user)</code>.
   * 
   * <p>
   * <b>Permission:</b> REMOVE on the VO group.
   * 
   * @see #removeMember
   * 
   * @param username
   *          The user's DN.
   * @param userca
   *          The user's CA.
   */
  public void deleteUser(String username, String userca) throws VOMSException;

  /**
   * Creates a new group as a subgroup of an existing group. Copies the default
   * ACL list of the parent to the new group and adds an extra entry for the
   * administrator with full privileges.
   * 
   * <p>
   * <b>Permission:</b> CREATE on parent group.
   * 
   * @param parentname
   *          The parent group's name.
   * @param groupname
   *          The group's name.
   */
  public void createGroup(String parentname, String groupname)
    throws VOMSException;

  /**
   * Deletes a group. The operation deletes the group, all of its sub-groups and
   * associated roles with all the membership information.
   * 
   * <p>
   * <b>Warning:</b> Deleting the VO "group" effectively wipes out the whole
   * database, so use with care!
   * 
   * <p>
   * <b>Permission:</b> DELETE on the group.
   * 
   * @param groupname
   *          The group's name.
   */
  public void deleteGroup(String groupname) throws VOMSException;

  /**
   * Creates a new role. Copies the default ACL list of the VO Group to the new
   * role and adds an extra entry for the administrator with full privileges.
   * 
   * <p>
   * <b>Permission:</b> CREATE on the VO group.
   * 
   * @param rolename
   *          The role to be added.
   */
  public void createRole(String rolename) throws VOMSException;

  /**
   * Deletes a role. The role is removed with all the membership information.
   * 
   * <p>
   * <b>Permission:</b> DELETE on the role.
   * 
   * @param rolename
   *          The role to be deleted.
   */
  public void deleteRole(String rolename) throws VOMSException;

  /**
   * Creates a new capability. Copies the default ACL list of the VO to the new
   * capability and adds an extra entry for the administrator with full
   * privileges.
   * 
   * <p>
   * <b>Permission:</b> CREATE on the VO group.
   * 
   * @param capability
   *          The capability to be created.
   */
  public void createCapability(String capability) throws VOMSException;

  /**
   * Deletes a capability. Deletes the capability with all the membership
   * information.
   * 
   * <p>
   * <b>Permission:</b> DELETE on the capability.
   * 
   * @param capability
   *          The capability to be deleted.
   */
  public void deleteCapability(String capability) throws VOMSException;

  /**
   * Adds a new member to the group. The user must be a member of the parent
   * group.
   * 
   * <p>
   * <b>Permission:</b> ADD on the group.
   * 
   * @param groupname
   *          The group's name.
   * @param username
   *          The user's DN.
   * @param userca
   *          The user's CA.
   */
  public void addMember(String groupname, String username, String userca)
    throws VOMSException;

  /**
   * Removes a member of a group. Also removes the membership information from
   * the group's sub-groups and associated roles of these groups. If it is the
   * VO group, then it will also delete the user with all its ACL entries.
   * 
   * <p>
   * <b>Permission:</b> REMOVE on the group.
   * 
   * @see #deleteUser
   * 
   * @param groupname
   *          The group's name.
   * @param username
   *          The user's DN.
   * @param userca
   *          The user's CA.
   */
  public void removeMember(String groupname, String username, String userca)
    throws VOMSException;

  /**
   * Assigns a new role to the user. The user must be a member of the parent
   * group.
   * 
   * <p>
   * <b>Permission:</b> ADD on the role.
   * 
   * @param groupname
   *          The name of the group associated with this assignment.
   * @param rolename
   *          The role's name.
   * @param username
   *          The name of the user to add.
   * @param userca
   *          The CA of the user to add.
   */
  public void assignRole(String groupname, String rolename, String username,
    String userca) throws VOMSException;

  /**
   * Dismisses a role of a user.
   * 
   * <p>
   * <b>Permission:</b> REMOVE on the role.
   * 
   * @param parentname
   *          The parent group's name.
   * @param rolename
   *          The role's name.
   * @param username
   *          The user's DN.
   * @param userca
   *          The user's CA.
   */
  public void dismissRole(String parentname, String rolename, String username,
    String userca) throws VOMSException;

  /**
   * Assigns a new capability to the user.
   * 
   * <p>
   * <b>Permission:</b> ADD on the capability.
   * 
   * @param capability
   *          The capability's name.
   * @param username
   *          The user's DN.
   * @param userca
   *          The user's CA.
   */
  public void assignCapability(String capability, String username, String userca)
    throws VOMSException;

  /**
   * Dismisses a capability of a user.
   * 
   * <p>
   * <b>Permission:</b> REMOVE on the capability.
   * 
   * @param capability
   *          The capability's name.
   * @param username
   *          The user's DN.
   * @param userca
   *          The user's CA.
   */
  public void dismissCapability(String capability, String username,
    String userca) throws VOMSException;

  /**
   * Lists members of a group.
   * 
   * <p>
   * <b>Permission:</b>LIST on the group.
   * 
   * @param groupname
   *          The group's name (null is the VO group).
   * @return List of users in this group.
   */
  public User[] listMembers(String groupname) throws VOMSException;

  /**
   * Lists assigned users of a role associated with a group.
   * 
   * <p>
   * <b>Permission:</b>LIST on the role.
   * 
   * @param groupname
   *          The group's name.
   * @param rolename
   *          The role's name.
   * @return List of users for this role.
   */
  public User[] listUsersWithRole(String groupname, String rolename)
    throws VOMSException;

  /**
   * Lists assigned users of a capability.
   * 
   * <p>
   * <b>Permission:</b>LIST on the capability.
   * 
   * @param capability
   *          The capability's name.
   * @return List of users with this capability.
   */
  public User[] listUsersWithCapability(String capability) throws VOMSException;

  /**
   * Returns the absolute "path" down to this group. The first element is the VO
   * group and the last is the group itself. There is at least one element in
   * this path if the group exists: the VO group.
   * 
   * <p>
   * <b>Permission:</b>LIST on parent groups.
   * 
   * @param groupname
   *          The group's name (null is the VO group).
   * @return Path to the group.
   */
  public String[] getGroupPath(String groupname) throws VOMSException;

  /**
   * Lists immediate sub-groups of a group.
   * 
   * <p>
   * <b>Permission:</b>LIST on the group.
   * 
   * @param groupname
   *          The group's name (null is the VO group).
   * @return List of groups in this group.
   */
  public String[] listSubGroups(String groupname) throws VOMSException;

  /**
   * Lists groups of a user.
   * 
   * <p>
   * <b>Permission:</b>LIST on the VO group.
   * 
   * @param username
   *          The user's DN.
   * @param userca
   *          The user's CA.
   * @return List of groups in this group.
   */
  public String[] listGroups(String username, String userca)
    throws VOMSException;

  /**
   * Lists roles.
   * 
   * <p>
   * <b>Permission:</b>LIST on the VO group.
   * 
   * @return List of roles in this VO.
   */
  public String[] listRoles() throws VOMSException;

  /**
   * Lists roles of a user.
   * 
   * <p>
   * <b>Permission:</b>LIST on the VO group.
   * 
   * @param username
   *          The user's DN.
   * @param userca
   *          The user's CA.
   * @return List of roles in this group.
   */
  public String[] listRoles(String username, String userca)
    throws VOMSException;

  /**
   * Lists capabilities.
   * 
   * <p>
   * <b>Permission:</b>LIST on the VO group.
   * 
   * @return List of capabilities.
   */
  public String[] listCapabilities() throws VOMSException;

  /**
   * Lists capabilities of a user.
   * 
   * <p>
   * <b>Permission:</b>LIST on the VO group.
   * 
   * @param username
   *          The user's DN.
   * @param userca
   *          The user's CA.
   * @return List of capabilities.
   */
  public String[] listCapabilities(String username, String userca)
    throws VOMSException;

  /**
   * Lists certificate authorities.
   * 
   * <p>
   * <b>Permission:</b>LIST on the VO group.
   * 
   * @return List of certificate authority DNs.
   */
  public String[] listCAs() throws VOMSException;

  /**
   * Returns the whole ACL associated with a container.
   * 
   * <p>
   * <b>Permission:</b> GETACL on the container.
   * 
   * @param container
   *          The container's name (null is the VO group).
   * @return The access control list.
   */
  public ACLEntry[] getACL(String container) throws VOMSException;

  /**
   * Replaces the existing ACL on this container.
   * 
   * <p>
   * <b>Permission:</b> SETACL on the container.
   * 
   * @param container
   *          The container's name.
   * @param acl
   *          The new access control list.
   */
  public void setACL(String container, ACLEntry[] acl) throws VOMSException;

  /**
   * Adds a new entry to an ACL of a container.
   * 
   * <p>
   * <b>Permission:</b> SETACL on the container.
   * 
   * @param container
   *          The container's name.
   * @param aclEntry
   *          The new access control list entry.
   */
  public void addACLEntry(String container, ACLEntry aclEntry)
    throws VOMSException;

  /**
   * Removes an existing entry from the ACL.
   * 
   * <p>
   * <b>Permission:</b> SETACL on the container.
   * 
   * @param container
   *          The container's name.
   * @param aclEntry
   *          The access control list entry to be removed.
   */
  public void removeACLEntry(String container, ACLEntry aclEntry)
    throws VOMSException;

  /**
   * Manipulates the default ACL, which is applied on every group created as a
   * subgroup of this one.
   * 
   * @see #getACL
   * 
   * @param groupname
   *          The group's name.
   * @return The access control list.
   */
  public ACLEntry[] getDefaultACL(String groupname) throws VOMSException;

  /**
   * Manipulates the default ACL, which is applied on every group created as a
   * subgroup of this one.
   * 
   * @see #setACL
   * 
   * @param groupname
   *          The group's name.
   * @param aclEntry
   *          The new access control list.
   */
  public void setDefaultACL(String groupname, ACLEntry[] aclEntry)
    throws VOMSException;

  /**
   * Manipulates the default ACL, which is applied on every group created as a
   * subgroup of this one.
   * 
   * @see #addACLEntry
   * 
   * @param groupname
   *          The group's name.
   * @param aclEntry
   *          The new access control list entry.
   */
  public void addDefaultACLEntry(String groupname, ACLEntry aclEntry)
    throws VOMSException;

  /**
   * Manipulates the default ACL, which is applied on every group created as a
   * subgroup of this one.
   * 
   * @see #removeACLEntry
   * 
   * @param groupname
   *          The group's name.
   * @param aclEntry
   *          The access control list entry to be removed.
   */
  public void removeDefaultACLEntry(String groupname, ACLEntry aclEntry)
    throws VOMSException;

  /**
   * Returns the major version number.
   */
  public int getMajorVersionNumber();

  /**
   * Returns the minor version number.
   */
  public int getMinorVersionNumber();

  /**
   * Returns the patch version number.
   */
  public int getPatchVersionNumber();
}

// Please do not change this line.
// arch-tag: 1a185f21-9efc-4279-8560-d420b4a23d29

