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
package org.glite.security.voms.admin.operations;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

/**
 * This class represents a VOMS permission that can be assigned to a VOMS-Admin administrator.
 * A {@link VOMSPermission} is fixed-length sequence of permission flags that describe the set of permissions a VOMS Administrator has in a specific context.
 * 
 * The currently defined flags are:
 * 
 * <ul>
<li> <code>CONTAINER_READ, CONTAINER_WRITE</code>: These flags are used to control access to the operations that list/alter the VO internal structure (groups and roles list/creations/deletions, user creations/deletions).
</li> <li> <code>MEMBERSHIP_READ, MEMBERSHIP_WRITE</code>: These flags are used to control  access to operations that manage/list membership in group and roles. 
</li> <li> <code>ATTRIBUTES_READ,ATTRIBUTES_WRITE</code>: These flags are used to control access to operations that mange generic attributes (at the user, group, or role level).
</li> <li> <code>ACL_READ,ACL_WRITE,ACL_DEFAULT</code>: These flags are used to control  access to operations that manage VO ACLs and default ACLs.
</li> <li> <code>REQUESTS_READ,&nbsp;REQUESTS_WRITE</code>: These flags are used to control  access to operations that  manage subscription requests regarding the VO,  group membership,  role assignment etc...
</li> <li> <code>PERSONAL_INFO_READ, PERSONAL_INFO_WRITE</code>: The flags are used to control  access to user personal information stored in the database.
</li> <li> <code>SUSPEND</code>: This flag controls who can suspend other users.
</li></ul>
 *  
 * @author <a href="mailto:andrea.ceccanti@cnaf.infn.it">Andrea Ceccanti</a>
 *
 */
public class VOMSPermission implements Serializable, Cloneable {

	private static final long serialVersionUID = 1L;
	
	
	
	public static final int CONTAINER_READ = 1;

	public static final int CONTAINER_WRITE = 2;

	public static final int MEMBERSHIP_READ = 4;

	public static final int MEMBERSHIP_WRITE = 8;

	public static final int ACL_READ = 16;

	public static final int ACL_WRITE = 32;

	public static final int ACL_DEFAULT = 64;

	public static final int REQUESTS_READ = 128;

	public static final int REQUESTS_WRITE = 256;

	public static final int ATTRIBUTES_READ = 512;

	public static final int ATTRIBUTES_WRITE = 1024;

	public static final int PERSONAL_INFO_READ = 2048;
	public static final int PERSONAL_INFO_WRITE = 4096;

	public static final int SUSPEND = 8192;

	private static final int NUM_PERMISSIONS = 14;

	private static final int ALL_PERMISSION_MASK = ~0 >>> (32 - NUM_PERMISSIONS);

	
	public static String asString(int bits) {

		VOMSPermission p = new VOMSPermission(bits);
		return p.toString();
	}

	public static VOMSPermission fromBits(int bits) {

		if (bits <= 0)
			throw new IllegalArgumentException(
					"Permission must be a positive integer.");

		VOMSPermission perm = new VOMSPermission();

		for (int i = 0; i < NUM_PERMISSIONS; i++) {
			int PERM_MASK = 1 << i;
			if ((bits & PERM_MASK) == PERM_MASK)
				perm.set(PERM_MASK);
		}

		return perm;
	}

	public static VOMSPermission fromString(String permString) {

		VOMSPermission perm = new VOMSPermission();

		String[] perms = StringUtils.split(permString, '|');

		if (perms.length == 1 && perms[0].equals(""))
			return perm;

		return fromStringArray(perms);

	}

