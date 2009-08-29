<%@include file="/WEB-INF/p/shared/taglibs.jsp"%>

<h1 class="username">
  <s:property value="fullName"/> 
  <span class="institution">
    <s:property value="institution"/>  
  </span>
</h1>

<%-- 
<dl>
    <dt>Member since:</dt><dd><s:property value="creationTime"/></dd>
    <dt>Expiration time:</dt><dd><s:property value="endTime"/></dd>
</dl>
--%>


<div class="info-tab">
  <h2><span>Personal information</span></h2>
  <voms:div id="pers-info-content" cssClass="content">
      <tiles2:insertTemplate template="personalInfo.jsp"/>
  </voms:div>
</div>

<div class="info-tab">
  <h2><span>Certificates</span></h2>
  <voms:div id="cert-info-content" cssClass="content">
      <tiles2:insertTemplate template="certificates.jsp"/>
  </voms:div>
</div>

<div class="info-tab">
  <h2><span>Groups and Roles</span></h2>
  <voms:div cssClass="content" id="mappings-content">
      
      <tiles2:insertTemplate template="mappings.jsp"/>
      
    </voms:div>
</div>


<div class="info-tab">
  <h2><span>Generic attributes</span></h2>
  <voms:div cssClass="content" id="generic-attrs-content">
    
    <tiles2:insertTemplate template="attributes.jsp"/>

  </voms:div>
</div>

