<%--

    Copyright (c) Istituto Nazionale di Fisica Nucleare (INFN). 2006-2015

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

--%>
<%@include file="/WEB-INF/p/shared/taglibs.jsp"%>

<tiles2:insertTemplate template="../shared/errorsAndMessages.jsp" />

<div class="reloadable"><s:if
	test="not #request.attributeClasses.empty">
	<voms:authorized permission="ATTRIBUTES_WRITE" context="vo">
		<div class="attributeCreationTab"><s:form action="set-attribute"
			namespace="/user"
			onsubmit="ajaxSubmit(this,'generic-attrs-content'); return false;">
			<s:token />
			<s:hidden name="userId" value="%{model.id}" />

			<table cellpadding="" cellspacing="" class="noborder">
				<tr>
					<td><s:select name="attributeName"
						list="#request.attributeClasses" listKey="name" listValue="name"
						label="Attribute name" /></td>
				</tr>

				<tr>
					<td><s:textarea label="Attribute value" name="attributeValue"
						rows="4" cols="30" value="" /></td>
				</tr>
				<tr>
					<td><s:submit value="%{'Set attribute'}" /></td>
				</tr>
			</table>
		</s:form></div>
	</voms:authorized>
</s:if> <s:else>
      No attribute classes defined for this VO.
</s:else> <voms:hasPermissions var="canReadAttrs" context="vo"
	permission="ATTRIBUTES_READ" /> <s:if
	test="not #request.attributeClasses.empty">
	<s:if test="#attr.canReadAttrs or #attr.currentAdmin.is(model)">
		<s:if test="attributes.isEmpty">
			<s:if test="not #request.attributeClasses.empty">
          No attributes defined for this user.
        </s:if>
		</s:if>
		<s:else>

			<table class="table" cellpadding="0" cellspacing="0">

				<tr>
					<th>Attribute name</th>
					<th>Attribute value</th>
					<th colspan="2" />
				</tr>

				<s:iterator var="attribute" value="attributes">

					<tr class="tableRow">
						<td><s:property value="name" /></td>
						<td><s:property value="value" /></td>
						<td><voms:authorized permission="ATTRIBUTES_WRITE"
							context="vo">
							<s:form action="delete-attribute" namespace="/user"
								onsubmit="ajaxSubmit(this,'generic-attrs-content'); return false;">
								<s:token />
								<s:hidden name="userId" value="%{model.id}" />
								<s:hidden name="attributeName" value="%{#attribute.name}" />
								<s:submit value="%{'delete'}" />
							</s:form>
						</voms:authorized></td>
					</tr>
				</s:iterator>
			</table>
		</s:else>
	</s:if>
	<s:else>
	You do not have enough permissions to see the attribute mappings for this user.
</s:else>
</s:if></div>