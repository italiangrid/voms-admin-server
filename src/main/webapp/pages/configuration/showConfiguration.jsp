<%--
 Copyright (c) Members of the EGEE Collaboration. 2006.
 See http://www.eu-egee.org/partners/ for details on the copyright
 holders.

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.

 Authors:
     Andrea Ceccanti - andrea.ceccanti@cnaf.infn.it
--%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://org.glite.security.voms.tags" prefix="voms"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>

<!-- Base URL of the admin interface -->
<div class="header1">
Configuration information
</div>

<div class="header2">
VOMS-Admin URL for this vo:
</div>

<div class="configurationInfo">
		${vomsAdminBaseURL}
</div>

<!-- Content of the vomses file -->
<div class="header2">
VOMSES string for this vo:
</div>
<div class="configurationInfo">
	${vomsesConf}
</div>

<div class="header2">
Example Mkgridmap configuration for this vo:
</div>

<div class="configurationInfo">
	${mkgridmapConf}
</div>
<div class="separator">
&nbsp;
</div>


<voms:pageCustomization pageName="showConfiguration.html" styleClass="customized-content"/>