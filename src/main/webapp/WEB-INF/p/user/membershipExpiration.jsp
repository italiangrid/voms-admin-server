<%@include file="/WEB-INF/p/shared/taglibs.jsp"%>


<div id="membership-expiration-pane">
  <div class="reloadable">
      
      <dl>
          <dt>Membership expiration date:</dt>
          <dd class="userMembershipEndTime">
            
            
            <s:text name="format.date" var="userEndTime">
              <s:param value="endTime"/>
            </s:text>
            
            
                  
            <s:form theme="simple" 
              action="set-membership-expiration"
              onsubmit="ajaxSubmit(this,'membership-expiration-pane'); return false;">
              <s:token/>
              <s:hidden name="userId" value="%{id}"/>
              <s:textfield id="membershipExpirationField" name="expirationDate" value="%{#userEndTime}" size="10" cssClass="membershipExpiration"/>
              <s:submit value="%{'Change'}" 
                disabled="%{#canWrite? true: false}"/>
            </s:form>
            <s:fielderror fieldName="expirationDate"/>
            <tiles2:insertTemplate template="../shared_20/errorsAndMessages.jsp"/>
        </dd>
      </dl>
  </div>
</div>