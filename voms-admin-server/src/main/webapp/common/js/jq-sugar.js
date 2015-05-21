/*
 * Copyright (c) Members of the EGEE Collaboration. 2006-2009.
 * See http://www.eu-egee.org/partners/ for details on the copyright holders.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Authors:
 * 	Andrea Ceccanti (INFN)
 */

function vomsErrorMessage(node, text){
	
	$(node).prepend("<div class='errorMessage'>"+text+"</div>");
	
}
function hideIfNoPermission(node, gid, rid, perms){
	
	var permActionURL = ajaxBaseURL +"permission.action";
	var params = {groupId : gid, permissionString: perms};
	
	if (rid != null){
		params['rid'] = rid;
	}
	
	jQuery.getJSON(permActionURL, params, function(data, status){
		if (status != "success")
			alert('Error executing ajax call... panel status will not be persistent across calls to the server.');
		else{
			if (! data.hasPermission){
				$(node).hide();
			}else{
				$(node).show();
			}
		}
	});
	
}

function disableIfNoPermission(node,gid,rid,perms){
	
	var permActionURL = ajaxBaseURL +"permission.action";
	var params = {groupId : gid, permissionString: perms};
	
	if (rid != null){
		params['rid'] = rid;
	}
	
	jQuery.getJSON(permActionURL, params, function(data, status){
		if (status != "success")
			alert('Error executing ajax call... panel status will not be persistent across calls to the server.');
		else{
			if (! data.hasPermission){
				$(node).attr('disabled', 'disabled');
			}else{
				$(node).attr('disabled','');
			}
		}
	});
	
}

function enableAddToGroupForm(){
	
	$('#add-to-group').map(function(){
			
		var gid = $(this).find('[name="groupId"]').val();
		disableIfNoPermission($(this).find(':submit'), gid, null, 'CONTAINER_READ|MEMBERSHIP_READ|MEMBERSHIP_WRITE');
		
		return this;
	});
}

function enableDeleteRoleAttributeForms(){
	 
	$('form.deleteRoleAttributeForm').map(function(){
		
		var gid = $(this).find('[name="groupId"]').val();
		var rid = $(this).find('[name="roleId"]').val();
		
		disableIfNoPermission($(this).find(':submit'), gid, rid, 'ATTRIBUTES_READ|ATTRIBUTES_WRITE');
		
		return this;
	
	});
}

function enableSetRoleAttributeForm(){
	
	$('#setRoleAttributeForm').map(function(){
		
		var gid = $(this).find('[name="groupId"]').val();
		var rid = $(this).find('[name="roleId"]').val();
		
		disableIfNoPermission($(this).find(':submit'), gid, rid, 'ATTRIBUTES_READ|ATTRIBUTES_WRITE');
		
		return this;
	});
	
}

function ajaxSubmit(formNode, outputPanelId){
	
	var url = $(formNode).attr('action');
	var params = $(formNode).serializeArray();
	
	if (url == undefined){
		// Should log something to the javascript console
		return;
	}
	
	ajaxLoad(outputPanelId,url,params);
}


function updateCSRFToken(lastPaneId){
	
	var lastTokenValue = $('#'+lastPaneId).find('[name="struts.token"]').val();
	
	if (lastTokenValue != undefined){
		
		$('input[name="struts.token"]').attr('value', lastTokenValue);
	}
}

function ajaxLoad(id, url, params){
	
	$('#loadDiv').show();
	
	$('#'+id+' div.reloadable').fadeTo("fast",0.50);
	
	$('#'+id).load(url, params, function(responseText,textStatus,req){
		if (textStatus == 'error'){
			alert("Error executing ajax request!");
			$(this).html(responseText);
		}
		
		$('#loadDiv').hide();
			
		$('#'+id+' div.reloadable').fadeTo("fast",1.00);
		eyeCandy();
		updateCSRFToken(id);
	});
}





function blinkBackground(node){
	var bgColor = $(node).css('background-color');
	
	$(node).animate({ backgroundColor: '#FFF58F'},50, null, function(){
		$(this).animate({ backgroundColor: bgColor},180);});
	
}

