<%@include file="/WEB-INF/p/shared/taglibs.jsp"%>

<div class="personal-info">
  <s:if test="name != null and name != ''">
    <div class="username">
      <s:property value="%{#user.name + ' ' + #user.surname}" />
    </div>
  </s:if>
  <s:else>
    <div class="unspecified">No name specified for this user.</div>
  </s:else>
  <s:if test="institution != null and institution != ''">
    <div
      class="institution"
      style="padding-top: .5em">
      <s:property value="institution" />
    </div>
  </s:if>
  <s:else>
    <div class="unspecified">No institution specified for this user.</div>
  </s:else>

  <div class="email">
    <a href="mailto:<s:property value="emailAddress"/>"> <s:property
        value="emailAddress" />
    </a>
  </div>

  <tiles2:insertTemplate template="membershipStatusDetail.jsp" />

</div>

<div style="font-weight: bold; color: #505050;">Certificates:</div>
<ol class="certificate-info">

  <s:iterator
    value="certificates"
    var="cert">
    <li>
      <div class="userDN <s:if test="suspended">suspended-cert</s:if>">
        <s:set
          value="subjectString"
          var="thisCertDN" />
        ${thisCertDN}
      </div>
      <div class="userCA <s:if test="suspended">suspended-cert</s:if>">
        <s:set
          value="ca.subjectString"
          var="thisCertCA" />
        ${thisCertCA}
      </div>
    </li>
  </s:iterator>
</ol>