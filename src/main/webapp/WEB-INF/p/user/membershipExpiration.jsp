<%@include file="/WEB-INF/p/shared/taglibs.jsp"%>

<voms:hasPermissions var="canReadPI" context="vo" permission="PERSONAL_INFO_READ"/>

<s:if test="#attr.canReadPI or (#attr.currentAdmin.is(model))">
  <div id="membership-expiration-pane">
    <div class="reloadable">
        
        <dl>
            <dt>Membership expiration date:</dt>
            <dd class="userMembershipEndTime">
              
              
              <s:text name="format.date" var="userEndTime">
                <s:param value="endTime"/>
              </s:text>
              
              <voms:hasPermissions var="canDelete" context="vo" permission="rw"/>
              
                    
              <s:form theme="simple" 
                action="set-membership-expiration"
                onsubmit="ajaxSubmit(this,'membership-expiration-pane'); return false;">
                <s:token/>
                <s:hidden name="userId" value="%{id}"/>
                <s:textfield id="membershipExpirationField" 
                  name="expirationDate" 
                  value="%{#userEndTime}" 
                  size="10" 
                  cssClass="membershipExpiration"
                  readonly="%{#attr.canDelete == false}"
                  />
                
                  <s:submit value="%{'Change'}" disabled="%{#attr.canDelete == false}"/>
                
              </s:form>
              <s:fielderror fieldName="expirationDate"/>
              <tiles2:insertTemplate template="../shared_20/errorsAndMessages.jsp"/>
          </dd>
        </dl>
    </div>
  </div>
</s:if>