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

<s:if test="hasPendingSignAUPTasks()">
  
  <span class="blabel blabel-warning baseline">Pending sign AUP request</span>
  
  <s:set var="daysBeforeExpiration" value="getPendingSignAUPTask(#attr.defaultAUP).daysBeforeExpiration"/>
  
  <s:if test="#daysBeforeExpiration < 0">
   <span class="badge badge-error" style="font-size: smaller;">expired</span>
  </s:if>
  <s:elseif test="#daysBeforeExpiration < 3">
    <span class="badge badge-error" style="font-size: smaller;"><s:property value="#daysBeforeExpiration"/> days left</span>
  </s:elseif> 
  <s:else>
    <span class="badge badge-info" style="font-size: smaller;"><s:property value="#daysBeforeExpiration"/> days left</span>
  </s:else>
  
</s:if>
