<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://org.glite.security.voms.tags" prefix="voms"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>

<div id="manageAdminsPane">

<c:choose>
	<c:when test="${! empty admins}">
		<div class="header2">Registered admins:</div>
		
		<table class="table" cellpadding="0" cellspacing="0">
		
			<c:forEach var="admin" items="${admins}" varStatus="status">
				<tr class="${ (status.index) %2 eq 0 ? 'tableRowEven': 'tableRowOdd'}">
					<td class="admin">
						<div class="userDN">
							<voms:formatDN dn="${admin.dn}" fields="CN"/>
						</div>
						<div class="userCA">
							<voms:formatDN dn="${admin.ca.dn}" fields="CN"/>
						</div>
					</td>

					<!-- Assigned Tag list -->
					<td class="tags" style="text-align: center;">
						<c:forEach var="tag" items="${admin.tags}">
							<div style="float: left; clear: left;">
								${tag.dn}
							</div>
							<div class="actionImage" style="float: right; clear: right;">
								<c:url var="removeTagURL" value="/TagActions.do">
									<c:param name="method" value="removeTag"/>
									<c:param name="adminId" value="${admin.id}"/>
									<c:param name="tagId" value="${tag.id}"/>
								</c:url>
								<voms:link permission="ALL" 
										context="vo" 
										href="${removeTagURL}"
										styleClass="imageLink"
										>
										<html:img page="/img/delete.gif" alt="remove tag" title="remove tag"/>												
								</voms:link>
							</div>
						</c:forEach>
					</td>
					
					<!-- Assign tags button -->
					<td class="assign-tags" style="text-align: right;">
						<voms:unassignedTags adminId="${admin.id}" var="unassignedTags"/>
						<c:if test="${!empty unassignedTags}">
							<html:form action="/TagActions" method="post">
								<html:hidden property="method" value="assignTag"/>
								<html:hidden property="adminId" value="${admin.id}"/>
								<html:select property="tagId" 
									styleClass="selectBox">
									<html:options collection="unassignedTags" property="id" labelProperty="dn"/>	
								</html:select>
								<voms:submit context="vo" permission="ALL" styleClass="submitButton" value="Assign tag"/>
							</html:form>
						</c:if>
					</td>
				</tr>
			</c:forEach>
		</table>	
	</c:when>
	<c:otherwise>
		<div class="header2">
			No admins defined in this vo.
			</div>
	</c:otherwise>
</c:choose>
</div>

