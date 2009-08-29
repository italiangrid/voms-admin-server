<%@include file="/WEB-INF/p/shared/taglibs.jsp"%>

<s:url action="manage" namespace="/acl" var="editACLURL">
  <s:param name="aclGroupId" value="id"/>
</s:url>

<s:set value="name" var="thisGroup"/>

<h1>
  Group <span class="groupname"><s:property value="name"/></span>

  <voms:authorized permission="ACL_READ" context="${thisGroup}">
  <s:a href="%{editACLURL}" cssClass="actionLink" cssStyle="margin-left:1em">View ACL</s:a>
  </voms:authorized>
</h1>

<div class="info-tab">
  <h2><span>Membership information</span></h2>
  <voms:div id="group-membership-content" cssClass="content">
    <div>
      <s:form theme="simple" action="search-member" namespace="/group" onsubmit="ajaxSubmit(this,'mmResults'); return false;">
        <s:hidden name="searchData.groupId" value="%{id}" id="groupSearchGroupId"/>
        <s:textfield name="searchData.text" size="20" id="groupSearchText"/>
        <s:submit value="%{'Search members'}" cssClass="submitButton"/>
      </s:form>
      
      
    </div>
    
    <div id="mmResults">
      <s:action name="search-member" namespace="/group" executeResult="true">
        <s:param name="searchData.groupId" value="id"/>
      </s:action>
    </div>
  </voms:div>
</div>

<div class="info-tab">
  <h2><span>Attributes information</span></h2>
  <voms:div id="generic-attrs-content" cssClass="content">
        
        <tiles2:insertTemplate template="attributes.jsp"/>
        
  </voms:div>
</div>

 


<s:url action="load-default" namespace="/acl" var="editDefaultACLURL">
  <s:param name="aclGroupId" value="id"/>
</s:url>






