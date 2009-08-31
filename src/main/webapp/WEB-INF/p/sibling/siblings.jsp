<%@include file="/WEB-INF/p/shared/taglibs.jsp"%>

<div id="listConfiguredVOsPane">

	<h1>
		List of VOs configured on this server:
	</h1>
	
	<table class="table">
		<s:iterator value="configuredVOs" var="voURL"/>
			<tr class="tableRow">
				<td>
					<a href="<s:property value='#voURL'/>">
						<s:property value="#voURL.substring(#voURL.lastIndexOf('/')+1)"/>
					</a>
				</td>
			</tr>
	
		</table>
	
</div>