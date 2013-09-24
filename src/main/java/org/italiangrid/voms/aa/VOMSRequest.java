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

package org.italiangrid.voms.aa;

import java.security.cert.X509Certificate;
import java.util.List;

import org.italiangrid.voms.VOMSAttribute;


public interface VOMSRequest {

	public String getRequesterSubject();
	public void setRequesterSubject(String requesterSubject);
	
	public String getRequesterIssuer();
	public void setRequesterIssuer(String requesterIssuer);
	
	public String getHolderSubject();
	public void setHolderSubject(String holderSubject);
	
	public String getHolderIssuer();
	public void setHolderIssuer(String holderIssuer);
	
	public X509Certificate getHolderCert();
	public void setHolderCert(X509Certificate holderCert);
	
	public List<String> getRequestedFQANs();
	public void setRequestedFQANs(List<String> fqans);
	
	public List<VOMSAttribute> getOwnedAttributes();
	public void setOwnedAttributes(List<VOMSAttribute> attrs);
	
	public long getRequestedValidity();
	public void setRequestedValidity(long validity);
	
	public List<String> getTargets();
	public void setTargets(List<String> targets);
	
}
