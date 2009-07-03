<%@include file="/WEB-INF/p/shared/taglibs.jsp"%>

<h2>Acceptable Usage Policies (AUPs)</h2>


<s:if test="gridAUP is null">
  No Grid AUP defined for this VO.
</s:if>
  

<s:else>
  <h3>Grid AUP</h3>
  <div>
    <s:form action="change-reacceptance-period" namespace="/aup" validate="true">
      <s:hidden name="aupId" value="%{gridAUP.id}"/>
      <s:textfield name="period" value="%{gridAUP.reacceptancePeriod}" label="Reacceptance period (in days)" size="4"/>
      <s:submit value="%{'Change'}"/>
    </s:form>
  </div>
  
  <h4>Versions:</h4>
  <table>
    <tr>
      <th>Version</th>
      <th>Creation date</th>
      <th>Active</th>
    </tr>
    <s:iterator value="gridAUP.versions" var="version">
      <tr>
        <td>
          <s:url action="version" namespace="/aup" var="versionDetailURL">
            <s:param name="aupVersionId" value="id"/>
          </s:url>
          <s:a href="%{#versionDetailURL}">
            <s:property value="version"/>
          </s:a>
        </td>
        <td>
          <s:property value="creationTime"/>
        </td>
        
        <td>
          <s:if test="#version.active">
            active
          </s:if>
          <s:else>
            <s:form action="set-active-version" namespace="/aup">
              <s:hidden name="aupId" value="%{gridAUP.id}"/>
              <s:hidden name="version" value="%{#version.version}"/>
              <s:submit value="%{'Set active'}"/>
            </s:form>
          </s:else>
        </td>
        
        <td>
          <s:if test="%{gridAUP.versions.size > 1 and not #version.active}">
            <s:form action="remove-version" namespace="/aup">
              <s:hidden name="aupId" value="%{gridAUP.id}"/>
              <s:hidden name="version" value="%{#version.version}"/>
              <s:submit value="%{'Remove'}"/>
            </s:form>
          </s:if>
        </td>
        
        
      </tr>
    </s:iterator>
  </table>
  <s:url action="add-version" method="input" namespace="/aup" var="addVersionURL">
    <s:param name="aupId" value="gridAUP.id"/>
  </s:url>
  <div>
  <s:a href="%{#addVersionURL}">
    Add a new version
  </s:a>
  </div>
  
  <div>
  <s:a href="#reaccept">
    Trigger reacceptance of currently active version
  </s:a>
  </div>
</s:else>
  
<s:if test="voAUP is null">
  No Grid AUP defined for this VO.
</s:if>
<s:else>
  <h3>VO AUP</h3>
  <div>
    
  </div>
</s:else>
