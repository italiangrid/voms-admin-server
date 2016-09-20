<%--

    Copyright (c) Istituto Nazionale di Fisica Nucleare (INFN). 2006-2016

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

--%>
<%@include file="/WEB-INF/p/shared/taglibs.jsp"%>

<voms:hasPermissions var="canReadPI" context="vo" permission="PERSONAL_INFO_READ"/>

<s:if test="#attr.canReadPI or (#attr.currentAdmin.is(model))">
  <div id="membership-expiration-pane">
    <div class="reloadable">
        
        <dl>
            <dt>Membership expiration date (mm/dd/yyyy):</dt>
            <dd class="userMembershipEndTime">
              
              
              <s:text name="format.date" var="userEndTime">
                <s:param value="endTime"/>
              </s:text>
              
              <voms:hasPermissions var="canDelete" context="vo" permission="rw"/>
              
                    
              <s:form theme="simple" 
                action="set-membership-expiration"
                onsubmit="ajaxSubmit(this,'membership-expiration-pane'); return false;">
                <s:token/>
                <s:hidden name="userId" value="%{id}"/>
                <s:textfield id="membershipExpirationField" 
                  name="expirationDate" 
                  value="%{#userEndTime}" 
                  size="10" 
                  cssClass="membershipExpiration"
                  readonly="%{#attr.canDelete == false or #attr.readOnlyMembershipExpiration == true}"
                  />
                
                  <s:submit value="%{'Change'}" disabled="%{#attr.canDelete == false or #attr.readOnlyMembershipExpiration == true}"/>
                
			  </s:form>
              <s:fielderror fieldName="expirationDate"/>
              <tiles2:insertTemplate template="../shared/errorsAndMessages.jsp"/>
          </dd>
        </dl>
    </div>
  </div>
</s:if>