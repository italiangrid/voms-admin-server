<%@include file="/WEB-INF/p/shared/taglibs.jsp"%>

<div class="info-tab">
  <h2><span>Pending requests</span></h2>
  <voms:div cssClass="content" id="pending-req-content">
  <tiles2:insertTemplate template="pendingRequests.jsp"/>  
  </voms:div>
</div>