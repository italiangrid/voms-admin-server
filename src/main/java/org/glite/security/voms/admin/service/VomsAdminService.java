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
package org.glite.security.voms.admin.service;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.glite.security.voms.ACLEntry;
import org.glite.security.voms.User;
import org.glite.security.voms.VOMSException;
import org.glite.security.voms.admin.configuration.VOMSConfiguration;
import org.glite.security.voms.admin.error.NullArgumentException;
import org.glite.security.voms.admin.error.UnimplementedFeatureException;
import org.glite.security.voms.admin.operations.VOMSContext;
import org.glite.security.voms.admin.operations.ca.ListCaOperation;
import org.glite.security.voms.admin.operations.groups.AddMemberOperation;
import org.glite.security.voms.admin.operations.groups.CreateGroupOperation;
import org.glite.security.voms.admin.operations.groups.DeleteGroupOperation;
import org.glite.security.voms.admin.operations.groups.ListChildrenGroupsOperation;
import org.glite.security.voms.admin.operations.groups.ListMembersOperation;
import org.glite.security.voms.admin.operations.groups.RemoveMemberOperation;
import org.glite.security.voms.admin.operations.roles.CreateRoleOperation;
import org.glite.security.voms.admin.operations.roles.DeleteRoleOperation;
import org.glite.security.voms.admin.operations.roles.ListRolesOperation;
import org.glite.security.voms.admin.operations.users.AssignRoleOperation;
import org.glite.security.voms.admin.operations.users.CreateUserOperation;
import org.glite.security.voms.admin.operations.users.DeleteUserOperation;
import org.glite.security.voms.admin.operations.users.DismissRoleOperation;
import org.glite.security.voms.admin.operations.users.FindUserOperation;
import org.glite.security.voms.admin.operations.users.ListUserGroupsOperation;
import org.glite.security.voms.admin.operations.users.ListUserRolesOperation;
import org.glite.security.voms.admin.operations.users.UpdateUserOperation;
import org.glite.security.voms.admin.persistence.error.HibernateFactory;
import org.glite.security.voms.admin.persistence.error.NoSuchUserException;
import org.glite.security.voms.admin.persistence.model.VOMSUser;
import org.glite.security.voms.admin.util.PathNamingScheme;
import org.glite.security.voms.service.admin.VOMSAdmin;

public class VomsAdminService implements VOMSAdmin {

	private static final Log log = LogFactory.getLog(VomsAdminService.class);

	public VomsAdminService() {

		super();

	}

	public User getUser(String username, String userca) throws RemoteException,
			VOMSException {

		log.info("getUser("
				+ StringUtils.join(new Object[] { username, userca }, ',')
				+ ");");

		try {

			VOMSUser u = (VOMSUser) FindUserOperation
					.instance(username, userca).execute();

			if (u == null)
				return null;
			else
				return u.asUser();

		} catch (RuntimeException e) {

			ServiceExceptionHelper.handleServiceException(log, e);

			throw e;

		}

	}

	public void setUser(User user) throws RemoteException, VOMSException {

		log.info("setUser(" + StringUtils.join(new Object[] { user }, ',')
				+ ");");

		if (user == null)
			throw new NullArgumentException("User passed as argument is null!");

		VOMSUser u = (VOMSUser) FindUserOperation.instance(user.getDN(),
				user.getCA()).execute();

		if (u == null)
			throw new NoSuchUserException("User (" + user.getDN() + ","
					+ user.getCA() + ") not found in database!");

		Validator.validateUser(user);

		u.fromUser(user);

		UpdateUserOperation.instance(u).execute();

	}

	public void createGroup(String parentname, String groupname)
			throws RemoteException, VOMSException {

		log.info("createGroup("
				+ StringUtils.join(new Object[] { parentname, groupname }, ',')
				+ ");");

		if (!groupname.startsWith("/"))
			groupname = "/" + groupname;

		try {

			Validator.validateInputString(groupname,
					"Invalid characters in group name!");
			CreateGroupOperation.instance(groupname).execute();

		} catch (RuntimeException e) {

			ServiceExceptionHelper.handleServiceException(log, e);

			throw e;
		}

	}

	public int getMajorVersionNumber() throws RemoteException {

		log.info("getMajorVersionNumber("
				+ StringUtils.join(new Object[] {}, ',') + ");");

		return 2;
	}

	public String getVOName() throws RemoteException, VOMSException {

		log.info("getVOName(" + StringUtils.join(new Object[] {}, ',') + ");");

		try {

			return "/" + VOMSConfiguration.instance().getVOName();

		} catch (RuntimeException e) {

			ServiceExceptionHelper.handleServiceException(log, e);
			throw e;

		}
	}

