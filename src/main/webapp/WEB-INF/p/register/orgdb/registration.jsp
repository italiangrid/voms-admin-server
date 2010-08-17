<%@include file="/WEB-INF/p/shared/taglibs.jsp"%>

<h1>
Welcome to the registration page for the <span class="voName">${voName}</span> VO.
</h1>

<p>
Please enter the following information:
</p>


<s:form action="query-database" validate="true" namespace="/register/orgdb" onsubmit="ajaxSubmit(this,'orgdb-results'); return false;">
	<ul class="form">
		<li>
			<s:textfield name="name" value="%{requester.name}" size="60" label="%{'Your name'}" cssClass="registrationField"/>		
		</li>
		<li>
			<s:textfield name="surname" value="%{requester.surname}" size="60" label="%{'Your surname'}" cssClass="registrationField"/>		
		</li>
		<li>
			<s:textfield name="emailAddress" value="%{requester.emailAddress}" size="60" label="%{'Your email address'}" cssClass="registrationField"/>
    	</li>
    	
    	<s:submit/>
	</ul>
</s:form>

<div id="orgdb-results">
	<tiles2:insertTemplate template="searchResults.jsp"/>
</div>