<%@include file="/WEB-INF/p/s2-common/taglibs.jsp"%>


<div id="searchUsersPane"><s:form action="search">
  <s:hidden
    name="searchData.firstResult"
    value="0"
  />
  <s:textfield name="searchData.text" />
  <s:submit value="Search users" />
</s:form></div>
<!-- searchPane -->

<div id="searchResultsPane">

<s:if test="searchResults.results.size == 0">
	No users found.
</s:if> 

<s:else>

  <table
    class="table"
    cellpadding="0"
    cellspacing="0"
  >
    <s:iterator
      value="searchResults.results"
      var="user"
      status="rowStatus"
    >
      <tr class="tableRow">

        <td width="95%">
          <div class="userDN">
            <s:property value="dn" />
          </div>
         </td>
      </tr>
    </s:iterator>
  </table>


</s:else>
</div>
