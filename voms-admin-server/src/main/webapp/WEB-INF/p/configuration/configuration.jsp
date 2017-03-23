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

<h1>
Configuration information:
</h1>

<h2>
VOMS-Admin URL for this vo:
</h2>

<s:textarea value="%{contactString}" readonly="true" rows="2" cols="100%" cssClass="configurationInfo"/>

<h2>
VOMSES string for this vo:
</h2>


<s:textarea value="%{vomsesConf}" readonly="true" rows="2" cols="100%" cssClass="configurationInfo"/>

<h2>
LSC configuration for this VOMS server:
</h2>
<s:textarea value="%{lsc}" readonly="true" rows="4" cols="100%" cssClass="configurationInfo"/>

<h2>
Example Mkgridmap configuration for this vo:
</h2>
<s:textarea value="%{mkGridmapConf}" readonly="true" rows="2" cols="100%" cssClass="configurationInfo"/>



