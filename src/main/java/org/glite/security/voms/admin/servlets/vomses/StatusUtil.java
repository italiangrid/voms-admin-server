package org.glite.security.voms.admin.servlets.vomses;

import java.io.IOException;

import org.glite.security.voms.admin.util.SysconfigUtil;
import org.italiangrid.voms.VOMSError;

public class StatusUtil {

	private static final String STATUS_CMD_TEMPLATE = "%s/etc/rc.d/init.d/voms-admin status %s";

	private static final String PREFIX = SysconfigUtil.getInstallationPrefix();

	private StatusUtil() {

	}

	public static boolean isActive(String voName) {
		String cmd = String.format(STATUS_CMD_TEMPLATE, PREFIX, voName);
		ProcessBuilder pb = new ProcessBuilder(cmd.split(" "));

		Process p;
		try {
			p = pb.start();
			int exitStatus = p.waitFor();
			return (exitStatus == 0);
		} catch (IOException e) {
			throw new VOMSError("Error retrieving VO status: "+e.getMessage(),e);
		} catch (InterruptedException e) {
			throw new VOMSError("Error retrieving VO status: "+e.getMessage(),e);
		}
	
	}

}