function eyeCandy(){
	
	formattedDNMagic();
	
	$('input:text.registrationField, textarea[readonly!=readonly].registrationField').focus(function(){
		
		$(this).addClass("activeTextField");
	});
	
	$('input:text.registrationField, textarea[readonly!=readonly].registrationField').blur(function(){
		
		$(this).removeClass("activeTextField");
	});
	
	
	$('.panelButton').click( function(){
		$(this).parent().find('.panelContent').toggle();
	});
	
	// $('input[type="submit"]').addClass('submitButton');
	
	$('*[readonly="readonly"]').addClass('readOnly');
	
	$('select').addClass('selectBox');
	
	enableSetRoleAttributeForm();
	enableDeleteRoleAttributeForms();
	
	
	$('input.checkboxError').wrap("<span class='checkboxValidationError'></span>");
	
	$('ul.message li').click(function(){
		$(this).fadeOut('normal');
	});
	
	$('ul.actionError li').click(function(){
		$(this).fadeOut('normal');
	});
	
	$('#userSelectorTrigger').change(function(){
		
		var checked = $(this).attr("checked");
		$('.userCheckbox').attr("checked", checked);
		
		$('.userCheckbox').change();
		
	});
	
	$('.userCheckbox').change(function(){
		
		var checked = $(this).attr("checked");
		if (checked)
			$(this).closest('tr').addClass('userSelected');
		else
			$(this).closest('tr').removeClass('userSelected');
	});
	
	
}

function countSelectedUsers(){
	return $('.userCheckbox:checked').size();
}
function showAUPContent(node){
	
	var aupText = $(node).next().text();
	$('#aclShowTextArea').val(aupText);
}


function initializePanelHeaders(){
	
	$('.info-tab h2').click(function(){
		
		var node =  $(this).next();
		node.toggle();
		
		var visible = $(node).is(':visible');
		var panelId = $(node).attr('id');
		
		jQuery.getJSON(ajaxBaseURL+'store.action', {panelId:panelId, visible:visible}, function(data, status){
			if (status != "success")
				alert('Error executing ajax call... panel status will not be persistent across calls to the server.');
		});
		
	});
	
}

function aclEntryStuff(){
	
	/** ACL entry selection code **/
	$('.entryType').hide();
	$('#'+$('.aclEntrySelector').val()).show();
	
	$('.aclEntrySelector').change( function(){
		
		var selectedEntry = $(this).val();
		$('.entryType:visible').hide();
		$('#'+selectedEntry).show();
		
	});
	
	
	$('#allPermissionHandle').click(function(){
		$('.permissionCheckbox').attr("checked", true);
		return false;
	});
	
	$('#noPermissionHandle').click(function(){
		$('.permissionCheckbox').attr("checked", false);
		return false;
	});
	
	$('#browsePermissionHandle').click(function(){
		$('.permissionCheckbox').attr("checked", false);
		
		$('.permissionCheckbox[value="CONTAINER_READ"]').attr("checked", true);
		$('.permissionCheckbox[value="MEMBERSHIP_READ"]').attr("checked", true);
		return false;
	});
	
	
	function checkPermissions(prefix){
		$('.permissionCheckbox[value^="'+prefix+'"]').map(function(){
			if ($(this).is(':checked'))
				$(this).attr('checked', false);
			else
				$(this).attr('checked', true);
			
			return this;
		})
	}
	
	$('#containerHandle').click(function(){
		checkPermissions('CONTAINER');
	});
	
	$('#membershipHandle').click(function(){
		checkPermissions('MEMBERSHIP');
	});
	
	$('#aclHandle').click(function(){
		checkPermissions('ACL');
	});
	$('#gaHandle').click(function(){
		checkPermissions('ATTRIBUTES');
	});
	
	$('#piHandle').click(function(){
		checkPermissions('PERSONAL_INFO');
	});
	
	$('#reqHandle').click(function(){
		checkPermissions('REQUESTS');
	});
	
	$('#suspendHandle').click(function(){
		checkPermissions('SUSPEND');
	});
	
	$('#propagateHandle').click(function(){
		$('#propagateCheckbox').click();
	});
	
	
	$('tr.borderTop').css('border-top','2px solid rgb(200,200,200)');
	$('tr.clickable').css('border-top','1px solid rgb(200,200,200)');
	
	$('tr.borderBottom').css('border-bottom','2px solid rgb(200,200,200)');
	
	ajaxSubmit($('#aclSelectionForm').map(function(){return this;}),'aclShowPane');
	
	var roleSelectVal = $('#aclRoleSelector').val();
	
	if (roleSelectVal != undefined && roleSelectVal != -1)
		$('#defaultACLSelector').attr('disabled','disabled');
	
	$('#aclRoleSelector').change(function(){
			var val = $(this).val();
			
			if (val != -1){
				$('#defaultACLSelector').attr('disabled','disabled');
				$('#defaultACLSelector').attr('checked','');
			}
			else
				$('#defaultACLSelector').attr('disabled','');
	});
	
	$('#showACLHelpHandle').click(function(){
		$(this).fadeTo('fast',.8);
		$('#aclHelp').slideToggle('fast', function(){
			
			if ($(this).is(':visible'))
				$('#showACLHelpHandle').text('Hide help');
			else
				$('#showACLHelpHandle').text('Show help');
			
			$('#showACLHelpHandle').fadeTo('fast',1);
				
		});
	});
	
}

