<%@include file="/WEB-INF/p/shared/taglibs.jsp"%>

<div class="info-tab">
  <h2><span>Your groups and roles</span></h2>
  <voms:div cssClass="content" id="req-content">
	<tiles2:insertTemplate template="mappingsRequest.jsp"/>  
  </voms:div>
</div>