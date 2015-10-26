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


<h1>Change HR database id for <s:property value="shortName"/></h1>



<s:form action="save-orgdb-id" 
  namespace="/user"
  validate="true"
  >
  <s:token/>
  <s:hidden name="userId" value="%{id}"/>
  <label class="label" for="_user_name">User:</label>
  <br></br>
  <div id="_user_name"><s:property value="shortName"/></div>
  <s:textfield id="orgDbIdVal" name="theOrgDbId" value="%{orgDbId}" size="20" label="CERN HR ID"/>
  <s:submit  align="left" value="%{'Change HR ID'}" onclick="confirmOrgDbIdChangeDialog(this, '%{shortName}'); return false"/>
</s:form>
