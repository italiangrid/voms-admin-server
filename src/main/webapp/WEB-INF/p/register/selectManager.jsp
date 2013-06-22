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

<h1>
Manager selection
</h1>
<p>
Please select the VO manager that will handle your VO membership request from
the following list. <br/>
<strong>Important:</strong> If you don't select any manager, your request will
be forwarded to all VO managers that have the rights to handle it.
</p>

<s:form action="select-manager" namespace="/register">
    
  <s:hidden name="requestId" value="%{model.id}" />
  <s:hidden name="confirmationId" value="%{confirmationId}" />
  
  <table class="table">
    <tr>
      <th/>
      <th>Manager name (or alias)</th>
      <th>Description</th>
    </tr>
  <s:iterator value="#attr.groupManagers">
    <tr>
      <td width="5%">
        <input 
          type="radio"
          name="managerId"
          value="${id}"/>
       </td>
      <td>
        <s:property value="name"/>
      </td>
      <td>  
        <s:property value="description"/>
      </td>
    </tr>
  </s:iterator>
  </table>  
  <s:submit value="%{'Continue'}" />
</s:form>