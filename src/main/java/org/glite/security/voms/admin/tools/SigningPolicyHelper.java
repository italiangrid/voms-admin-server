package org.glite.security.voms.admin.tools;

public class SigningPolicyHelper {

	private static SigningPolicyHelper instance = null;

	private SigningPolicyHelper() {

	}

	public static SigningPolicyHelper instance() {

		if (instance == null)
			instance = new SigningPolicyHelper();

		return instance;
	}

	public static void main(String[] args) {

	}

}
