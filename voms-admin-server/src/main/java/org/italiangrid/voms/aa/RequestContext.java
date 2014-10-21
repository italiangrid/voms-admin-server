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

import org.glite.security.voms.admin.persistence.model.VOMSUser;


public interface RequestContext {

	public boolean isHandled();
	public void setHandled(boolean complete);
	
	public VOMSUser getVOMSUser();
	public void setVOMSUser(VOMSUser u);
	
	public VOMSRequest getRequest();
	public VOMSResponse getResponse();
	
	public String getVOName();
	public void setVOName(String vo);
	
	public String getHost();
	public void setHost(String hostname);
	
	public int getPort();
	public void setPort(int port);
	
}
