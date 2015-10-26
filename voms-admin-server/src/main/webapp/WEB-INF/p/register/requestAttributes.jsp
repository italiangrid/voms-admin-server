<%--

    Copyright (c) Istituto Nazionale di Fisica Nucleare (INFN). 2006-2015

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
Group selection
</h1>
<p>
Please select the VO groups you would like to be member of.<br/>
<span class="strong">Note that your selection will be assessed by the VO or group
manager (where applicable) which could reject some of your choices.</span>
</p>

<s:form action="request-attributes" namespace="/register">
	<s:token/>
	
	<s:hidden name="requestId" value="%{model.id}" />
	<s:hidden name="confirmationId" value="%{confirmationId}" />
    <table class="table" style="margin-top: 1em;">
      <thead>
        <tr style="background-color: #f5f5f5;">
          <th><s:checkbox 
                name="notSet" 
                id="groupSelector"
                theme="simple"/></th>
          <th>VO group</th>
        </tr>
      </thead>
      <tbody>
        <s:iterator value="#request.voGroups">
          <tr class="tableRow">
            <td width="1%"><s:checkbox name="requestedGroups" fieldValue="%{name}" theme="simple" cssClass="groupCheckbox"/></td>
            <td><span class="group"></span><s:property value="name"/>
              <div class="groupDescription"><s:property value="description"/></div>
            </td>
          </tr>
        </s:iterator>
      </tbody>	
    </table>
	<s:submit value="%{'Continue'}" />
</s:form>

<script type="text/javascript">
  $('#groupSelector').change(function(){
    var checked = $(this).attr("checked");
    $('.groupCheckbox').attr("checked", checked);
    $('.groupCheckbox').change();
  
});
</script>