	public void createUser(User user) throws RemoteException, VOMSException {

		log.info("createUser("
				+ StringUtils.join(new Object[] { user.getDN(), user.getCA() },
						',') + ");");

		try {

			Validator.validateUser(user);
			CreateUserOperation.instance(user.getDN(), user.getCA(),
					user.getCN(), user.getCertUri(), user.getMail()).execute();

			HibernateFactory.commitTransaction();

		} catch (RuntimeException e) {

			ServiceExceptionHelper.handleServiceException(log, e);
			throw e;
		}

	}

	public void deleteUser(String username, String userca)
			throws RemoteException, VOMSException {

		log.info("deleteUser("
				+ StringUtils.join(new Object[] { username, userca }, ',')
				+ ");");

		try {

			DeleteUserOperation.instance(username, userca).execute();
			HibernateFactory.commitTransaction();

		} catch (RuntimeException e) {
			ServiceExceptionHelper.handleServiceException(log, e);
			throw e;

		}

	}

	public void deleteGroup(String groupname) throws RemoteException,
			VOMSException {

		log.info("deleteGroup("
				+ StringUtils.join(new Object[] { groupname }, ',') + ");");

		try {

			if (!groupname.startsWith("/"))
				groupname = "/" + groupname;

			DeleteGroupOperation.instance(groupname).execute();
			HibernateFactory.commitTransaction();

		} catch (RuntimeException e) {

			ServiceExceptionHelper.handleServiceException(log, e);
			throw e;
		}

	}

	public void createRole(String rolename) throws RemoteException,
			VOMSException {

		log.info("createRole("
				+ StringUtils.join(new Object[] { rolename }, ',') + ");");

		try {

			Validator.validateDN(rolename, "Invalid characters in role name!");
			if (PathNamingScheme.isRole(rolename))
				rolename = PathNamingScheme.getRoleName(rolename);

			CreateRoleOperation.instance(rolename).execute();
			HibernateFactory.commitTransaction();

		} catch (RuntimeException e) {

			ServiceExceptionHelper.handleServiceException(log, e);
			throw e;
		}

	}

	public void deleteRole(String rolename) throws RemoteException,
			VOMSException {

		log.info("deleteRole("
				+ StringUtils.join(new Object[] { rolename }, ',') + ");");

		try {
			if (PathNamingScheme.isRole(rolename))
				rolename = PathNamingScheme.getRoleName(rolename);

			DeleteRoleOperation.instance(rolename).execute();
			HibernateFactory.commitTransaction();

		} catch (RuntimeException e) {

			ServiceExceptionHelper.handleServiceException(log, e);
			throw e;

		}

	}

	public void createCapability(String capability) throws RemoteException,
			VOMSException {

		log.info("createCapability("
				+ StringUtils.join(new Object[] { capability }, ',') + ");");

		try {

			throw new UnimplementedFeatureException(
					"createCapability(String s)");

		} catch (RuntimeException e) {

			ServiceExceptionHelper.handleServiceException(log, e);
			throw e;
		}
	}

	public void deleteCapability(String capability) throws RemoteException,
			VOMSException {

		log.info("deleteCapability("
				+ StringUtils.join(new Object[] { capability }, ',') + ");");

		throw new UnimplementedFeatureException("deleteCapability(String s)");

	}

	public void addMember(String groupname, String username, String userca)
			throws RemoteException, VOMSException {

		log.info("addMember("
				+ StringUtils.join(
						new Object[] { groupname, username, userca }, ',')
				+ ");");

		try {

			AddMemberOperation.instance(groupname, username, userca).execute();

			HibernateFactory.commitTransaction();

		} catch (RuntimeException e) {

			ServiceExceptionHelper.handleServiceException(log, e);
			throw e;

		}

	}

	public void removeMember(String groupname, String username, String userca)
			throws RemoteException, VOMSException {

		log.info("removeMember("
				+ StringUtils.join(
						new Object[] { groupname, username, userca }, ',')
				+ ");");

		try {

			RemoveMemberOperation.instance(groupname, username, userca)
					.execute();

			HibernateFactory.commitTransaction();

		} catch (RuntimeException e) {

			ServiceExceptionHelper.handleServiceException(log, e);
			throw e;

		}

	}

	public void assignRole(String groupname, String rolename, String username,
			String userca) throws RemoteException, VOMSException {

		log.info("assignRole("
				+ StringUtils.join(new Object[] { groupname, rolename,
						username, userca }, ',') + ");");

		if (PathNamingScheme.isRole(rolename))
			rolename = PathNamingScheme.getRoleName(rolename);

		try {

			AssignRoleOperation.instance(groupname, rolename, username, userca)
					.execute();

			HibernateFactory.commitTransaction();

		} catch (RuntimeException e) {

			ServiceExceptionHelper.handleServiceException(log, e);
			throw e;

		}

	}

