<%@include file="/WEB-INF/p/shared/taglibs.jsp"%>

<h1>
Delete <s:if test="defaultACL">default</s:if> ACL entry: 
</h1>

<s:form>
  <s:hidden name="aclId" value="%{model.id}" />
  <s:hidden name="adminId" value="%{admin.id}" />
  
  <table class="table" cellpadding="0" cellspacing="0">   
    <tr>
      <td class="header">Admin:</td>
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
        <td class="header">Context:</td>
        <td class="highlight"><s:property value="context"/></td>
      </tr>
      <tr>
      <td colspan="2">&nbsp;</td>
    </tr>
    <tr>
      <td class="header" >Permission:</td>
      <td style="font-weight:bold;"><s:property value="permissions[admin].compactRepresentation"/></td>
    </tr>
    <tr>
      <td colspan="2">&nbsp;</td>
    </tr>
    <tr>
      <td><div class="header">Remove also from children contexts?</div></td>
      <td>
        <s:checkbox name="propagate"/>  
      </td>
    </tr>
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