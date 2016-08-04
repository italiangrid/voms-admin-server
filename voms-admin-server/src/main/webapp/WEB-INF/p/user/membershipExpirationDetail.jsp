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

<s:if test="not #attr.disableMembershipEndTime">
  <s:if test="hasExpired()">
    <span class="blabel blabel-invert-important">Membership expired <s:date
        name="endTime"
        nice="true"
        format="struts.date.format.past" />
    </span>
  </s:if>
  <s:else>
    <span class="blabel blabel-invert">Membership expires <s:date
        name="endTime"
        nice="true"
        format="struts.date.format.future" />
    </span>
  </s:else>
</s:if>