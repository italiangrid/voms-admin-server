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

<h2>AUP version detail</h2>

<div>

  <div>
    Name:
  </div>
  <div>
    <s:property value="aup.name"/>
  </div>
  
  <div>
    Version:
  </div>
  <div>
    <s:property value="version"/>
  </div>
  
  <div>
    URL:
  </div>
  <div>
    <s:property value="url"/>
  </div>
  
  <s:if test="URLContent != null">
    <div>
      Content:
    </div>
    <div>
      <s:textarea rows="20" cols="80" value="%{model.URLContent}"/>
    </div>
  </s:if>  

</div>