	public static VOMSPermission fromStringArray(String[] perms) {

		VOMSPermission perm = new VOMSPermission();

		for (int i = 0; i < perms.length; i++) {

			// Skip empty permissions
			if (perms[i].trim().equals(""))
				continue;

			if (perms[i].equals("CONTAINER_READ"))
				perm.setContainerReadPermission();
			else if (perms[i].equals("CONTAINER_WRITE"))
				perm.setContainerWritePermission();
			else if (perms[i].equals("MEMBERSHIP_READ"))
				perm.setMembershipReadPermission();
			else if (perms[i].equals("MEMBERSHIP_WRITE"))
				perm.setMembershipWritePermission();
			else if (perms[i].equals("ACL_READ"))
				perm.setACLReadPermission();
			else if (perms[i].equals("ACL_WRITE"))
				perm.setACLWritePermission();
			else if (perms[i].equals("ACL_DEFAULT"))
				perm.setACLDefaultPermission();
			else if (perms[i].equals("REQUESTS_READ"))
				perm.setRequestsReadPermission();
			else if (perms[i].equals("REQUESTS_WRITE"))
				perm.setRequestsWritePermission();
			else if (perms[i].equals("ATTRIBUTES_READ"))
				perm.setAttributesReadPermission();
			else if (perms[i].equals("ATTRIBUTES_WRITE"))
				perm.setAttributesWritePermission();
			else if (perms[i].equals("PERSONAL_INFO_READ"))
				perm.setPersonalInfoReadPermission();
			else if (perms[i].equals("PERSONAL_INFO_WRITE"))
				perm.setPersonalInfoWritePermission();
			else if (perms[i].equals("SUSPEND"))
				perm.setSuspendPermission();
			else if (perms[i].equals("ALL"))
				perm.setAllPermissions();
			else if (perms[i].equals("NONE"))
				perm.setEmptyPermissions();
			else
				throw new IllegalArgumentException(
						"Unknown permission passed as argument: " + perms[i]);
		}

		return perm;

	}

	public static VOMSPermission getAllPermissions() {

		return new VOMSPermission().setAllPermissions();
	}

	public static VOMSPermission getAttributesRWPermissions() {

		return new VOMSPermission().setAttributesReadPermission()
				.setAttributesWritePermission();
	}

	public static VOMSPermission getContainerReadPermission() {

		return new VOMSPermission().setContainerReadPermission();
	}

	public static VOMSPermission getContainerRWPermissions() {

		return new VOMSPermission().setContainerReadPermission()
				.setContainerWritePermission();
	}

	public static VOMSPermission getEmptyPermissions() {

		return new VOMSPermission().setEmptyPermissions();
	}

	public static VOMSPermission getMembershipRWPermissions() {

		return new VOMSPermission().setMembershipReadPermission()
				.setMembershipWritePermission();
	}

	public static VOMSPermission getRequestsRWPermissions() {

		return new VOMSPermission().setRequestsReadPermission()
				.setRequestsWritePermission();
	}

	public static VOMSPermission getSuspendPermissions() {
		return new VOMSPermission().setSuspendPermission();
	}

	private int bits = 0;

	public VOMSPermission() {

	}

	public VOMSPermission(int bits) {

		this.bits = bits;
	}

	private List buildPermList() {

		ArrayList permList = new ArrayList();

		if (test(CONTAINER_READ))
			permList.add("CONTAINER_READ");

		if (test(CONTAINER_WRITE))
			permList.add("CONTAINER_WRITE");

		if (test(MEMBERSHIP_READ))
			permList.add("MEMBERSHIP_READ");

		if (test(MEMBERSHIP_WRITE))
			permList.add("MEMBERSHIP_WRITE");

		if (test(ACL_READ))
			permList.add("ACL_READ");

		if (test(ACL_WRITE))
			permList.add("ACL_WRITE");

		if (test(ACL_DEFAULT))
			permList.add("ACL_DEFAULT");

		if (test(REQUESTS_READ))
			permList.add("REQUESTS_READ");

		if (test(REQUESTS_WRITE))
			permList.add("REQUESTS_WRITE");

		if (test(ATTRIBUTES_READ))
			permList.add("ATTRIBUTES_READ");

		if (test(ATTRIBUTES_WRITE))
			permList.add("ATTRIBUTES_WRITE");

		if (test(PERSONAL_INFO_READ))
			permList.add("PERSONAL_INFO_READ");

		if (test(PERSONAL_INFO_WRITE))
			permList.add("PERSONAL_INFO_WRITE");

		if (test(SUSPEND))
			permList.add("SUSPEND");

		return permList;

	}

	public Object clone() throws CloneNotSupportedException {

		VOMSPermission clone = (VOMSPermission) super.clone();
		clone.bits = this.bits;
		return clone;
	}

	public boolean equals(Object other) {

		if (this == other)
			return true;

		if (!(other instanceof VOMSPermission))
			return false;

		VOMSPermission that = (VOMSPermission) other;
		return (this.bits == that.bits);

	}

	public int getBits() {

		return bits;
	}

