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
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.glite.security.voms.User;
import org.glite.security.voms.VOMSException;
import org.glite.security.voms.admin.database.NoSuchGroupException;
import org.glite.security.voms.admin.database.NoSuchRoleException;
import org.glite.security.voms.admin.database.NoSuchUserException;
import org.glite.security.voms.admin.error.UnimplementedFeatureException;
import org.glite.security.voms.admin.model.VOMSAttributeDescription;
import org.glite.security.voms.admin.model.VOMSGroup;
import org.glite.security.voms.admin.model.VOMSRole;
import org.glite.security.voms.admin.model.VOMSUser;
import org.glite.security.voms.admin.operations.attributes.CreateAttributeDescriptionOperation;
import org.glite.security.voms.admin.operations.attributes.DeleteAttributeDescriptionOperation;
import org.glite.security.voms.admin.operations.attributes.FindAttributeDescriptionOperation;
import org.glite.security.voms.admin.operations.attributes.ListAttributeDescriptionsOperation;
import org.glite.security.voms.admin.operations.groups.DeleteGroupAttributeOperation;
import org.glite.security.voms.admin.operations.groups.FindGroupOperation;
import org.glite.security.voms.admin.operations.groups.ListAttributesForGroupOperation;
import org.glite.security.voms.admin.operations.groups.SetGroupAttributeOperation;
import org.glite.security.voms.admin.operations.roles.DeleteRoleAttributeOperation;
import org.glite.security.voms.admin.operations.roles.FindRoleOperation;
import org.glite.security.voms.admin.operations.roles.ListRoleAttributesOperation;
import org.glite.security.voms.admin.operations.roles.SetRoleAttributeOperation;
import org.glite.security.voms.admin.operations.users.DeleteUserAttributeOperation;
import org.glite.security.voms.admin.operations.users.FindUserOperation;
import org.glite.security.voms.admin.operations.users.ListAttributesForUserOperation;
import org.glite.security.voms.admin.operations.users.SetUserAttributeOperation;
import org.glite.security.voms.admin.util.PathNamingScheme;
import org.glite.security.voms.service.attributes.AttributeClass;
import org.glite.security.voms.service.attributes.AttributeValue;
import org.glite.security.voms.service.attributes.VOMSAttributes;

public class VomsAttributesService implements VOMSAttributes {

	private static final Log log = LogFactory
			.getLog(VomsAttributesService.class);

	public void createAttributeClass(String name, String description,
			boolean unique) throws RemoteException, VOMSException {

		log.info("createAttributeClass("
				+ StringUtils.join(new Object[] { name, description,
						new Boolean(unique) }, ',') + ");");

		try {

			Validator.validateInputString(name,
					"Invalid characters in attribute class name!");
			Validator.validateInputString(description,
					"Invalid characters in attribute class description!");

			CreateAttributeDescriptionOperation.instance(name, description,
					new Boolean(unique)).execute();

		} catch (RuntimeException e) {

			ServiceExceptionHelper.handleServiceException(log, e);
			throw e;
		}

	}

	public void createAttributeClass(String name, String description)
			throws RemoteException, VOMSException {

		log.info("createAttributeClass("
				+ StringUtils.join(new Object[] { name, description }, ',')
				+ ");");

		try {

			Validator.validateInputString(name,
					"Invalid characters in attribute class name!");
			Validator.validateInputString(description,
					"Invalid characters in attribute class description!");

			CreateAttributeDescriptionOperation.instance(name, description,
					new Boolean(false)).execute();

		} catch (RuntimeException e) {

			ServiceExceptionHelper.handleServiceException(log, e);
			throw e;
		}
	}

	public void createAttributeClass(String name) throws RemoteException,
			VOMSException {

		log.info("createAttributeClass("
				+ StringUtils.join(new Object[] { name }, ',') + ");");

		try {

			Validator.validateInputString(name,
					"Invalid characters in attribute class name!");

			CreateAttributeDescriptionOperation.instance(name, null,
					new Boolean(false)).execute();

		} catch (RuntimeException e) {

			ServiceExceptionHelper.handleServiceException(log, e);
			throw e;
		}
	}

	public void deleteAttributeClass(String name) throws RemoteException,
			VOMSException {

		log.info("deleteAttributeClass("
				+ StringUtils.join(new Object[] { name }, ',') + ");");

		try {

			DeleteAttributeDescriptionOperation.instance(name).execute();

		} catch (RuntimeException e) {

			ServiceExceptionHelper.handleServiceException(log, e);
			throw e;
		}

	}

