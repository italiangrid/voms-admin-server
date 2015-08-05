<%@include file="/WEB-INF/p/shared/taglibs.jsp"%>
<s:url value="https://phonebook.cern.ch/phonebook/#personDetails/" 
  var="phonebookURL">
  <s:param name="id" value="%{orgDbId}"/>
</s:url>

<voms:hasPermissions var="canDelete" context="vo" permission="rw"/>

<div>
  CERN HR database id: <a href="${phonebookURL}">${orgDbId}</a>
  
  <s:if test="#attr.canDelete">
	  <s:form 
	    action="change-orgdb-id" 
	    namespace="/user" 
	    theme="simple" 
	    cssStyle="display: inline">
	    <s:hidden name="userId" value="%{id}"/>
	    <s:submit value="%{'Change HR id'}"/>
	  </s:form>  
  </s:if>
</div>

