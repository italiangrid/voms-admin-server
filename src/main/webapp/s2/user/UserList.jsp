<%@include file="/s2-common/taglibs.jsp"%>

<s:iterator value="#request.userList">
	<div>
		<span class="user.id">%{id}</span>
		<span class="user.name">%{name} ${user.surname}</span>
	</div>
</s:iterator>