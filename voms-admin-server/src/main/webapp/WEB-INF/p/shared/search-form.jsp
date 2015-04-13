<%@include file="/WEB-INF/p/shared/taglibs.jsp"%>

<s:form validate="true" 
  action="search" namespace="/user" 
  cssClass="form-inline">
  <div class="form-group">
    <s:hidden name="searchData.type" value="%{'user'}"/>
    
    <s:textfield 
      name="searchData.text" 
      value="%{#session.searchData.text}"
      cssClass="form-control input-sm nav-search-input"
      placeholder="Search VO..."
      />
    <s:submit cssClass="hidden"/>  
  </div>
</s:form>