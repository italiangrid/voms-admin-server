<%@include file="/WEB-INF/p/shared/taglibs.jsp"%>


<h1>Change HR database id for <s:property value="shortName"/></h1>



<s:form action="save-orgdb-id" 
  namespace="/user"
  validate="true"
  >
  <s:token/>
  <s:hidden name="userId" value="%{id}"/>
  <label class="label" for="_user_name">User:</label>
  <br></br>
  <div id="_user_name"><s:property value="shortName"/></div>
  <s:textfield id="orgDbIdVal" name="theOrgDbId" value="%{orgDbId}" size="20" label="CERN HR ID"/>
  <s:submit  align="left" value="%{'Change HR ID'}" onclick="confirmOrgDbIdChangeDialog(this, '%{shortName}'); return false"/>
</s:form>
