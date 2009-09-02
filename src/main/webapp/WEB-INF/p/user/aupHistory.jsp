<%@include file="/WEB-INF/p/shared/taglibs.jsp"%>

<s:if test="aupAcceptanceRecords.empty">
No AUP acceptance records found.
</s:if>
<s:else>
  <table>
    <tr>
      <th>AUP info</th>
      <th>Last acceptance date</th>
    </tr>
    <s:iterator value="aupAcceptanceRecords" var="rec">
      <s:url action="sign" namespace="/aup" method="input" var="saURL">
                <s:param name="aupId" value="aupVersion.aup.id"/>
       </s:url>
      <tr>
        <td>
          <dl>
            <dt>name:</dt>
            <dd>${rec.aupVersion.aup.name}</dd>
            <dt>version:</dt>
            <dd><a href="${saURL}">${rec.aupVersion.version}</a></dd>
            <dt>expiration date:</dt>
            <dd>
                <s:text name="format.datetime">
                  <s:param value="expirationDate"/>
                </s:text>
             </dd>
          </dl>
          
        </td>
        <td>
          <s:text name="format.datetime">
            <s:param value="lastAcceptanceDate"/>
          </s:text>
          
        </td>
      </tr>
    </s:iterator>
  </table>
</s:else>