	public void dismissRole(String groupname, String rolename, String username,
			String userca) throws RemoteException, VOMSException {

		log.info("dismissRole("
				+ StringUtils.join(new Object[] { groupname, rolename,
						username, userca }, ',') + ");");

		if (PathNamingScheme.isRole(rolename))
			rolename = PathNamingScheme.getRoleName(rolename);

		try {

			DismissRoleOperation
					.instance(groupname, rolename, username, userca).execute();

			HibernateFactory.commitTransaction();

		} catch (RuntimeException e) {

			ServiceExceptionHelper.handleServiceException(log, e);
			throw e;

		}
	}

	public void assignCapability(String capability, String username,
			String userca) throws RemoteException, VOMSException {

		log.info("assignCapability("
				+ StringUtils.join(
						new Object[] { capability, username, userca }, ',')
				+ ");");

		throw new UnimplementedFeatureException("assignCapability(...)");

	}

	public void dismissCapability(String capability, String username,
			String userca) throws RemoteException, VOMSException {

		log.info("dismissCapability("
				+ StringUtils.join(
						new Object[] { capability, username, userca }, ',')
				+ ");");

		throw new UnimplementedFeatureException("dismissCapability(...)");

	}

	public User[] listMembers(String groupname) throws RemoteException,
			VOMSException {

		log.info("listMembers("
				+ StringUtils.join(new Object[] { groupname }, ',') + ");");

		if (groupname == null || groupname.equals(""))
			groupname = "/" + VOMSConfiguration.instance().getVOName();

		try {

			List members = (List) ListMembersOperation.instance(groupname)
					.execute();

			HibernateFactory.commitTransaction();

			return VOMSUser.collectionAsUsers(members);

		} catch (RuntimeException e) {

			ServiceExceptionHelper.handleServiceException(log, e);
			throw e;

		}
	}

	public User[] listUsersWithRole(String groupname, String rolename)
			throws RemoteException, VOMSException {

		log.info("listUsersWithRole("
				+ StringUtils.join(new Object[] { groupname, rolename }, ',')
				+ ");");
		try {

			if (!PathNamingScheme.isRole(rolename))
				rolename = "Role=" + rolename;

			String contextString = groupname + "/" + rolename;

			List members = (List) ListMembersOperation.instance(contextString)
					.execute();

			HibernateFactory.commitTransaction();
			return VOMSUser.collectionAsUsers(members);

		} catch (RuntimeException e) {

			ServiceExceptionHelper.handleServiceException(log, e);
			throw e;

		}

	}

	public User[] listUsersWithCapability(String capability)
			throws RemoteException, VOMSException {

		log.info("listUsersWithCapability("
				+ StringUtils.join(new Object[] { capability }, ',') + ");");

		throw new UnimplementedFeatureException("listUsersWithCapability(...)");

	}

	public String[] getGroupPath(String groupname) throws RemoteException,
			VOMSException {

		log.info("getGroupPath("
				+ StringUtils.join(new Object[] { groupname }, ',') + ");");

		try {

			String[] parentChain = PathNamingScheme
					.getParentGroupChain(groupname);
			String[] result = new String[parentChain.length + 1];

			result[0] = groupname;
			System.arraycopy(parentChain, 0, result, 1, parentChain.length);

			return result;

		} catch (RuntimeException e) {

			ServiceExceptionHelper.handleServiceException(log, e);
			throw e;

		}
	}

	public String[] listSubGroups(String groupname) throws RemoteException,
			VOMSException {

		log.info("listSubGroups("
				+ StringUtils.join(new Object[] { groupname }, ',') + ");");

		try {

			List childrenGroups;

			if (groupname == null) {

				VOMSContext ctxt = VOMSContext.getVoContext();
				childrenGroups = (List) ListChildrenGroupsOperation.instance(
						ctxt.getGroup()).execute();
			} else
				childrenGroups = (List) ListChildrenGroupsOperation.instance(
						groupname).execute();

			HibernateFactory.commitTransaction();
			return ServiceUtils.groupsToStringArray(childrenGroups);

		} catch (RuntimeException e) {

			ServiceExceptionHelper.handleServiceException(log, e);
			throw e;

		}
	}

	public String[] listGroups(String username, String userca)
			throws RemoteException, VOMSException {

		log.info("listGroups("
				+ StringUtils.join(new Object[] { username, userca }, ',')
				+ ");");
		try {

			Set groups = (Set) ListUserGroupsOperation.instance(username,
					userca).execute();

			HibernateFactory.commitTransaction();

			return ServiceUtils.groupsToStringArray(groups);

		} catch (RuntimeException e) {

			ServiceExceptionHelper.handleServiceException(log, e);
			throw e;

		}

	}

