<%@include file="/WEB-INF/p/shared/taglibs.jsp"%>

<s:form>
        <s:textfield name="name" disabled="true" label="Name" size="40" cssClass="text"/>
        <s:textfield name="surname" disabled="true" label="Surname" size="40" cssClass="text"/>
        <s:textfield name="institution" disabled="true" label="Institution" size="40" cssClass="text"/>
        <s:textarea name="address" disabled="true" label="Address" rows="4" cols="30"   cssClass="text"/>
        <s:textfield name="phoneNumber" disabled="true" label="Phone" size="40"   cssClass="text"/>
        <s:textfield name="emailAddress" disabled="true" label="Email" size="40"   cssClass="text"/>
        <s:submit value="%{'Change personal information'}" disabled="true"/>
</s:form>
      