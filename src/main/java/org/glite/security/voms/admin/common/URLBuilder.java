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
