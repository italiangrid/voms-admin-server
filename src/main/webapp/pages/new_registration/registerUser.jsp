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
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://org.glite.security.voms.tags" prefix="voms"%>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>

<div class="header1">
Welcome to voms-admin registration for the <span class="voName"> ${voName}</span> VO.
</div>

<p>
To access the VO resources, you must agree to the VO's Usage Rules. 
Please fill out all fields in the form below and click on the submit
button at the bottom of the page.
</p>
<p>
After you submit this request, you will receive an email with instructions on how to proceed. 
Your request will not be forwarded to the VO managers until you confirm that you have a valid email 
address by following those instructions.
</p>

<p>IMPORTANT:</p>
<p>
By submitting this information you agree that it may be distributed to and stored by 
VO and site administrators. You also agree that action may be taken to confirm the information you provide 
is correct, that it may be used for the purpose of controlling access to VO resources and that it may be 
used to contact you in relation to this activity.
</p>

<html:form action="/SubmitVOMembershipRequest" method="post" onsubmit="return validateSubmitRequestForm(this)">

			<table cellpadding="0" cellspacing="0" class="form">
						
				<tr>
					<td width="100%">
						<div class="regLabel">Your distinguished name (DN):</div>			
					</td>
				</tr>
				<tr>
					<td>
						<div id="regDN">
							${currentAdmin.realSubject}
						</div>
					</td>
				</tr>
				
				<tr>
					<td>
						<div class="regLabel">Your CA:</div>			
					</td>
				</tr>
				<tr>
					<td>
						<div id="regCA">
							${currentAdmin.realIssuer}
						</div>
					</td>
				</tr>
				
				<tr>
					<td>
						<div class="regLabel">Your email address:</div>			
					</td>
				</tr>
				<tr>
					<td>
						<div id="regEmail">
							<html:text property="emailAddress" size="50" styleClass="inputField" value="${currentAdmin.realEmailAddress}"/>
						</div>
					</td>
				</tr>
				
				<tr>
					<td>
						<div class="regLabel">Your institute:</div>			
					</td>
				</tr>
				<tr>
					<td>
						<html:text property="institute" size="50" styleClass="inputField"/>								
					</td>
				</tr>
				
				<tr>
					<td>
						<div class="regLabel">Your phone number:</div>			
					</td>
				</tr>
				<tr>
					<td>
						<html:text property="phoneNumber" size="50" styleClass="inputField"/>																
					</td>
				</tr>
				
				<tr>
					<td>
						<div class="regLabel">Comments for the VO admin:</div>			
					</td>
				</tr>
				<tr>
					<td>
						<html:text property="comments" size="50" styleClass="inputField"/>																								
					</td>
				</tr>
				
				<tr>
					<td>
						<html:checkbox property="aupAccepted"/> <span class="regLabel">You agree on the VO's usage rules.</span>
					</td>
				</tr>
				<tr><td/></tr>
				<tr><td/></tr>
				<tr>
					<td><html:submit value="Register!" styleClass="submitButton"/></td>
				</tr>
			</table>

</html:form>

<html:javascript formName="/SubmitVOMembershipRequest"/>	