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
package org.glite.security.voms.admin.tools;

import java.util.List;

public class SigningPolicyDescriptor {

	private String signingPolicyFileName;
	private String caHash;

	private String accessIdCA;
	private String posRights;
	private List<String> conditionalSubjects;

	public String getAccessIdCA() {

		return accessIdCA;
	}

	public void setAccessIdCA(String accessIdCA) {

		this.accessIdCA = accessIdCA;
	}

	public String getPosRights() {

		return posRights;
	}

	public void setPosRights(String posRights) {

		this.posRights = posRights;
	}

	public List<String> getConditionalSubjects() {

		return conditionalSubjects;
	}

	public void setConditionalSubjects(List<String> conditionalSubjects) {

		this.conditionalSubjects = conditionalSubjects;
	}

}
