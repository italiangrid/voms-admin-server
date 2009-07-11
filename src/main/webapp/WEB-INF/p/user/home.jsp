<%@include file="/WEB-INF/p/shared/taglibs.jsp"%>

<h1>
  Welcome to the ${voName} Virtual Organization, <s:property value="model"/>.
</h1>

<s:if test="suspended">
  <h1 class="warning">
    Your membership is currently suspended for the following reason:
  </h1>
  <div class="suspension reason">
    <s:property value="suspensionReason"/>
  </div>
</s:if>
<h2>
Your membership information:
</h2>

<div class="voMembershipInfo">
<table class="table" cellpadding="0" cellspacing="0">   
    <tr class="tableHeaderRow">
      <td>Group name</td>
      <td>Roles</td>
    </tr>
    
    <s:iterator value="mappingsMap" var="mapping">
      <tr>
      <td width="50%">
          <div class="group">
            <s:property value="key"/>
          </div>
        </td>
       <td>
          <div class="roles">
            <s:iterator value="value" var="role">
              <div class="roleCell">
                <s:property value="name"/>
              </div>
            </s:iterator>
         </div>
      </td>
      </tr>
    </s:iterator>
</table>
</div>


<s:if test="not attributes.empty">
<h2>
Your generic attributes:
</h2>

<div class="genericAttributesInfo">
  <table class="table" cellpadding="0" cellspacing="0">
    
    <tr class="tableHeaderRow">
      <td>Attribute name</td>
      <td>Attribute value</td>
    </tr>
    
    <s:iterator value="attributes" var="attribute">
      <tr>
        <td>
          <div class="attributeName">
            <s:property value="name"/>
          </div>
        </td>
        <td>
          <div class="attributeValue">
            <s:property value="value"/>
          </div>
        </td>
      </tr>
    </s:iterator>
   </table>
</div>
</s:if>
 