<%--

    Copyright (c) Members of the EGEE Collaboration. 2006-2009.
    See http://www.eu-egee.org/partners/ for details on the copyright holders.

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
    	Andrea Ceccanti (INFN)

--%>
<%@include file="/WEB-INF/p/shared/taglibs.jsp"%>

<voms:hasPermissions var="canReadPI" context="vo" permission="PERSONAL_INFO_READ"/>

<s:if test="#attr.canReadPI or (#attr.currentAdmin.is(model))">
  <div id="membership-expiration-pane">
  	
    <div class="reloadable">
		<voms:hasPermissions var="canDelete" context="vo" permission="rw"/>
        
       	<s:text name="format.date" var="userEndTime">
       		<s:param value="endTime"/>
        </s:text>
        
		<span class="label">Membership expiration date (mm/dd/yyyy):</span>
		<span>${userEndTime}</span>
		
                        
          
		<s:form theme="simple" 
			action="extend-membership-expiration"
            onsubmit="ajaxSubmit(this,'membership-expiration-pane'); return false;"
            cssStyle="display:inline">
              
              <s:token/>
              <s:hidden name="userId" value="%{id}"/>                               
              	<s:submit 
              		value="%{'Extend membership'}" 
              		disabled="%{#attr.canDelete == false or #attr.readOnlyMembershipExpiration == true}" 
              		cssStyle="display:inline"
              		tooltip="Extends membership for the user starting from the current date for the default period configured for the VO (typically 12 months)."
              		/>
	  	</s:form>  
    </div>
	<tiles2:insertTemplate template="../shared/errorsAndMessages.jsp"/>    
  </div>
</s:if>

