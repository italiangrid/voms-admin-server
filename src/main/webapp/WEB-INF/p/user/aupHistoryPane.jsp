<%@include file="/WEB-INF/p/shared/taglibs.jsp"%>

<div class="info-tab">
  <h2><span><tiles2:insertAttribute name="panelName"/></span></h2>
  <voms:div id="aup-history-content" cssClass="content">
    <tiles2:insertTemplate template="aupHistory.jsp"/>
  </voms:div>
</div>