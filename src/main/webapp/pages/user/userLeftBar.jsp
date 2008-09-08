<%--
 Copyright (c) Members of the EGEE Collaboration. 2006.
 See http://www.eu-egee.org/partners/ for details on the copyright
 holders.

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.

 Authors:
     Andrea Ceccanti - andrea.ceccanti@cnaf.infn.it
--%>
<%@ taglib uri="http://org.glite.security.voms.tags" prefix="voms"%>

<div class="userLeftBar">

<voms:link 
	action="/SearchUser"
	context="vo"permission="CONTAINER_READ|MEMBERSHIP_READ" 
	disabledStyleClass="disabledLink"
	styleClass="vomsLink"
	>
	Search users
</voms:link>	

<voms:link 
	action="/PreCreateUser" 
	context="vo"permission="CONTAINER_READ|CONTAINER_WRITE|MEMBERSHIP_READ|MEMBERSHIP_WRITE" 
	disabledStyleClass="disabledLink"
	styleClass="vomsLink"
	>
	Create a user
</voms:link>
</div>

