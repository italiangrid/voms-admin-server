<%@include file="/WEB-INF/p/shared/taglibs.jsp"%>

<s:form validate="true" 
  action="search" namespace="/user" 
  cssClass="navbar-form navbar-left">
  <div class="form-group">
    <s:hidden name="searchData.type" value="%{'user'}"/>
    
    <s:textfield 
      name="searchData.text" 
      value="%{#session.searchData.text}"
      cssClass="form-control input-sm nav-search-input"
      />
    <s:submit cssClass="hidden"/>  
  </div>
</s:form>

<%--
<form class="navbar-form navbar-left" role="search">
	<div class="form-group">
		<input type="text" class="form-control input-sm nav-search-input"
			placeholder="Search...">
	</div>
	
</form>
 --%>