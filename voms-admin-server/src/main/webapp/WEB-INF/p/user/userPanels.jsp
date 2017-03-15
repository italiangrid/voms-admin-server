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

<div role="tabpanel">
  
  <!-- Nav tabs -->
  <ul class="nav nav-tabs" role="tablist">
    <li role="presentation" class="active"><a href="#pi" aria-controls="pi" role="tab" data-toggle="tab">Personal information</a></li>
    <li role="presentation"><a href="#certificates" aria-controls="certificates" role="tab" data-toggle="tab">Certificates</a></li>
    <li role="presentation"><a href="#groups" aria-controls="groups" role="tab" data-toggle="tab">Groups & Roles</a></li>
    <li role="presentation"><a href="#attributes" aria-controls="attributes" role="tab" data-toggle="tab">Attributes</a></li>
  </ul>
  
  <!-- Tab panes -->
  
  <div class="tab-content">
    <div role="tabpanel" class="tab-pane active" id="pi"><tiles2:insertTemplate template="personalInfo.jsp"/></div>
    <div role="tabpanel" class="tab-pane" id="certificates"><tiles2:insertTemplate template="certs.jsp"/></div>
    <div role="tabpanel" class="tab-pane" id="groups">Groups and roles</div>
    <div role="tabpanel" class="tab-pane" id="attributes">Attributes</div>
  </div>
</div>