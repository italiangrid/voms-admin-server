<%--

    Copyright (c) Istituto Nazionale di Fisica Nucleare (INFN). 2006-2015

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


<h1>Edit group description</h1>

<voms:hasPermissions
  var="canCreate"
  context="vo"
  permission="CONTAINER_READ|CONTAINER_WRITE" />

<s:if test="not #attr.canCreate">
You do not have enough privileges to edit the group description.
</s:if>
<s:else>

  <div>
    <s:actionerror />

    <p>Edit group description for group:</p>
  
    <div class="req-fqan">
      <s:property value="name" />  
    </div>

    <s:form
      id="saveGroupDescription"
      action="save-group-description"
      namespace="/group"
      validate="true">

      <s:token />

      <s:hidden
        name="groupId"
        value="%{id}" />

      <s:textarea
        name="groupDescription"
        label="New description (can be empty)"
        value="%{description}"
        cols="78" />

      <s:submit
        value="%{'Update group description!'}"
        align="left" />
    </s:form>
  </div>

</s:else>