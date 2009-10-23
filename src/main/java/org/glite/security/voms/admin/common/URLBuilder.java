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
package org.glite.security.voms.admin.common;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class URLBuilder {

	public static String baseVOMSURL() {

		InetAddress addr;
		try {

			addr = InetAddress.getLocalHost();

		} catch (UnknownHostException e) {
			throw new VOMSFatalException(
					"Error getting local host inet address!", e);
		}

		String voName = VOMSConfiguration.instance().getVOName();
		String hostName;

		if (addr.getCanonicalHostName().startsWith("localhost"))
			hostName = addr.getHostAddress();
		else
			hostName = addr.getCanonicalHostName();

		String portNumber = "8443";

		return String.format("https://%s:%s/voms/%s", hostName, portNumber,
				voName);

	}

}
