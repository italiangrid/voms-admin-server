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

<s:if test="#attr.disableMembershipEndTime">
Membership expiration time support is disabled for this VO.
</s:if>
<s:else>
  <h1>
    Change membership expiration time for
    <s:property value="shortName" />
  </h1>

  <s:if test="endTime == null">
  This membership currently DOES NOT expire.
  </s:if>

  <s:else>
  This membership expires on <s:date name="endTime" /> (<s:date
      name="endTime"
      nice="true" />).    
  </s:else>

  <s:form
    action="set-membership-expiration"
    namespace="/user">

    <s:token />

    <s:date
      name="proposedExpirationDate"
      var="ped"
      format="MM/dd/yyyy" />

    <div style="margin-top: 1em;">

      <s:hidden
        name="userId"
        value="%{id}" />

      <s:label
        for="ced_exp_date"
        value="Set membership expiration date (months/day/year) to:"
        cssClass="input-label" />

      <s:textfield
        id="ced_exp_date"
        name="expirationDate"
        size="10"
        cssClass="ced-input-date"
        value="%{#ped}" />



      <s:submit
        value="%{'Change expiration date'}"
        cssClass="ced-submit-button" />

      <s:submit
        value="%{'Remove membership expiration date'}"
        cssClass="ced-submit-button"
        onclick="ced_null_expiration();" />
    </div>

  </s:form>
  <script type="text/javascript">
			function ced_null_expiration() {
				$('#ced_exp_date').val('');
				return;
			};

			$('.ced-input-date').datepicker();
		</script>

</s:else>