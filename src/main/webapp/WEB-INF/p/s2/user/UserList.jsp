<%@include file="/WEB-INF/p/s2-common/taglibs.jsp"%>

<s:iterator value="users">
	<div>
		<span class="user.id">%{id}</span>
		<span class="user.name">%{name} ${user.surname}</span>
	</div>
</s:iterator>