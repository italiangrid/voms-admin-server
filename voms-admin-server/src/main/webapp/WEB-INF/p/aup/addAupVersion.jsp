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

<h1> Add a new version for '<s:property value="name"/>' AUP</h1> 

<s:actionerror/>

<s:form validate="true">
  <s:token/>
  <s:hidden name="aupId" value="%{model.id}"/>
  <s:textfield name="version" label="Version"/>
  <s:textfield name="url" label="URL" size="40"/>
  <s:submit value="%{'Add version'}" align="left"/>
</s:form>