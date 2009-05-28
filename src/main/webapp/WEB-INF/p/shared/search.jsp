<%@include file="/WEB-INF/p/shared/taglibs.jsp"%>
<s:form id="search" action="search" namespace="/search">Search: 
<s:select list="#{'user':'Users','group':'Groups','role':'Roles', 'attribute':'Attributes'}" name="searchData.type"/>
<s:textfield id="searchField" size="15" name="searchData.text"/>
<s:hidden name="searchData.firstResult" value="0"/>
<s:submit id="submitButton" value="go!"/>
</s:form>