	public String getCompactRepresentation() {

		StringBuffer buf = new StringBuffer();

		if (bits == 0)
			return "NONE";

		if (test(ALL_PERMISSION_MASK))
			return "ALL";

		// Container perms
		buf.append("C:");
		buf.append(test(CONTAINER_READ) ? "r" : "-");
		buf.append(test(CONTAINER_WRITE) ? "w" : "-");

		buf.append(" M:");
		buf.append(test(MEMBERSHIP_READ) ? "r" : "-");
		buf.append(test(MEMBERSHIP_WRITE) ? "w" : "-");

		buf.append(" Acl:");
		buf.append(test(ACL_READ) ? "r" : "-");
		buf.append(test(ACL_WRITE) ? "w" : "-");
		if (test(ACL_DEFAULT))
			buf.append("d");

		buf.append(" Attrs:");
		buf.append(test(ATTRIBUTES_READ) ? "r" : "-");
		buf.append(test(ATTRIBUTES_WRITE) ? "w" : "-");

		buf.append(" Req:");
		buf.append(test(REQUESTS_READ) ? "r" : "-");
		buf.append(test(REQUESTS_WRITE) ? "w" : "-");

		buf.append(" Info:");
		buf.append(test(PERSONAL_INFO_READ) ? "r" : "-");
		buf.append(test(PERSONAL_INFO_WRITE) ? "w" : "-");

		buf.append(" Susp:");
		buf.append(test(SUSPEND) ? "y" : "-");

		return buf.toString();

	}

	public boolean has(String permString) {

		if (permString == null)
			return false;

		if (permString.equals("CONTAINER_READ"))
			return test(CONTAINER_READ);
		else if (permString.equals("CONTAINER_WRITE"))
			return test(CONTAINER_WRITE);
		else if (permString.equals("MEMBERSHIP_READ"))
			return test(MEMBERSHIP_READ);
		else if (permString.equals("MEMBERSHIP_WRITE"))
			return test(MEMBERSHIP_WRITE);
		else if (permString.equals("ACL_READ"))
			return test(ACL_READ);

		else if (permString.equals("ACL_WRITE"))
			return test(ACL_WRITE);

		else if (permString.equals("ACL_DEFAULT"))
			return test(ACL_DEFAULT);

		else if (permString.equals("REQUESTS_READ"))
			return test(REQUESTS_READ);

		else if (permString.equals("REQUESTS_WRITE"))
			return test(REQUESTS_WRITE);

		else if (permString.equals("ATTRIBUTES_READ"))
			return test(ATTRIBUTES_READ);

		else if (permString.equals("ATTRIBUTES_WRITE"))
			return test(ATTRIBUTES_WRITE);

		else if (permString.equals("PERSONAL_INFO_READ"))
			return test(PERSONAL_INFO_READ);

		else if (permString.equals("PERSONAL_INFO_WRITE"))
			return test(PERSONAL_INFO_WRITE);

		else if (permString.equals("SUSPEND"))
			return test(SUSPEND);
		else
			throw new IllegalArgumentException(
					"Unknown permission passed as argument: " + permString);
	}

	public boolean hasACLDefaultPermission() {

		return test(ACL_DEFAULT);
	}

	public boolean hasACLReadPermission() {

		return test(ACL_READ);
	}

	public boolean hasACLWritePermission() {

		return test(ACL_WRITE);
	}

	public boolean hasAttributeReadPermission() {

		return test(ATTRIBUTES_READ);
	}

	public boolean hasAttributeWritePermission() {

		return test(ATTRIBUTES_WRITE);
	}

	public boolean hasContainerReadPermission() {

		return test(CONTAINER_READ);
	}

	public boolean hasContainerWritePermission() {

		return test(CONTAINER_WRITE);
	}

	public int hashCode() {

		return new Integer(bits).hashCode();

	}

	public boolean hasMembershipReadPermission() {

		return test(MEMBERSHIP_READ);
	}

	public boolean hasMembershipWritePermission() {

		return test(MEMBERSHIP_WRITE);
	}

	public boolean hasPersonalInfoReadPermission() {
		return test(PERSONAL_INFO_READ);
	}

	public boolean hasPersonalInfoWritePermission() {
		return test(PERSONAL_INFO_WRITE);
	}

	public boolean hasRequestReadPermission() {

		return test(REQUESTS_READ);
	}

	public boolean hasRequestWritePermission() {

		return test(REQUESTS_WRITE);
	}

	public boolean hasSuspendPermission() {

		return test(SUSPEND);
	}

	public boolean satisfies(VOMSPermission other) {

		int perms = bits & other.getBits();
		return (perms == other.getBits());
	}

	protected VOMSPermission set(int permission) {

		if (permission <= 0)
			throw new IllegalArgumentException(
					"Permission must be a positive integer.");
		bits |= permission;
		return this;
	}

	public VOMSPermission setACLDefaultPermission() {

		set(ACL_DEFAULT);
		return this;
	}

