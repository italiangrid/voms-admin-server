<%@include file="/WEB-INF/p/shared/taglibs.jsp"%>


<div class="info-tab">
  <h2><span>Your request history</span></h2>
  <voms:div cssClass="content" id="req-history-content">
  <tiles2:insertTemplate template="requestHistory.jsp"/>  
  </voms:div>
</div>