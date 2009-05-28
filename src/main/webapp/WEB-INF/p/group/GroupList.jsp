<%@include file="/WEB-INF/p/shared/taglibs.jsp"%>

<s:iterator value="#request.groupList" var="group">
	<div>
		<s:url action="%{actionClass}_show" var="url">
			<s:param name="requestId"  value="id"/>
		</s:url>
		
		<s:a href="%{url}"><s:property value="name"/></s:a>
	</div>
</s:iterator>