function formattedDNMagic(){
	
	$('div.formattedDN').click(function(){
		$('span',this).toggle();
	});
	
	$('#toggleFormattedDNsHandle').click(function(){
		$('div.formattedDN').click();
	});
}


function pageSetup(){

	$('#loadDiv').hide();
	
	initializePanelHeaders();
	
	eyeCandy();
	
	aclEntryStuff();
	
	
}



function openSuspendDialog(node, dialogId, text){
	
	if ($(node).is(':submit')){
		
		confirmFunc = function(){
			
			var formInputElem = '#'+dialogId + "_suspensionReasonInputField";
			var suspensionReason = $(formInputElem).val();
			
			var form = $(node).closest('form');
			
			setFormActionFromSubmitButton(form, node);
			
			form.append("<input type='hidden' name='suspensionReason' value='"+suspensionReason+"'/>");
			
			if ($(node).attr('form').onsubmit != undefined){
				
				$(node).attr('form').onsubmit();
				$('#'+dialogId).dialog('destroy');
				return false;
			}
			else
				form.submit();
				
		};
		
	}else{
		
		var dest = $(node).attr('href');
		
		confirmFunc  = function(){
			window.location = dest;
		};
	}
	
	$('#'+dialogId+" .dialogMessage").text(text);
	
	$('#'+dialogId).dialog({resizable: false,
		width: 800,
		modal: true,
		closeOnEscape: true,
		autoOpen: false,
		overlay: {
		backgroundColor: '#000',
		opacity: 0.5},
		buttons: {
			'Confirm suspension?': confirmFunc,
			Cancel: function() {
				$(this).dialog('close');
				return false;
			}
		}
	});
	
	$('#'+dialogId).dialog('open');
	
	return false;
	
}

function setFormActionFromSubmitButton(formNode, submitButtonNode){
	
	var action = $(submitButtonNode).attr('name');
	
	if (action != undefined && action != ""){
		var actionName = action.substring(action.lastIndexOf(':')+1)+".action";
		var formAction = formNode.attr('action');
		var newActionStr = formAction.slice(0,formAction.lastIndexOf('/')+1)+actionName;
		formNode.attr('action', newActionStr);
	}
}


function openConfirmDialog(node,dialogId,text){
	
	if ($(node).is(':submit')){
		
		confirmFunc = function(){
			
			var form = $(node).closest('form');
			setFormActionFromSubmitButton(form, node);
			form.submit();
		};
		
	}else{
		
		var dest = $(node).attr('href');
		
		confirmFunc  = function(){
			window.location = dest;
		};
	}
	
	
	$('#'+dialogId+" .dialogMessage").text(text);
	
	$('#'+dialogId).dialog({resizable: false,
		width: 600,
		modal: true,
		closeOnEscape: true,
		autoOpen: false,
		overlay: {
		backgroundColor: '#000',
		opacity: 0.5},
		buttons: {
			Confirm: confirmFunc,
			Cancel: function() {
				$(this).dialog('close');
			}
		}
	});
	
	$('#'+dialogId).dialog('open');
	
}

function openConfirmChangeReacceptancePeriodDialog(node, dialogId) {
	
	confirmFunc = function(){
		var form = $(node).closest('form');
		var reqText = $('#aupChangeReacceptancePeriodRequiredText').val();
		if (reqText.toLowerCase() == "yes") {
			form.submit();
			return true;
		}
		$('#aupChangeReacceptancePeriodRequiredText_alert').show();
		return false;
	};
	
	$('#aupChangeReacceptancePeriodRequiredText').val("");
	$('#aupChangeReacceptancePeriodRequiredText_alert').hide();
	
	$('#'+dialogId).dialog({resizable: false,
		width: 800,
		modal: true,
		closeOnEscape: true,
		autoOpen: false,
		overlay: {
		backgroundColor: '#000',
		opacity: 0.5},
		buttons: {
			Confirm: confirmFunc,
			Cancel: function() {
				$(this).dialog('close');
				return false;
			}
		}
	});
	
	$('#'+dialogId).dialog('open');
	$('#aupReacceptanceRequiredText').focus();
	return false;
	
}

