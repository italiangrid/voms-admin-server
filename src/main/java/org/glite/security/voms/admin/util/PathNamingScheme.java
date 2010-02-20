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
package org.glite.security.voms.admin.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.glite.security.voms.admin.configuration.VOMSConfiguration;
import org.glite.security.voms.admin.configuration.VOMSConfigurationConstants;
import org.glite.security.voms.admin.error.NullArgumentException;
import org.glite.security.voms.admin.error.VOMSSyntaxException;

/**
 * 
 * 
 * @author <a href="mailto:lorentey@elte.hu">Karoly Lorentey</a>
 * @author <a href="mailto:andrea.ceccanti@cnaf.infn.it">Andrea Ceccanti </a>
 * 
 * 
 */
public class PathNamingScheme {

	public static final Log log = LogFactory.getLog(PathNamingScheme.class);

	public static final String containerSyntax = "^(/[\\w.-]+)+|((/[\\w.-]+)+/)?(Role=[\\w.-]+)|(Capability=[\\w\\s.-]+)$";

	public static final String groupSyntax = "^(/[\\w.-]+)+$";

	public static final String roleSyntax = "^Role=[\\w.-]+$";

	public static final String qualifiedRoleSyntax = "^(/[\\w.-]+)+/Role=[\\w.-]+$";

	public static final String capabilitySyntax = "^Capability=[\\w\\s.-]+$";

	public static final Pattern containerPattern = Pattern
			.compile(containerSyntax);

	public static final Pattern groupPattern = Pattern.compile(groupSyntax);

	public static final Pattern rolePattern = Pattern.compile(roleSyntax);

	public static final Pattern qualifiedRolePattern = Pattern
			.compile(qualifiedRoleSyntax);

	public static final Pattern capabilityPattern = Pattern
			.compile(capabilitySyntax);

	public static void checkSyntax(String containerName) {

		if (containerName == null)
			throw new NullArgumentException("containerName ==  null");

		if (containerName.length() > 255)
			throw new VOMSSyntaxException("containerName.length() > 255");

		if (!containerPattern.matcher(containerName).matches())
			throw new VOMSSyntaxException("Syntax error in container name: "
					+ containerName);
	}

	public static void checkGroup(String groupName) {

		checkSyntax(groupName);

		if (!groupPattern.matcher(groupName).matches())
			throw new VOMSSyntaxException("Syntax error in group name: "
					+ groupName);
	}

	public static void checkRole(String roleName) {

		if (roleName == null)
			throw new NullArgumentException("roleName == null");

		if (roleName.length() > 255)
			throw new VOMSSyntaxException("roleName.length()>255");

		if (!rolePattern.matcher(roleName).matches())
			throw new VOMSSyntaxException("Syntax error in role name: "
					+ roleName);
	}

	public static String getParentGroupName(String groupName) {

		checkSyntax(groupName);

		if (StringUtils.countMatches(groupName, "/") == 1)
			return groupName;
		else
			return StringUtils.substringBeforeLast(groupName, "/");
	}

	public static String[] getParentGroupChain(String groupName) {

		checkSyntax(groupName);

		String[] tmp = groupName.split("/");
		String[] groupChain = (String[]) ArrayUtils
				.subarray(tmp, 1, tmp.length);

		if (groupChain.length == 1) {
			return new String[] { groupName };
		}

		String[] result = new String[groupChain.length - 1];

		if (result.length == 1) {
			result[0] = "/" + groupChain[0];
			return result;
		}

		for (int i = groupChain.length - 1; i > 0; i--)
			result[i - 1] = "/"
					+ StringUtils.join(ArrayUtils.subarray(groupChain, 0, i),
							"/");

		return result;
	}

	public static boolean isGroup(String groupName) {

		checkSyntax(groupName);

		String voName = "/"
				+ VOMSConfiguration.instance().getString(
						VOMSConfigurationConstants.VO_NAME);

		if (!groupName.startsWith(voName)) {
			log.error("Group name : " + groupName
					+ " does not start with vo name: " + voName);
			return false;
		}

		return groupPattern.matcher(groupName).matches();
	}

	public static boolean isRole(String roleName) {

		checkSyntax(roleName);
		return rolePattern.matcher(roleName).matches();
	}

	public static boolean isQualifiedRole(String roleName) {

		checkSyntax(roleName);
		return qualifiedRolePattern.matcher(roleName).matches();
	}

	public static String getRoleName(String containerName) {

		if (!isRole(containerName) && !isQualifiedRole(containerName))
			throw new VOMSSyntaxException("No role specified in \""
					+ containerName + "\" voms syntax.");

		Matcher m = containerPattern.matcher(containerName);

		if (m.matches()) {

			String roleGroup = m.group(4);
			return roleGroup.substring(roleGroup.indexOf("=") + 1, roleGroup
					.length());

		}

		return null;
	}

	public static String getGroupName(String containerName) {

		checkSyntax(containerName);

		// If it's a container and it's not a role or a qualified role, then
		// it's a group!
		if (!isRole(containerName) && !isQualifiedRole(containerName))
			return containerName;

		Matcher m = containerPattern.matcher(containerName);

		if (m.matches()) {
			String groupName = m.group(2);

			if (groupName.endsWith("/"))
				return groupName.substring(0, groupName.length() - 1);
			else
				return groupName;
		}

		return null;
	}

}