	public void deleteAttributeClass(AttributeClass attributeClass)
			throws RemoteException, VOMSException {

		log
				.info("deleteAttributeClass("
						+ StringUtils
								.join(new Object[] { attributeClass }, ',')
						+ ");");

		try {

			DeleteAttributeDescriptionOperation.instance(
					attributeClass.getName()).execute();

		} catch (RuntimeException e) {

			ServiceExceptionHelper.handleServiceException(log, e);
			throw e;
		}
	}

	public void deleteGroupAttribute(String groupName, String attributeName)
			throws RemoteException, VOMSException {

		log.info("deleteGroupAttribute("
				+ StringUtils.join(new Object[] { groupName, attributeName },
						',') + ");");

		try {

			DeleteGroupAttributeOperation.instance(groupName, attributeName)
					.execute();

		} catch (RuntimeException e) {

			ServiceExceptionHelper.handleServiceException(log, e);
			throw e;
		}
	}

	public void deleteGroupAttribute(String groupName, AttributeValue value)
			throws RemoteException, VOMSException {

		log.info("deleteGroupAttribute("
				+ StringUtils.join(new Object[] { groupName, value }, ',')
				+ ");");

		try {

			DeleteGroupAttributeOperation.instance(groupName,
					value.getAttributeClass().getName()).execute();

		} catch (RuntimeException e) {

			ServiceExceptionHelper.handleServiceException(log, e);
			throw e;
		}
	}

	public void deleteRoleAttribute(String groupName, String roleName,
			String attributeName) throws RemoteException, VOMSException {

		log.info("deleteRoleAttribute("
				+ StringUtils.join(new Object[] { groupName, roleName,
						attributeName }, ',') + ");");

		try {

			if (PathNamingScheme.isRole(roleName))
				roleName = PathNamingScheme.getRoleName(roleName);

			DeleteRoleAttributeOperation.instance(groupName, roleName,
					attributeName).execute();

		} catch (RuntimeException e) {

			ServiceExceptionHelper.handleServiceException(log, e);
			throw e;
		}
	}

	public void deleteRoleAttribute(String groupName, String roleName,
			AttributeValue attributeValue) throws RemoteException,
			VOMSException {

		log.info("deleteRoleAttribute("
				+ StringUtils.join(new Object[] { groupName, roleName,
						attributeValue.getAttributeClass().getName() }, ',')
				+ ");");

		try {

			if (PathNamingScheme.isRole(roleName))
				roleName = PathNamingScheme.getRoleName(roleName);

			DeleteRoleAttributeOperation.instance(groupName, roleName,
					attributeValue.getAttributeClass().getName()).execute();

		} catch (RuntimeException e) {

			ServiceExceptionHelper.handleServiceException(log, e);
			throw e;
		}
	}

	public void deleteUserAttribute(User user, String attributeName)
			throws RemoteException, VOMSException {

		log.info("deleteUserAttribute("
				+ StringUtils.join(new Object[] { user, attributeName }, ',')
				+ ");");

		try {

			DeleteUserAttributeOperation.instance(user, attributeName)
					.execute();

		} catch (RuntimeException e) {

			ServiceExceptionHelper.handleServiceException(log, e);
			throw e;
		}
	}

	public void deleteUserAttribute(User user, AttributeValue attributeValue)
			throws RemoteException, VOMSException {

		log.info("deleteUserAttribute("
				+ StringUtils.join(new Object[] { user, attributeValue }, ',')
				+ ");");

		try {

			DeleteUserAttributeOperation.instance(user,
					attributeValue.getAttributeClass().getName()).execute();

		} catch (RuntimeException e) {

			ServiceExceptionHelper.handleServiceException(log, e);
			throw e;
		}
	}

	public AttributeClass getAttributeClass(String name)
			throws RemoteException, VOMSException {

		log.info("getAttributeClass("
				+ StringUtils.join(new Object[] { name }, ',') + ");");

		try {

			VOMSAttributeDescription desc = (VOMSAttributeDescription) FindAttributeDescriptionOperation
					.instance(name).execute();

			return desc.asAttributeClass();

		} catch (RuntimeException e) {

			ServiceExceptionHelper.handleServiceException(log, e);
			throw e;
		}

	}

	public AttributeClass[] listAttributeClasses() throws RemoteException,
			VOMSException {

		log.info("listAttributeClasses("
				+ StringUtils.join(new Object[] {}, ',') + ");");

		try {

			List descriptions = (List) ListAttributeDescriptionsOperation
					.instance().execute();

			return ServiceUtils.toAttributeClassArray(descriptions);

		} catch (RuntimeException e) {

			ServiceExceptionHelper.handleServiceException(log, e);
			throw e;
		}

	}

