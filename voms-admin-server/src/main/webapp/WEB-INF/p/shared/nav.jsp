<%@include file="/WEB-INF/p/shared/taglibs.jsp"%>

<s:url action="login" var="home_url" namespace="/home" />
<s:url action="search" var="browse_url" namespace="/user" />
<s:url action="start" var="register_url" namespace="/register"/>
<s:url action="configuration" var="configuration_url" 
  namespace="/configuration"/>

<nav class="navbar navbar-voms navbar-static-top">
    <div class="container">
    <div class="navbar-header col-md-2">
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
         <li id="nav-search">
           <tiles2:insertTemplate template="search-form.jsp"/>
         </li>
         
         <s:if test="#request.currentAdmin.voUser == null">
          <li id="nav-req-membership"><a href="${register_url}">Register</a></li>
         </s:if>
        <li id="nav-configuration"><a href="${configuration_url}">Configuration info</a></li>  
      </ul>
      
      <ul class="nav navbar-nav navbar-right">
        <li id="nav-vomses"><a href="/">Other VOs on this server</a></li>
        <li class="dropdown">
          <a id="voms-nav-dropdown" href="#" class="dropdown-toggle" 
            data-toggle="dropdown" role="button" aria-expanded="false" title="User info" data-placement="right">
            <i class="glyphicon glyphicon-user"></i>
            <span class="small">${currentAdmin.realCN}</span> 
          </a>  
          <ul class="dropdown-menu dropdown-menu-right">
            <li><a href="#"><span class="small">${currentAdmin.realSubject}</span></a></li>
          </ul>
        </li>
      </ul>
    </div>
    <!--/.nav-collapse -->
    </div>
</nav>