<%@include file="/WEB-INF/p/shared/taglibs.jsp"%>

<h1>Edit <s:if test="defaultACL">default</s:if> ACL entry:</h1>

<s:form>
  <s:hidden name="aclId" value="%{model.id}" />
  <s:hidden name="adminId" value="%{admin.id}" />

  <table class="form noborder aclManagement" width="100%">
    <tr>
      <td style="width: 10%;"><h1>Admin:</h1></td>
      <td class="admin">
      <div class="userDN"><voms:formatDN dn="${admin.dn}" fields="CN" />
      </div>
      <div class="userCA"><voms:formatDN dn="${admin.ca.subjectString}"
        fields="CN" /></div>
      </td>
    </tr>
 
    <tr>
      <td><h1>Context:</h1></td>
      
      <td class="context"><s:property value="context" /></td>
    </tr>
    <tr>
    </tr>
    </table>
    
    
    <table class="form noborder">
    
    <%@include file="/WEB-INF/p/acl/permissionFormFieldset.jsp"%>
    
    <tr>
      <td colspan="2">&nbsp;</td>
    </tr>
    <s:if test="not model.defaultACL and model.context.groupContext">
    <tr>
      <td>
      <h1>Propagate to children contexts?</h1>
      </td>
      <td><s:checkbox name="propagate" theme="simple"/></td>
    </tr>
    </s:if>
    <s:else>
      <s:hidden name="propagate" value="false"/>
    </s:else>
    <tr>
      <td colspan="4"/>
    </tr>
    <tr>
      <td colspan="3"/>
      <td><s:submit value="%{'Save entry'}" /></td>
    </tr>
  </table>
</s:form>