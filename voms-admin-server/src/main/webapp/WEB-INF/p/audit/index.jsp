<%@include file="/WEB-INF/p/shared/taglibs.jsp"%>

<s:if test="#attr.currentAdmin.admin == null">
  You are not a VO admin. You will see nothing around here.
</s:if>
<s:else>
  <h1>Audit log</h1>


  <s:form theme="simple">
    <table>
      <thead>

        <s:date
          name="searchParams.fromTime"
          var="ft"
          format="MM/d/YYYY" />
        <s:date
          name="searchParams.toTime"
          var="tt"
          format="MM/d/YYYY" />


        <tr>
          <td
            class="audit-log-search-panel"
            colspan="4"><s:label
              for="sp_from"
              value="From:"
              cssClass="input-label" /> <s:textfield
              id="sp_from"
              name="searchParams.fromTime"
              size="8"
              cssClass="input-date"
              value="%{#ft}" /> <s:label
              for="sp_to"
              value="To:"
              cssClass="input-label" /> 
          
              <s:textfield
              id="sp_to"
              name="searchParams.toTime"
              label="To"
              value="%{#tt}"
              size="8"
              cssClass="input-date" /> <s:label
              for="sp_filter_type"
              value="Filter by:"
              cssClass="input-label" /> <s:select
              id="sp_filter_type"
              name="searchParams.filterType"
              list="{'principal', 'type'}"
              value="searchParams.filterType" /> 
              
              <s:set value="searchParams.filterString" var="fs"/>
              
              <input
                id="sp_filter" 
                type="text"
                name="searchParams.filterString"
                placeholder="use % as a wildcard, e.g.: %VOMS%"
                size="50"
                value="<s:property value='searchParams.filterString'/>"> 
              
              
              <s:submit value="%{'Filter'}" /></td>
        </tr>
      </thead>
      <tbody>
        <s:if test="model.results.isEmpty()">
          <tr>
            <td colspan="4">No records found matching filter.</td>
          </tr>
        </s:if>
        <s:else>
          <tr>
            <th>When</th>
            <th>Event type</th>
            <th colspan="2">Description</th>
          </tr>
        </s:else>
        <s:iterator value="model.results" var="event">
          <tr>
            <td style="width:15%"><s:date
                name="timestamp"
                nice="true"
                format="struts.date.format.past" /></td>
                
            
            <td style="width:15%"><s:property
                value="eventNameFormatter.formatEventName(#event.type)" /></td>
                    
            <td style="width:50%" class="al-desc">
              <div class="audit-log-principal">
                <s:property
                  value="adminNameFormatter.formatAdminName(#event.principal)" />
              </div>
                <s:property
                value="eventDescriptor.buildEventDescription(#event)" />
            </td>
            <td>
                <a 
                  href="#" 
                  class="al-trigger"
                  data-target="al_${event.id}">more info</a>
          
            </td>
          </tr>
          <tr class='al-dtl'>
            <td 
              id="al_${event.id}" 
              colspan="4" 
              class='al-dtl al-dtl-hidden'>
              <tiles2:insertTemplate template="audit_log_detail.jsp"/>
            </td>
          </tr>
        </s:iterator>
      </tbody>
    </table>
  </s:form>
</s:else>


<script type="text/javascript">
	$('.input-date').datepicker();
</script>