	public VOMSPermission setACLReadPermission() {

		set(ACL_READ);
		return this;
	}

	public VOMSPermission setACLWritePermission() {

		set(ACL_WRITE);
		return this;
	}

	public VOMSPermission setAllPermissions() {

		set(ALL_PERMISSION_MASK);
		return this;
	}

	public VOMSPermission setAttributesReadPermission() {

		set(ATTRIBUTES_READ);
		return this;
	}

	public VOMSPermission setAttributesWritePermission() {

		set(ATTRIBUTES_WRITE);
		return this;
	}

	public VOMSPermission setContainerReadPermission() {

		set(CONTAINER_READ);
		return this;
	}

	public VOMSPermission setContainerWritePermission() {

		set(CONTAINER_WRITE);
		return this;
	}

	public VOMSPermission setEmptyPermissions() {

		bits = 0;
		return this;
	}

	public VOMSPermission setMembershipReadPermission() {

		set(MEMBERSHIP_READ);
		return this;
	}

	public VOMSPermission setMembershipRWPermission() {

		set(MEMBERSHIP_READ);
		set(MEMBERSHIP_WRITE);
		return this;

	}

	public VOMSPermission setMembershipWritePermission() {

		set(MEMBERSHIP_WRITE);
		return this;
	}

	public VOMSPermission setPermissions(int bits) {

		set(bits);
		return this;
	}

	public VOMSPermission setPersonalInfoReadPermission() {

		set(PERSONAL_INFO_READ);
		return this;
	}

	public VOMSPermission setPersonalInfoWritePermission() {

		set(PERSONAL_INFO_WRITE);
		return this;
	}

	public VOMSPermission setRequestsReadPermission() {

		set(REQUESTS_READ);
		return this;
	}

	public VOMSPermission setRequestsWritePermission() {

		set(REQUESTS_WRITE);
		return this;
	}

	public VOMSPermission setSuspendPermission() {

		set(SUSPEND);
		return this;
	}

	protected boolean test(int permission) {

		if (permission <= 0)
			throw new IllegalArgumentException(
					"Permission must be a positive integer.");
		return ((bits & permission) == permission);
	}

	public String toBinaryString() {

		return Integer.toBinaryString(bits);
	}

	public String toString() {

		if (test(ALL_PERMISSION_MASK))
			return "ALL";

		if (bits == 0)
			return "NONE";

		List permList = buildPermList();

		String result = StringUtils.join(permList.iterator(), ",");

		permList.clear();
		permList = null;

		return result;
	}

	public String[] toStringArray() {

		if (bits == 0)
			return null;

		List permList = buildPermList();
		String[] result = new String[permList.size()];

		permList.toArray(result);

		permList.clear();
		permList = null;

		return result;

	}

	public List<String> toStringList() {
		List<String> result = new ArrayList<String>();
		if (bits == 0)
			return result;

		return buildPermList();

	}

	protected VOMSPermission unset(int permission) {

		if (permission <= 0)
			throw new IllegalArgumentException(
					"Permission must be a positive integer.");
		bits &= ~permission;
		return this;
	}

	public VOMSPermission unsetACLDefaultPermission() {

		unset(ACL_DEFAULT);
		return this;
	}

	public VOMSPermission unsetACLReadPermission() {

		unset(ACL_READ);
		return this;
	}

	public VOMSPermission unsetACLWritePermission() {

		unset(ACL_WRITE);
		return this;
	}

	public VOMSPermission unsetAttributesReadPermission() {

		unset(ATTRIBUTES_READ);
		return this;
	}

	public VOMSPermission unsetAttributesWritePermission() {

		unset(ATTRIBUTES_WRITE);
		return this;
	}

	public VOMSPermission unsetContainerReadPermission() {

		unset(CONTAINER_READ);
		return this;
	}

	public VOMSPermission unsetContainerWritePermission() {

		unset(CONTAINER_WRITE);
		return this;
	}

	public VOMSPermission unsetMembershipReadPermission() {

		unset(MEMBERSHIP_READ);
		return this;
	}

	public VOMSPermission unsetMembershipWritePermission() {

		unset(MEMBERSHIP_WRITE);
		return this;
	}

	public VOMSPermission unsetRequestsReadPermission() {

		unset(REQUESTS_READ);
		return this;
	}

	public VOMSPermission unsetRequestsWritePermission() {

		unset(REQUESTS_WRITE);
		return this;
	}
	
	public void limitToPermissions(VOMSPermission limit){
		if (limit == null)
			return;
		
		bits &= limit.getBits();
	}
	
}
