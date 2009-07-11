<%@include file="/WEB-INF/p/shared/taglibs.jsp"%>

<h1>
 VO Membership request details
</h1>

<ul>
  <li>
    DN: <s:property value="requesterInfo.certificateSubject"/>
  </li>
  <li>
    CA: <s:property value="requesterInfo.certificateIssuer"/>
  </li>
  
  
  <li>
    <h2>Personal information</h2>
  </li>
  <s:iterator value="requesterInfo.personalInformation.keySet()" var="key">
    <li>
    <div class="label">
      <s:property value="key"/>
    </div>
    <div>
      <s:property value="requesterInfo.personalInformation[key]"/>
    </div>
    </li>
  </s:iterator>
  <li>
    Submitted on : <s:property value="creationDate"/>
  </li>
  <li>
    
    <s:submit/>
  </li>
</ul>