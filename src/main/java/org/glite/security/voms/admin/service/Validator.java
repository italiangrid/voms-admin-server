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
 * 
 *   
 *     Andrea Ceccanti - andrea.ceccanti@cnaf.infn.it
 *******************************************************************************/
package org.glite.security.voms.admin.service;

import java.util.regex.Pattern;

import org.glite.security.voms.User;

public class Validator {

	public static final String VALID_INPUT_PATTERN = "^[^<>&=]*$";
	public static final String VALID_DN_PATTERN = "^[^<>&]*$";

	public static final Pattern inputPattern = Pattern
			.compile(VALID_INPUT_PATTERN);
	public static final Pattern dnPattern = Pattern.compile(VALID_DN_PATTERN);

	public static void validateInputString(String inputString,
			String errorMessage) {
		assert inputString != null : "Cannot validate a null input string!";

		if (!inputPattern.matcher(inputString).matches())
			throw new IllegalArgumentException(errorMessage);

	}

	public static void validateDN(String dnString, String errorMessage) {
		assert dnString != null : "Cannot validate a null input string!";

		if (!dnPattern.matcher(dnString).matches())
			throw new IllegalArgumentException(errorMessage);

	}

	public static void validateUser(User u) {

		validateDN(u.getDN(), "Invalid characters in user's DN!");
		if (u.getCN() != null)
			validateDN(u.getCN(), "Invalid characters in user's CN!");
		if (u.getMail() != null)
			validateDN(u.getMail(), "Invalid characters in user's EMAIL!");
	}
}
