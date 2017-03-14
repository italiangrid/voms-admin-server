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


<s:if test="hasPendingSignAUPTasks()">
  <div class="badge-container">
    <span class="blabel blabel-warning baseline">Pending sign AUP request</span>

    <s:set
      var="aupTaskExpired"
      value="getPendingSignAUPTask(#attr.defaultAUP).expiryDateInThePast()" />

    <s:set
      var="aupTaskExpirationDate"
      value="getPendingSignAUPTask(#attr.defaultAUP).expiryDate" />

    <s:set
      var="daysBeforeExpiration"
      value="getPendingSignAUPTask(#attr.defaultAUP).daysBeforeExpiration" />

    <s:if test="#aupTaskExpired">
      <span class="blabel blabel-invert-important"> expired <s:date
          nice="true"
          format="struts.date.format.past"
          name="#aupTaskExpirationDate" />
      </span>
    </s:if>
    <s:else>
      <span class="blabel blabel-invert">expires <s:date
          nice="true"
          format="struts.date.format.future"
          name="#aupTaskExpirationDate" />
      </span>
    </s:else>
  </div>
</s:if>
<s:else>
  <div class="badge-container">
    <s:iterator
      value="aupAcceptanceRecords.{? #this.aupVersion == #attr.defaultAUP.activeVersion}">
      <s:if test="hasExpired()">
        <span class="blabel blabel-invert-important"> AUP signature
          expired <s:date
            name="expirationDate"
            nice="true"
            format="struts.date.format.past" />
        </span>
      </s:if>
      <s:else>
        <span class="blabel blabel-invert"> AUP signature expires <s:date
            name="expirationDate"
            nice="true"
            format="struts.date.format.future" />
        </span>
      </s:else>
    </s:iterator>
  </div>
</s:else>