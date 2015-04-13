<%@include file="/WEB-INF/p/shared/taglibs.jsp"%>

<s:url action="login" var="home_url" namespace="/home" />
<s:url action="search" var="browse_url" namespace="/user" />
<s:url action="start" var="register_url" namespace="/register"/>
<s:url action="configuration" var="configuration_url" 
  namespace="/configuration"/>

<nav class="navbar navbar-voms navbar-default navbar-static-top">
    <div class="container">
    <div class="navbar-header col-md-2">
      <button type="button" class="navbar-toggle collapsed"
        data-toggle="collapse" data-target="#navbar" aria-expanded="false"
        aria-controls="navbar">
        <span class="sr-only">Toggle navigation</span> <span
          class="icon-bar"></span> <span class="icon-bar"></span> <span
          class="icon-bar"></span>
      </button>
      <a class="navbar-brand" href="${home_url}">VOMS Admin</a>
    </div>
    
    <div id="navbar" class="navbar-collapse collapse">
      
      <ul class="nav navbar-nav navbar-right">
      
        <s:if test="#request.currentAdmin.voUser == null">
           <li>
              <a href="${register_url}">Register</a>
           </li>
        </s:if> 
        <li class="dropdown">
          <a href="#" class="dropdown-toggle" 
            data-toggle="dropdown" role="button" aria-expanded="false" title="VO info">
            VO: <span>${voName}</span> <b class="caret"></b>
          </a>  
          <ul class="dropdown-menu dropdown-menu-right">
            <li><a href="${configuration_url}">Configuration info</a></li>  
            <li><a href="/">Other VOs on this server</a></li>
          </ul>
        </li>
        
        <li class="dropdown">
          <a href="#" class="dropdown-toggle" 
            data-toggle="dropdown" role="button" aria-expanded="false" title="User info" data-placement="right">
            <i class="glyphicon glyphicon-user"></i> ${currentAdmin.name} 
          </a>  
          <ul class="dropdown-menu dropdown-menu-right">
            <li class="disabled"><a href="#">Logged in as ${currentAdmin.realSubject}</a></li>
          </ul>
        </li>
      </ul>
    </div>
    <!--/.nav-collapse -->
    </div>
</nav>