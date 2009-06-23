<%@include file="/WEB-INF/p/shared/taglibs.jsp"%>


<h2>Attribute classes</h2>

<div class="createTab">
  <voms:hasPermissions var="canCreate" 
            context="/${voName}" 
            permission="ATTRIBUTES_READ|ATTRIBUTES_WRITE"/>
  
  <s:if test="#attr.canCreate">
    <s:url action="create-form" namespace="/attribute" var="manageAttributeURL"/>
    <s:a href="%{manageAttributeURL}">Manage attribute classes</s:a>
  </s:if>
</div>

<div class="searchTab">

<s:form validate="true">
  <s:hidden name="searchData.type" value="%{'attribute'}"/>
  <s:textfield name="searchData.text" size="20"/>
  <s:submit value="%{'Search attributes'}"/>
</s:form>

<s:if test='(searchResults.searchString eq null) and (searchResults.results.size == 0)'>
No attribute classes defined in this VO.
</s:if>
<s:elseif test="searchResults.results.size == 0">
  No attribute mappings found matching search string '<s:property value="searchResults.searchString"/>'.
</s:elseif> 
<s:else>
  <table
    class="table"
    cellpadding="0"
    cellspacing="0"
  >
    <s:iterator
      value="searchResults.results"
      var="userAttribute"
      status="rowStatus"
    >
      <tr class="tableRow">
        <td>
          <s:property value="%{#userAttribute[0]}"/>
         </td>
         <td>
          <s:property value="%{#userAttribute[2]}"/>
         </td>
         <td>
          <s:set var="userDn" value="%{#userAttribute[1].dn}"/>
          <voms:formatDN dn="${userDn}" fields="CN"/>
         </td>
      </tr>
    </s:iterator>
  </table>
  
  <s:url action="search" namespace="/attribute" var="searchURL"/>
  
  <voms:searchNavBar context="vo" 
      permission="ATTRIBUTE_READ" 
      disabledLinkStyleClass="disabledLink"
      id="searchResults"
      linkStyleClass="navBarLink"
      searchURL="${searchURL}"
      styleClass="resultsCount"
      />
</s:else>
</div>