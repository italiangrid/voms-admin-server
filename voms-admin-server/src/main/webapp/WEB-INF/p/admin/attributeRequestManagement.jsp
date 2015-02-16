<%--

    Copyright (c) Members of the EGEE Collaboration. 2006-2009.
    See http://www.eu-egee.org/partners/ for details on the copyright holders.

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
      Andrea Ceccanti (INFN)

--%>
<%@include file="/WEB-INF/p/shared/taglibs.jsp"%>

<s:if
	test="requesterInfo.getMultivaluedInfo('requestedGroup').size() > 0">

	<s:set var="requestedGroups"
		value="requesterInfo.getMultivaluedInfo('requestedGroup')" />

	<s:set var="groupVisibility" value="%{'visible'}" />

	<s:if
		test="requesterInfo.getMultivaluedInfo('requestedGroup').size() >= 10">
		<tr>
			<td colspan="3" class="no-border-bottom">User has requested more
				than 10 groups. <span id="groupToggler_<s:property value='id'/>"
				class="clickable" style="color: #456aac;">Show selection</span>
			</td>

		</tr>
	</s:if>

	<tr id="req_groups_header_<s:property value='id'/>">
		<th colspan="3" style="border-bottom: 0"><s:checkbox
				id="apprGroupSel_%{id}" name="notSet" />Requested groups</th>
	</tr>

	<tr id="req_groups_row_<s:property value='id'/>">
		<td colspan="2" class="no-border-bottom">
			<ul id="reqGroupList_<s:property value='id'/>">
				<s:iterator value="#requestedGroups" var="name">
					<li><s:checkbox name="approvedGroups" fieldValue="%{name}"
							theme="simple" cssClass="groupCheck_%{id}" value="false" />
						<s:property value="name" /></li>
				</s:iterator>
			</ul>
		</td>
		<td class="no-border-bottom" />
	</tr>
	<script>
                    $('#apprGroupSel_<s:property value="id"/>').change(function(){
                      var checked = $(this).attr("checked");
                      $('.groupCheck_<s:property value="id"/>').attr("checked", checked);
                      $('.groupCheck_<s:property value="id"/>').change(); });                    
                  
                    <s:if test="requesterInfo.getMultivaluedInfo('requestedGroup').size() >= 10">
                      $('#req_groups_header_<s:property value='id'/>').hide();
                      $('#req_groups_row_<s:property value='id'/>').hide();
                      $("#groupToggler_<s:property value='id'/>").click(function(){
                        $('#groupToggler_<s:property value='id'/>').text('Hide selection');
                        $('#req_groups_header_<s:property value='id'/>').toggle();
                          $('#req_groups_row_<s:property value='id'/>').toggle();
                          if ($('#req_groups_header_<s:property value='id'/>').is(':visible')) {
                            
                            $('#groupToggler_<s:property value='id'/>').text('Hide selection');
                            $('html, body').animate({
                                scrollTop: $("#req_info_<s:property value='id'/>").offset().top
                            }, 1000);
                          } else {
                            
                            $('#groupToggler_<s:property value='id'/>').text('Show selection');
                          }
                      });
                      </s:if>
                  </script>
</s:if>