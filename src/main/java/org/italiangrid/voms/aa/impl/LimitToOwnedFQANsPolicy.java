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

import org.italiangrid.voms.VOMSAttribute;
import org.italiangrid.voms.aa.RequestContext;


public class LimitToOwnedFQANsPolicy implements FQANFilteringPolicy {
	
	public LimitToOwnedFQANsPolicy() {}

	@Override
	public boolean filterIssuedFQANs(RequestContext context) {

		if (context.getRequest().getOwnedAttributes().isEmpty())
			return false;
		
		boolean modified = false;
		
		for (VOMSAttribute a: context.getRequest().getOwnedAttributes()){
			
			// Only consider valid VOMS attributes for this VO
			if (!a.getVO().equals(context.getVOName()))
				continue;
			
			// Limit issued FQANs to the owned attributes
			modified = context.getResponse().getIssuedFQANs().retainAll(a.getFQANs());
		}
		
		return modified;
	}

}