	public String[] listRoles(String username, String userca)
			throws RemoteException, VOMSException {

		log.info("listRoles("
				+ StringUtils.join(new Object[] { username, userca }, ',')
				+ ");");
		try {

			Set roles = (Set) ListUserRolesOperation.instance(username, userca)
					.execute();

			HibernateFactory.commitTransaction();

			return ServiceUtils.toStringArray(roles);

		} catch (RuntimeException e) {

			ServiceExceptionHelper.handleServiceException(log, e);
			throw e;

		}
	}

	public String[] listRoles() throws RemoteException, VOMSException {

		log.info("listRoles();");

		try {

			List roles = (List) ListRolesOperation.instance().execute();

			HibernateFactory.commitTransaction();

			return ServiceUtils.rolesToStringArray(roles);

		} catch (RuntimeException e) {

			ServiceExceptionHelper.handleServiceException(log, e);
			throw e;

		}
	}

	public String[] listCapabilities(String username, String userca)
			throws RemoteException, VOMSException {

		log.info("listCapabilities("
				+ StringUtils.join(new Object[] { username, userca }, ',')
				+ ");");

		throw new UnimplementedFeatureException("listCapabilities(...)");
	}

	public String[] listCapabilities() throws RemoteException, VOMSException {

		log.info("listCapabilities(" + StringUtils.join(new Object[] {}, ',')
				+ ");");

		throw new UnimplementedFeatureException("listCapabilities(...)");

	}

	public String[] listCAs() throws RemoteException, VOMSException {

		log.info("listCAs();");

		try {

			List cas = (List) ListCaOperation.instance().execute();

			HibernateFactory.commitTransaction();
			return ServiceUtils.casToStringArray(cas);

		} catch (RuntimeException e) {

			ServiceExceptionHelper.handleServiceException(log, e);
			throw e;

		}

	}

	public ACLEntry[] getACL(String container) throws RemoteException,
			VOMSException {

		log.info("getACL(" + StringUtils.join(new Object[] { container }, ',')
				+ ");");

		throw new UnimplementedFeatureException("getACL(...)");
	}

	public void setACL(String container, ACLEntry[] acl)
			throws RemoteException, VOMSException {

		log
				.info("setACL("
						+ StringUtils
								.join(new Object[] { container, acl }, ',')
						+ ");");

		throw new UnimplementedFeatureException("setACL(...)");

	}

	public void addACLEntry(String container, ACLEntry aclEntry)
			throws RemoteException, VOMSException {

		log.info("addACLEntry("
				+ StringUtils.join(new Object[] { container, aclEntry }, ',')
				+ ");");

		throw new UnimplementedFeatureException("addACLEntry(...)");

	}

	public void removeACLEntry(String container, ACLEntry aclEntry)
			throws RemoteException, VOMSException {

		log.info("removeACLEntry("
				+ StringUtils.join(new Object[] { container, aclEntry }, ',')
				+ ");");

		throw new UnimplementedFeatureException("removeACLEntry(...)");

	}

	public ACLEntry[] getDefaultACL(String groupname) throws RemoteException,
			VOMSException {

		log.info("getDefaultACL("
				+ StringUtils.join(new Object[] { groupname }, ',') + ");");

		throw new UnimplementedFeatureException("getDefaultACL(...)");
	}

	public void setDefaultACL(String groupname, ACLEntry[] aclEntry)
			throws RemoteException, VOMSException {

		log.info("setDefaultACL("
				+ StringUtils.join(new Object[] { groupname, aclEntry }, ',')
				+ ");");

		throw new UnimplementedFeatureException("setDefaultACL(...)");

	}

	public void addDefaultACLEntry(String groupname, ACLEntry aclEntry)
			throws RemoteException, VOMSException {

		log.info("addDefaultACLEntry("
				+ StringUtils.join(new Object[] { groupname, aclEntry }, ',')
				+ ");");

		throw new UnimplementedFeatureException("addDefaultACLEntry(...)");

	}

	public void removeDefaultACLEntry(String groupname, ACLEntry aclEntry)
			throws RemoteException, VOMSException {

		log.info("removeDefaultACLEntry("
				+ StringUtils.join(new Object[] { groupname, aclEntry }, ',')
				+ ");");

		throw new UnimplementedFeatureException("removeDefaultACLEntry(...)");

	}

	public int getMinorVersionNumber() throws RemoteException {

		log.info("getMinorVersionNumber()");

		return 0;
	}

	public int getPatchVersionNumber() throws RemoteException {

		log.info("getPatchVersionNumber()");
		return 0;
	}

}
