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

<%@ include file="taglibs.jsp" %>

<div id="headerTitle">
	<div id="vaInfo">
		<table cellpadding="0" cellspacing="0" width="100%">
			<tr>
				<td valign="bottom" width="25%">
					<html:img page="/img/va-logo.png" style="float:left;"/>
				</td>
				<td align="left" valign="bottom" style="padding-left: .5em;">
					for VO:<span id="voName" style="padding-left:.5em">${voName}</span>
				</td>			
				<td align="right" valign="bottom" style="padding-left: .5em;">Current user:
								<span id='adminDN' title="${currentAdmin.realSubject}" style="padding-left:.5em">
									<voms:formatDN dn="${currentAdmin.realSubject}" fields="CN"/>
								</span>
				</td>
			</tr>
		</table>
	</div>
</div>




