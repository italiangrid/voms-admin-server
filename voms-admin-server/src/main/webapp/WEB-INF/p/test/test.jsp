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
<%@include file="/WEB-INF/p/shared/taglibs.jsp"%>

<h1>Test page</h1>

<div id="tabs">
  <ul>
    <li><a href="#tabs-1">Personal Information</a></li>
    <li><a href="#tabs-2">Certificates</a></li>
    <li><a href="#tabs-3">Groups and roles</a></li>
    <li><a href="#tabs-4">Generic attributes</a></li>
    <li><a href="#tabs-5">Request history</a></li>
  </ul>
  <div id="tabs-1">
    <p>Personal info</p>
  </div>
  <div id="tabs-2">
    <p>Certificates</p>
  </div>
  <div id="tabs-3">
    <p>Groups and roles</p>
  </div>
  <div id="tabs-4">
    <p>Generic attributes</p>
  </div>
  <div id="tabs-5">
    <p>Requests</p>
  </div>
</div>

<script type="text/javascript">
$( function() {
 $("#tabs").tabs(); 
});
</script>