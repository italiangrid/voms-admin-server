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

<h2>Please enter a reason for user certificate suspension</h2>

<div>
  Certificate subject:
</div>
<div>
  <s:property value="certificate.subjectString"/>
</div>
<div>
  Certificate issuer:
</div>

<div>
  <s:property value="certificate.ca.subjectString"/>
</div>
  
<s:form action="suspend-certificate" namespace="/user">
  <s:token/>
  <s:hidden name="userId" value="%{model.id}"/>
  <s:hidden name="certificateId" value="%{certificate.id}"/>
  <s:textfield name="suspensionReason" label="Reason" size="20"/>
  <s:submit value="%{'Suspend user certificate'}"/>
</s:form>