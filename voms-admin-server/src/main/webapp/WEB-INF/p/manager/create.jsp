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
<h1>Create a new Group Manager</h1>

<s:if test="voGroups.size() == 0">
  No groups defined (besides the VO root group) for this VO.
</s:if>
<s:else>
<p>All the fields below are required.</p>
<s:form validate="true">

  <s:token/>
  <s:textfield 
    name="name" 
    label="Name" 
    size="40"
    value="%{name}" 
    cssClass="registrationField"/>
  
  <s:textfield 
      name="Description" 
      disabled="false" 
      label="Description"
      size="40" 
      value="%{description}" 
      cssClass="registrationField"/>
      
  
  <s:textfield 
      name="emailAddress" 
      disabled="false" 
      label="Email"
      size="40" 
      value="%{emailAddress}" 
      cssClass="registrationField"/>
  
  <h2>For group:</h2>
  <s:select list="voGroups" 
    listKey="id" 
    listValue="name"
    name="groupId"/>
  
  <s:submit 
    value="%{'Create Group Manager'}"/>
</s:form>
</s:else>

