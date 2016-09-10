<%@include file="/WEB-INF/p/shared/taglibs.jsp"%>

<%-- 
  This is needed as this fragment is included both from user detail
  user home and user list page.
  In the home and user details the page, the reference to the user is 
  kept in the model.
  In the list users page is kept in #user.
 --%>

<s:if test="! #user">
  <s:set
    var="user"
    value="model" />
</s:if>

<voms:hasPermissions
  var="canReadPI"
  context="vo"
  permission="PERSONAL_INFO_READ" />

<div class="status-detail">
  <tiles2:insertTemplate template="suspensionDetail.jsp" />
  
  <s:if test="#attr.canReadPI or (#attr.currentAdmin.is(#user))">
    <tiles2:insertTemplate template="aupStatusDetail.jsp" />

    <s:if test="not #attr.disableMembershipEndTime">
      <tiles2:insertTemplate template="membershipExpirationDetail.jsp" />
    </s:if>
  </s:if>
</div>