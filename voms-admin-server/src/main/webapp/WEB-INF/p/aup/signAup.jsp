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

<h1>VO Acceptable usage policy version ${activeVersion.version}</h1>

<s:url 
  action="index"
  namespace="/info" var="infoUrl"/>
  
  
<s:if test="! #attr.currentAdmin.voUser">
  You are not a member of this VO, you will see nothing around here.
  Click <s:a href="%{infoUrl}">here</s:a> to get more information on how you have
  been authenticated.
</s:if>
<s:elseif test="registrationDisabled()">
  Registration is disabled for this VO.
</s:elseif>
<s:else>
  <s:form 
    action="sign"
    validate="true">
    <s:token />

    <s:hidden
      name="aupId"
      value="%{model.id}" />
    <s:textarea
      rows="24"
      cols="100%"
      value="%{model.activeVersion.URLContent}"
      readonly="true" />
    <s:checkbox
      name="aupAccepted"
      label="I declare I have read and agree with the AUP terms displayed above"
      labelposition="right" />
    <s:submit
      value="%{'Submit'}"
      align="left" />

  </s:form>
</s:else>
