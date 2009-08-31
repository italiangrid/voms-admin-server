<%@include file="/WEB-INF/p/shared/taglibs.jsp"%>

<h1>
Configuration information:
</h1>


<h2>
VOMS-Admin URL for this vo:
</h2>

<s:textarea value="%{contactString}" readonly="true" rows="2" cols="80" cssClass="configurationInfo"/>

<h2>
VOMSES string for this vo:
</h2>


<s:textarea value="%{vomsesConf}" readonly="true" rows="2" cols="80" cssClass="configurationInfo"/>

<h2>
Example Mkgridmap configuration for this vo:
</h2>
<s:textarea value="%{mkGridmapConf}" readonly="true" rows="2" cols="80" cssClass="configurationInfo"/>



