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

<s:if test="not #request.registrationEnabled">
  The registration service and AUP management is currently disabled for this VO.
</s:if>
<s:else>

<h1>VO Acceptable Usage Policy:</h1>

<tiles2:insertTemplate template="../shared/errorsAndMessages.jsp"/>
  
<voms:hasPermissions var="canModify" context="vo" permission="rw"/>

<s:if test="model is null">
  No acceptable usage policy defined for this VO.
</s:if>
<s:else>
  
  <div  id="aclShowPane" style="position: relative;">
  
  	<s:textarea id="aclShowTextArea" rows="24" cols="100%" readonly="true" value="%{activeVersion.URLContent}" cssStyle="width: 100%"/>
  	
	<s:if test="#attr.canModify">  	
  		<s:form action="change-reacceptance-period" namespace="/aup" validate="true" cssStyle="clear:both; float: right">
      		<s:hidden name="aupId" value="%{id}"/>
      		<s:token/>

      		<s:textfield name="period" value="%{model.reacceptancePeriod}" label="Reacceptance period (in days)" size="4" labelposition="left"/>
      		<s:submit value="%{'Change'}" align="right" cssStyle="margin-top: 5px"/>
    	</s:form>
    </s:if>
   
  </div>
  
  <voms:hasPermissions var="canRead"
    context="vo"
    permission="CONTAINER_READ"/>
    
  <s:if test="#attr.canRead">
  <h2>Currently available aup versions:</h2>
  
  <div id="addAUPVersionPane">
	<s:if test="#attr.canModify">  	
  		<s:url action="add-version" method="input" namespace="/aup" var="voAddVersionURL">
    		<s:param name="aupId" value="id"/>
  		</s:url>
  		<s:a href="%{#voAddVersionURL}">
    		Add a new AUP version
  		</s:a>
  	</s:if>
  </div>
  <table>
    <tr>
      <th style="width: 10%">Version</th>
      <th style="width: 70%">URL</th>
      <th>Active</th>
      <th/>
    </tr>
    <s:iterator value="versions" var="version">
      <tr class="tableRow">
        <td class="aupVersion">
          <s:property value="version"/>
        </td>
        <td>
        	<s:property value="url"/>
        	<s:url action="edit-version!input" var="editAUPVersionURL">
        		<s:param name="aupId" value="model.id"/>
        		<s:param name="version" value="version"/>
        	</s:url>
        	
        	<s:a href="%{#editAUPVersionURL}" cssClass="changeAUPURL">change</s:a>
        	
        	 
        	 
        	<s:if test="versions.size > 1">
        		<span id="showAUPContent" class="clickable" onclick="showAUPContent(this)">show content</span>
        		<span style="display:none"><s:property value="URLContent"/></span>
        	</s:if>
        	
        </td>
        	
        <td>
          <s:if test="active">
            <span class="highlight">active</span>
          </s:if>
          <s:else>
          	<s:if test="#attr.canModify">
            	<s:form action="set-active-version" namespace="/aup" theme="simple">
            		<s:token/>
              		<s:hidden name="aupId" value="%{model.id}"/>
              		<s:hidden name="version" value="%{version}"/>
              		<s:submit value="%{'Set active'}"/>
            	</s:form>
            </s:if>
          </s:else>
        </td>
        
        <td class="actionCell">
        <s:if test="#attr.canModify">
          	<s:if test="%{versions.size > 1 and not active}">
            	<s:form action="remove-version" namespace="/aup" theme="simple">
            		<s:token/>
              		<s:hidden name="aupId" value="%{model.id}"/>
              		<s:hidden name="version" value="%{version}"/>
              		<s:submit value="%{'Remove'}" onclick="openConfirmDialog(this, 'deleteAUPVersionDialog','%{version}'); return false"/>
            	</s:form>
          </s:if>
          <s:if test="active">
          	<s:form action="trigger-acceptance" theme="simple">
          		<s:token/>
    			<s:submit value="%{'Trigger reacceptance'}" onclick="openConfirmDialog(this, 'triggerReacceptanceDialog','%{version}'); return false"/>      	
          	</s:form>
          </s:if>
          </s:if>
        </td>
      </tr>
    </s:iterator>
  </table> 
  </s:if>
</s:else>
</s:else>
