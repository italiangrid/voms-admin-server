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
<h1>
  Welcome to the <span class="voName">${voName}</span> VO,
</h1>

<div id="welcomeUserName">
   
    <s:if test="model.requesterInfo.name != null and model.requesterInfo.surname != null">
    <s:property value="model.requesterInfo.name+ ' ' +model.requesterInfo.surname"/>
      <span class="institution">
          (<s:property value="model.requesterInfo.institution"/>)  
        </span>
  </s:if>
  <s:else>
    <s:property value="model.requesterInfo.certificateSubject"/>
  </s:else>
</div>

