<%@include file="/WEB-INF/p/common/taglibs.jsp"%>

<div id="userSearch">

<s:if test='(searchResults.searchString eq null) and (searchResults.results.size == 0)'>
No users found in this VO.
</s:if>
<s:elseif test="searchResults.results.size == 0">
	No users found matching search string '<s:property value="searchResults.searchString"/>'.
</s:elseif> 
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
