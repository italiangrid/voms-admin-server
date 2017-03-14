<%--

    Copyright (c) Istituto Nazionale di Fisica Nucleare (INFN). 2006-2016

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
<!DOCTYPE html>

<%@ page contentType="text/html; charset=UTF-8"%>

<%@include file="/WEB-INF/p/shared/taglibs.jsp"%>

<html>
  <head>
    
    <%@include file="/WEB-INF/p/shared/meta.jsp"%>
    
    <title>VOMS Admin &gt; ${voName}</title>
    
    <link rel="stylesheet" type="text/css"
      href="<s:url value="/style/style.css"/>" />
      
    <link rel="stylesheet" 
      href="<s:url value="/style/font-awesome.min.css"/>">
      
    <sj:head jqueryui="false" />
    
    
    <link type="text/css" 
      href="<s:url value="/style/custom-theme/jquery-ui.css"/>" 
      rel="stylesheet" />
    
    <script type="text/javascript" src="<s:url value="/common/js/jquery-ui.js"/>">
    </script>
    
    <script type="text/javascript" src="<s:url value="/common/js/jquery.cookie.js"/>">
      
    </script>
    
    <script type="text/javascript">
      ajaxBaseURL = '<s:url value="/ajax/"/>';
      memberSearchURL =  '<s:url  value="/search/member.action"/>';
    </script>
  </head>

  <body>
  
  <tiles2:insertTemplate template="dialogs.jsp"/>
    
  <div id="header">
    <tiles2:insertAttribute name="header"/>
  </div> <!-- header -->
  
  <tiles2:insertAttribute name="menu"/>
  
  <div id="messages">
    <tiles2:insertAttribute name="messages"/>
  </div>
  
  <div id="body">
      <tiles2:insertAttribute name="leftBar" />
      
      <div id="noflash">
        <tiles2:insertAttribute name="content" />
      </div>
  </div> <!-- body -->
  
  <div id="footer">
    <tiles2:insertAttribute name="footer"/>
  </div> <!-- footer -->
  
  <script type="text/javascript" src="<s:url value="/common/js/jq-sugar.js"/>">
  </script>
  </body>
</html>