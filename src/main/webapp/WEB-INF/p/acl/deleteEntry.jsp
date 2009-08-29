<%@include file="/WEB-INF/p/shared/taglibs.jsp"%>

<h1>
Delete <s:if test="defaultACL">default</s:if> ACL entry: 
</h1>

<s:form>
  <s:hidden name="aclId" value="%{model.id}" />
  <s:hidden name="adminId" value="%{admin.id}" />
  
  <table class="form noborder">   
    <tr>
      <td><h1>Admin:</h1></td>
      <td class="admin">
        <div class="userDN">
          <voms:formatDN dn="${admin.dn}" fields="CN"/>
        </div>
        <div class="userCA">
          <voms:formatDN dn="${admin.ca.subjectString}" fields="CN"/>
        </div>
      </td>
      </tr>
      <tr>
        <td colspan="2">&nbsp;</td>
      </tr>
      <tr>
        <td><h1>Context:</h1></td>
        <td class="highlight"><s:property value="context"/></td>
      </tr>
      <tr>
      <td colspan="2">&nbsp;</td>
    </tr>
    <tr>
      <td><h1>Permission:</h1></td>
      <td style="font-weight:bold;"><s:property value="permissions[admin].compactRepresentation"/></td>
    </tr>
    <tr>
      <td colspan="2">&nbsp;</td>
    </tr>
    <s:if test="not defaultACL and context.groupContext">
    <tr>
      <td><h1>Remove also from children contexts?</h1></td>
      <td>
        <s:checkbox name="propagate" theme="simple"/>  
      </td>
    </tr>
    </s:if>
    <s:else>
      <s:hidden name="propagate" value="%{false}"/>  
    </s:else>
    <tr>
      <td colspan="2">&nbsp;</td>
    </tr>
    <tr>
      <td/>
      <td>
        <s:submit value="%{'Delete entry'}"/>
      </td>
    </tr>
    <tr>
      <td colspan="2">&nbsp;</td>
    </tr>
  </table>
</s:form>