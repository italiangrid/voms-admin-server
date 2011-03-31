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
<div class="reloadable">
<tiles2:insertTemplate template="../shared/errorsAndMessages.jsp"/>
<s:if test="aupAcceptanceRecords.empty">
No AUP acceptance records found.

	<s:if test="#request.registrationEnabled">
  		<voms:hasPermissions var="canSuspend" context="vo" permission="SUSPEND"/>
		<s:if test="#attr.canSuspend">
			<div style="text-align: right;">
				<s:form action="create-acceptance-record" onsubmit="ajaxSubmit(this,'aup-history-content'); return false;" theme="simple" cssStyle="display: inline">
					<s:token/>
					<s:hidden name="userId" value="%{model.id}" />
					<s:submit 
						value="%{'Sign AUP on behalf of user'}"	/>
				</s:form>
			</div>
		</s:if>
	</s:if>
</s:if>
<s:else>
  <table>
    <tr>
      <th>AUP info</th>
      <th>Last acceptance date</th>
    </tr>
    <s:iterator value="aupAcceptanceRecords" var="rec">
      <s:url action="load" namespace="/aup" var="saURL"/>
      <tr>
        <td>
          <dl>
            <dt>name:</dt>
            <dd>${rec.aupVersion.aup.name}</dd>
            <dt>version:</dt>
            <dd><a href="${saURL}">${rec.aupVersion.version}</a></dd>
            <s:if test="not valid">
             	<dt>Warning:</dt>
             	<dd class="aupWarning">
					This user has been requested to sign again the AUP.
             	</dd>
             </s:if>
             <s:else>
            	<dt>expiration date:</dt>
            	<dd>
                	<s:text name="format.datetime">
                  		<s:param value="expirationDate"/>
                	</s:text>
             	</dd>
             </s:else>                      	
          	</dl>
          
        </td>
        <td>
          <s:text name="format.datetime">
            <s:param value="lastAcceptanceDate"/>
          </s:text>
        </td>
      </tr>
    </s:iterator>
  </table>
  
  <s:if test="#request.registrationEnabled">
  	<voms:hasPermissions var="canSuspend" context="vo" permission="SUSPEND"/>
  	<div style="text-align: right;">
  		
  	
  		<s:if test="#attr.canSuspend">
		
		
			<s:if test="(not model.hasInvalidAUPAcceptanceRecord()) and (not model.hasPendingSignAUPTasks())">
				
				<s:form action="trigger-reacceptance" 
				onsubmit="ajaxSubmit(this,'aup-history-content'); return false;" 
				theme="simple" 
				cssStyle="display: inline;">
					<s:token/>
					<s:hidden name="userId" value="%{model.id}" />
					<s:submit 
					value="%{'Request AUP reacceptance'}"/>
				</s:form>
			
			
			
			</s:if>
		
			
				<s:form action="create-acceptance-record" onsubmit="ajaxSubmit(this,'aup-history-content'); return false;" theme="simple" cssStyle="display: inline">
					<s:token/>
					<s:hidden name="userId" value="%{model.id}" />
					<s:submit 
						value="%{'Sign AUP on behalf of user'}"	/>
				</s:form>
			
		
		</s:if>
		
	</div>
	</s:if>	
</s:else>
</div>