	public AttributeValue[] listGroupAttributes(String groupName)
			throws RemoteException, VOMSException {

		log.info("listGroupAttributes("
				+ StringUtils.join(new Object[] { groupName }, ',') + ");");

		try {

			VOMSGroup g = (VOMSGroup) FindGroupOperation.instance(groupName)
					.execute();

			if (g == null)
				throw new NoSuchGroupException("Group '" + groupName
						+ "' not found!");

			Collection attributes = (Collection) ListAttributesForGroupOperation
					.instance(g).execute();

			return ServiceUtils.toAttributeValueArray(attributes);

		} catch (RuntimeException e) {

			ServiceExceptionHelper.handleServiceException(log, e);
			throw e;
		}

	}

	public AttributeValue[] listRoleAttributes(String groupName, String roleName)
			throws RemoteException, VOMSException {

		log.info("listRoleAttributes("
				+ StringUtils.join(new Object[] { groupName, roleName }, ',')
				+ ");");

		try {

			if (roleName.startsWith("Role="))
				roleName = PathNamingScheme.getRoleName(roleName);

			VOMSRole r = (VOMSRole) FindRoleOperation.instance(roleName)
					.execute();
			VOMSGroup g = (VOMSGroup) FindGroupOperation.instance(groupName)
					.execute();

			if (r == null)
				throw new NoSuchRoleException("Role '" + roleName
						+ "' not found!");

			if (g == null)
				throw new NoSuchRoleException("Group '" + groupName
						+ "' not found!");

			Collection attributes = (Collection) ListRoleAttributesOperation
					.instance(g, r).execute();

			return ServiceUtils.toAttributeValueArray(attributes);

		} catch (RuntimeException e) {

			ServiceExceptionHelper.handleServiceException(log, e);
			throw e;
		}

	}

	public AttributeValue[] listUserAttributes(User user)
			throws RemoteException, VOMSException {

		log.info("listUserAttributes("
				+ StringUtils.join(new Object[] { user }, ',') + ");");

		try {

			VOMSUser u = (VOMSUser) FindUserOperation.instance(user.getDN(),
					user.getCA()).execute();

			if (u == null)
				throw new NoSuchUserException("User '" + user.getDN() + ","
						+ user.getCA() + "' not found!");

			Collection attributes = (Collection) ListAttributesForUserOperation
					.instance(u).execute();

			return ServiceUtils.toAttributeValueArray(attributes);

		} catch (RuntimeException e) {

			ServiceExceptionHelper.handleServiceException(log, e);
			throw e;

		}

	}

	public void saveAttributeClass(AttributeClass in0) throws RemoteException,
			VOMSException {

		log.info("saveAttributeClass("
				+ StringUtils.join(new Object[] { in0 }, ',') + ");");

		throw new UnimplementedFeatureException("saveAttributeClass(...)");

	}

	public void setGroupAttribute(String groupName,
			AttributeValue attributeValue) throws RemoteException,
			VOMSException {

		log.info("setGroupAttribute("
				+ StringUtils.join(new Object[] { groupName, attributeValue },
						',') + ");");

		try {

			Validator.validateInputString(attributeValue.getValue(),
					"Invalid characters in attribute value!");
			SetGroupAttributeOperation.instance(groupName, attributeValue)
					.execute();

		} catch (RuntimeException e) {

			ServiceExceptionHelper.handleServiceException(log, e);
			throw e;
		}
	}

	public void setRoleAttribute(String groupName, String roleName,
			AttributeValue attributeValue) throws RemoteException,
			VOMSException {

		log.info("setRoleAttribute("
				+ StringUtils.join(new Object[] { groupName, roleName,
						attributeValue }, ',') + ");");

		try {

			Validator.validateInputString(attributeValue.getValue(),
					"Invalid characters in attribute value!");
			if (PathNamingScheme.isRole(roleName))
				roleName = PathNamingScheme.getRoleName(roleName);

			SetRoleAttributeOperation.instance(groupName, roleName,
					attributeValue).execute();

		} catch (RuntimeException e) {

			ServiceExceptionHelper.handleServiceException(log, e);
			throw e;

		}
	}

	public void setUserAttribute(User user, AttributeValue attributeValue)
			throws RemoteException, VOMSException {

		log.info("setUserAttribute("
				+ StringUtils.join(new Object[] { user, attributeValue }, ',')
				+ ");");

		try {

			Validator.validateInputString(attributeValue.getValue(),
					"Invalid characters in attribute value!");
			SetUserAttributeOperation.instance(user, attributeValue).execute();

		} catch (RuntimeException e) {

			ServiceExceptionHelper.handleServiceException(log, e);
			throw e;
		}

	}

}
