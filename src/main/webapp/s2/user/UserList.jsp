<%@include file="/s2-common/taglibs.jsp"%>

<s:iterator value="userList" var="user">
	<div>
		<span class="user.id">%{user.id}</span>
		<span class="user.name">%{user.name} ${user.surname}</span>
	</div>
</s:iterator>