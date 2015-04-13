<%@include file="/jsps/taglibs.jsp"%>

<s:url action="index" var="users_url" namespace="/v4/users" />
<s:url action="index" var="groups_url" namespace="/v4/groups" />
<s:url action="index" var="roles_url" namespace="/v4/roles"/>
<s:url action="index" var="attributes_url" namespace="/v4/attributes"/>
<s:url action="index" var="requests_url" namespace="/v4/requests"/>
<s:url action="index" var="settings_url" namespace="/v4/settings"/>

<nav class="navbar navbar-default">
		<div class="navbar-header">
			<button type="button" class="navbar-toggle collapsed"
				data-toggle="collapse" data-target="#navbar" aria-expanded="false"
				aria-controls="navbar">
				<span class="sr-only">Toggle navigation</span> <span
					class="icon-bar"></span> <span class="icon-bar"></span> <span
					class="icon-bar"></span>
			</button>
			<a class="navbar-brand" href="${home_url}"><span class="text-uppercase">${voName}</span></a>
		</div>
		
		
		<div id="navbar" class="navbar-collapse collapse">
			<ul class="nav navbar-nav">
			  
        <li id="nav-users"><a href="${users_url}">Users</a></li>
				<li id="nav-groups"><a href="${groups_url}">Groups</a></li>
				<li id="nav-roles"><a href="${roles_url}">Roles</a></li>
				<li id="nav-attributes"><a href="${attributes_url}">Generic attributes</a></li>
				
			</ul>
			<ul class="nav navbar-nav navbar-right">
			  <li id="nav-requests"><a href="${requests_url}" data-toggle="tooltip" title="Requests" data-placement="left"><i class="glyphicon glyphicon-inbox"></i></a></li>
        <li id="nav-settings"><a href="${settings_url}" data-toggle="tooltip" title="Settings" data-placement="bottom"><i class="glyphicon glyphicon-cog"></i></a></li>
				<li class="dropdown">
          <a id="voms-nav-dropdown" href="#" class="dropdown-toggle" 
            data-toggle="dropdown" role="button" aria-expanded="false" title="User info" data-placement="right">
            <i class="glyphicon glyphicon-home"></i>
          </a>
          <ul class="dropdown-menu dropdown-menu-right">
            <li><a href="#">${voName}</a></li>
          </ul>
        </li>
		  </ul>
		</div>
		<!--/.nav-collapse -->
</nav>

<script type="text/javascript">
var namespaceName = '<s:property value="namespaceName"/>';
</script>