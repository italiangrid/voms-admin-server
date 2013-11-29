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

package org.italiangrid.voms.aa.impl;

import org.glite.security.voms.admin.util.PathNamingScheme;


public class LegacyFQANEncoding implements FQANEncoding {

	private final String NULL_CAPABILITY = "Capability=NULL";
	private final String NULL_ROLE = "Role=NULL";
	
	@Override
	public String encodeFQAN(String fqan) {
		String result = null;
		
		if (PathNamingScheme.isQualifiedRole(fqan)) {
			result = String.format("%s/%s", fqan, NULL_CAPABILITY);
		} else if (PathNamingScheme.isGroup(fqan)) {
			result = String.format("%s/%s/%s", fqan, NULL_ROLE, NULL_CAPABILITY);
		}
		
		return result;
	}

	@Override
	public String decodeFQAN(String fqan) {
		int index = fqan.indexOf("/"+NULL_ROLE); 
		
		if ( index > 0)
			return fqan.substring(0, index);
		
		return fqan.substring(0, fqan.indexOf("/"+NULL_CAPABILITY));
		
	}

}
