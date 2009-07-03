<%@include file="/WEB-INF/p/shared/taglibs.jsp"%>

<h2>AUP version detail</h2>

<div>

  <div>
    Name:
  </div>
  <div>
    <s:property value="aup.name"/>
  </div>
  
  <div>
    Version:
  </div>
  <div>
    <s:property value="version"/>
  </div>
  
  <div>
    URL:
  </div>
  <div>
    <s:property value="url"/>
  </div>
  
  <s:if test="URLContent != null">
    <div>
      Content:
    </div>
    <div>
      <s:textarea rows="20" cols="80" value="%{model.URLContent}"/>
    </div>
  </s:if>  

</div>
