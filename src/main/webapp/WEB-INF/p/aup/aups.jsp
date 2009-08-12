<%@include file="/WEB-INF/p/shared/taglibs.jsp"%>

<h1>Acceptable Usage Policy:</h1>
  
<s:if test="voAUP is null">
  No acceptable usage policy defined for this VO.
</s:if>
<s:else>
  <div>
    <s:form action="change-reacceptance-period" namespace="/aup" validate="true">
      <s:hidden name="aupId" value="%{model.id}"/>
      <s:textfield name="period" value="%{model.reacceptancePeriod}" label="Reacceptance period (in days)" size="4"/>
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
    <s:iterator value="versions" var="version">
      <tr>
        <td>
          <s:url action="version" namespace="/aup" var="voVersionDetailURL">
            <s:param name="aupVersionId" value="id"/>
          </s:url>
          <s:a href="%{#voVersionDetailURL}">
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
              <s:hidden name="aupId" value="%{model.id}"/>
              <s:hidden name="version" value="%{#version.version}"/>
              <s:submit value="%{'Set active'}"/>
            </s:form>
          </s:else>
        </td>
        
        <td>
          <s:if test="%{versions.size > 1 and not #version.active}">
            <s:form action="remove-version" namespace="/aup">
              <s:hidden name="aupId" value="%{model.id}"/>
              <s:hidden name="version" value="%{#version.version}"/>
              <s:submit value="%{'Remove'}"/>
            </s:form>
          </s:if>
        </td>
        
        
      </tr>
    </s:iterator>
  </table>
  <s:url action="add-version" method="input" namespace="/aup" var="voAddVersionURL">
    <s:param name="aupId" value="id"/>
  </s:url>
  <div>
  <s:a href="%{#voAddVersionURL}">
    Add a new version
  </s:a>
  </div>
  
  <div>
  <s:url 
    action="trigger-acceptance" 
    namespace="/aup"
    var="voAUPReacceptanceURL"
 >
    <s:param name="aupId" value="id"/>
 </s:url >
  <s:a href="%{#voAUPReacceptanceURL}">
    Trigger reacceptance of currently active version
  </s:a>
  </div>
</s:else>