function bulkRequestSetup(){
	
	$('#req-selector').click(function(e){
		var checked = $(this).attr("checked");
		var checkboxes = $('.req-checkbox');
		checkboxes.attr("checked", checked);
		
		var checkboxesChecked = checkboxes.is(":checked");
		if (checkboxesChecked) {
			$('.req-row').removeClass('req-row-noselect');
			$('.req-toolbar-btns').removeClass('req-toolbar-btns-hidden');
		}else {
			$('.req-row').addClass('req-row-noselect');
			$('.req-toolbar-btns').addClass('req-toolbar-btns-hidden');
		}
	});
	
	var checkboxes = $('.req-checkbox');
	
	checkboxes.click(function() {
		var checkboxesChecked = checkboxes.is(":checked");
		
		if (checkboxesChecked) {
			$('.req-row').removeClass('req-row-noselect');
			$('.req-toolbar-btns').removeClass('req-toolbar-btns-hidden');
		}else {
			$('.req-row').addClass('req-row-noselect');
			$('.req-toolbar-btns').addClass('req-toolbar-btns-hidden');
		}
	});
	
	
	$('.req-detail-trigger').click(function(e) {
		e.preventDefault();
		var detailNode = $(this).closest('td').find('.req-detail');
		if ($(detailNode).hasClass("req-detail-hidden")){
			$(this).text("hide info");
			$(detailNode).removeClass("req-detail-hidden");
		}else {
			$(this).text("more info");
			$(detailNode).addClass("req-detail-hidden");
		}
	});

	if ($('.req-row').size() < 2) {
		checkboxes.attr("disabled", true);
		$('#req-selector').attr("disabled", "disabled");
	}else {
		checkboxes.attr("disabled", false);
		$('#req-selector').removeAttr("disabled");
	}
}

function rejectRequestDialog(node, dialogId){
	
	if ($(node).is(':submit')){
		
		confirmFunc = function(){
			var form = $(node).closest('form');
			var motivation = $('#confirmRejectedRequestDialog_input').val();
			form.append("<input type='hidden' name='motivation' value='"+motivation+"'/>");
			setFormActionFromSubmitButton(form, node);
			form.submit();
		};
	}
	
	$('#confirmRejectedRequestDialog_input').val("");
	$('#'+dialogId).dialog({resizable: false,
		width: 800,
		modal: true,
		closeOnEscape: true,
		autoOpen: false,
		overlay: {
		backgroundColor: '#000',
		opacity: 0.5},
		buttons: {
			'Reject requests?': confirmFunc,
			Cancel: function() {
				$(this).dialog('close');
				return false;
			}
		}
	});
	
	$('#'+dialogId).dialog('open');
	$('#confirmRejectedRequestDialog_input').focus();
	
	return false;
};

function rejectSingleRequestDialog(submitNode, dialogId){
	
	if ($(submitNode).is(':submit')){
		
		confirmFunc = function(){
			var form = $(submitNode).closest('form');
			
			var action = $(submitNode).attr('formaction');
			form.attr('action', action);
			var motivation = $('#confirmRejectedRequestDialog_input').val();
			form.append("<input type='hidden' name='motivation' value='"+motivation+"'/>");
			form.submit();
		};
	}
	
	$('#confirmRejectedRequestDialog_input').val("");
	$('#'+dialogId).dialog({resizable: false,
		width: 800,
		modal: true,
		closeOnEscape: true,
		autoOpen: false,
		overlay: {
		backgroundColor: '#000',
		opacity: 0.5},
		buttons: {
			'Reject request?': confirmFunc,
			Cancel: function() {
				$(this).dialog('close');
				return false;
			}
		}
	});
	
	$('#'+dialogId).dialog('open');
	$('#confirmRejectedRequestDialog_input').focus();
	return false;
};

$(document).ready(function(){
	pageSetup();
	bulkRequestSetup();
});
