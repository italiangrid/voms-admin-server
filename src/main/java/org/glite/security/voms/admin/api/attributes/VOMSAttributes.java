/*******************************************************************************
 *Copyright (c) Members of the EGEE Collaboration. 2006. 
 *See http://www.eu-egee.org/partners/ for details on the copyright
 *holders.  
 *
 *Licensed under the Apache License, Version 2.0 (the "License"); 
 *you may not use this file except in compliance with the License. 
 *You may obtain a copy of the License at 
 *
 *    http://www.apache.org/licenses/LICENSE-2.0 
 *
 *Unless required by applicable law or agreed to in writing, software 
 *distributed under the License is distributed on an "AS IS" BASIS, 
 *WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 *See the License for the specific language governing permissions and 
 *limitations under the License.
 *
 * Authors:
 *     Andrea Ceccanti - andrea.ceccanti@cnaf.infn.it
 *******************************************************************************/
package org.glite.security.voms.admin.api.attributes;

import org.glite.security.voms.admin.api.User;
import org.glite.security.voms.admin.api.VOMSException;

public interface VOMSAttributes {

	/**
	 * 
	 * Creates a new attribute class.
	 * 
	 * @param name
	 *            , the name of the attribute
	 * @param description
	 *            , a description associated to the attribute
	 * @param uniquenessChecked
	 *            , a flag that enables the uniqueness checking of attribute
	 *            values between users.
	 * 
	 * @throws VOMSException
	 *             , if something goes wrong.
	 */
	public void createAttributeClass(String name, String description,
			boolean uniquenessChecked) throws VOMSException;

	/**
	 * 
	 * @param name
	 * @param description
	 * @throws VOMSException
	 */
	public void createAttributeClass(String name, String description)
			throws VOMSException;

	/**
	 * 
	 * @param name
	 * @throws VOMSException
	 */
	public void createAttributeClass(String name) throws VOMSException;

	/**
	 * 
	 * @param name
	 * @return
	 * @throws VOMSException
	 */
	public AttributeClass getAttributeClass(String name) throws VOMSException;

	/**
	 * 
	 * @param attributeClass
	 * @throws VOMSException
	 */
	public void saveAttributeClass(AttributeClass attributeClass)
			throws VOMSException;

	/**
	 * 
	 * @param name
	 * @throws VOMSException
	 */
	public void deleteAttributeClass(String name) throws VOMSException;

	/**
	 * 
	 * @param attributeClass
	 * @throws VOMSException
	 */
	public void deleteAttributeClass(AttributeClass attributeClass)
			throws VOMSException;

	/**
	 * 
	 * @return
	 * @throws VOMSException
	 */
	public AttributeClass[] listAttributeClasses() throws VOMSException;

	/**
	 * 
	 * @param user
	 * @return
	 * @throws VOMSException
	 */
	public AttributeValue[] listUserAttributes(User user) throws VOMSException;

	/**
	 * 
	 * @param user
	 * @param attributeValue
	 * @throws VOMSException
	 */
	public void setUserAttribute(User user, AttributeValue attributeValue)
			throws VOMSException;

	/**
	 * 
	 * @param user
	 * @param attributeName
	 * @throws VOMSException
	 */
	public void deleteUserAttribute(User user, String attributeName)
			throws VOMSException;

	/**
	 * 
	 * @param user
	 * @param attributeValue
	 * @throws VOMSException
	 */
	public void deleteUserAttribute(User user, AttributeValue attributeValue)
			throws VOMSException;

	/**
	 * 
	 * @param groupName
	 * @param attributeValue
	 * @throws VOMSException
	 */
	public void setGroupAttribute(String groupName,
			AttributeValue attributeValue) throws VOMSException;

	/**
	 * 
	 * @param groupName
	 * @param attributeName
	 * @throws VOMSException
	 */
	public void deleteGroupAttribute(String groupName, String attributeName)
			throws VOMSException;

	/**
	 * 
	 * @param groupName
	 * @param attributeValue
	 * @throws VOMSException
	 */
	public void deleteGroupAttribute(String groupName,
			AttributeValue attributeValue) throws VOMSException;

	/**
	 * 
	 * @param groupName
	 * @return
	 * @throws VOMSException
	 */
	public AttributeValue[] listGroupAttributes(String groupName)
			throws VOMSException;

	/**
	 * 
	 * @param groupName
	 * @param roleName
	 * @param attributeValue
	 * @throws VOMSException
	 */
	public void setRoleAttribute(String groupName, String roleName,
			AttributeValue attributeValue) throws VOMSException;

	/**
	 * 
	 * @param groupName
	 * @param roleName
	 * @param attrName
	 * @throws VOMSException
	 */
	public void deleteRoleAttribute(String groupName, String roleName,
			String attrName) throws VOMSException;

	/**
	 * 
	 * @param groupName
	 * @param roleName
	 * @param attributeValue
	 * @throws VOMSException
	 */
	public void deleteRoleAttribute(String groupName, String roleName,
			AttributeValue attributeValue) throws VOMSException;

	/**
	 * 
	 * @param groupName
	 * @param roleName
	 * @return
	 * @throws VOMSException
	 */
	public AttributeValue[] listRoleAttributes(String groupName, String roleName)
			throws VOMSException;

}
