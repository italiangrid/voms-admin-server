<%@include file="/WEB-INF/p/shared/taglibs.jsp"%>
<h1>
  Welcome to the <span class="voName">${voName}</span> VO,
</h1>

<div id="welcomeUserName">
   
    <s:if test="model.requesterInfo.name != null and model.requesterInfo.surname != null">
    <s:property value="model.requesterInfo.name+ ' ' +model.requesterInfo.surname"/>
      <span class="institution">
          (<s:property value="model.requesterInfo.institution"/>)  
        </span>
  </s:if>
  <s:else>
    <s:property value="model.requesterInfo.certificateSubject"/>
  </s:else>
</div>

