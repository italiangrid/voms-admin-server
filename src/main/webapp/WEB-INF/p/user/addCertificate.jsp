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

<h1>Add a certificate for user <span class="userName">${shortName}</span></h1>
<tiles2:insertTemplate template="../shared_20/errorsAndMessages.jsp"/>
<%-- 
<h2>User information</h2>

<div>
  Name: <s:property value="name"/> <s:property value="surname"/>
</div>

<div>
  Email: <s:property value="emailAddress"/>
</div>
--%>

<s:form action="save-certificate" enctype="multipart/form-data" namespace="/user" method="POST">
  <s:token/>
  <s:hidden name="userId" value="%{model.id}"/>
  <h2>Enter a DN, CA couple:</h2>
  <div style="padding: 1em 2em 1em 2em; background-color: #F5F5F5">
    <s:textfield name="subject" size="45" label="Subject" labelposition="top"/>
    <s:select name="caSubject" list="#request.trustedCas" listKey="subjectString" listValue="subjectString" label="CA"/>
  </div>
  <h2>Or choose a PEM encoded X509 certificate from your file system:</h2>
  <div style="padding: 1em 2em 1em 2em; background-color: #f5f5f5">
    <s:file name="certificateFile" label="Certificate file"/>
    <s:submit value="%{'Add certificate'}"/>
  </div>
      
</s:form>