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
	var params = $(formNode).serialize();
	
	if (url == undefined){
		// Should log something to the javascript console
		return;
	}
	if (params != "" && params != null)
		url += "?"+params;
	
	ajaxLoad(outputPanelId,url);
}

function ajaxLoad(id, url){
	
	$('#loadDiv').show();
	
	$('#'+id+' div.reloadable').fadeTo("fast",0.50);
	
	$('#'+id).load(url, null, function(responseText,textStatus,req){
		if (textStatus == 'error'){
			alert("Error executing ajax request!");
			$(this).html(responseText);
		}
		
		$('#loadDiv').hide();
			
		$('#'+id+' div.reloadable').fadeTo("fast",1.00);
		eyeCandy();
	});
}



$(document).ready(function(){
	
	pageSetup();
	
});

function blinkBackground(node){
	var bgColor = $(node).css('background-color');
	
	$(node).animate({ backgroundColor: '#FFF58F'},50, null, function(){
		$(this).animate({ backgroundColor: bgColor},180);});
	
}

function eyeCandy(){
	
	colorTableRows();

	
	$('input:text.registrationField, textarea[readonly!=readonly].registrationField').focus(function(){
		
		$(this).addClass("activeTextField");
	});
	
	$('input:text.registrationField, textarea[readonly!=readonly].registrationField').blur(function(){
		
		$(this).removeClass("activeTextField");
	});
	
	
	$('.panelButton').click( function(){
		$(this).parent().find('.panelContent').toggle();
	});
	
	$('input[type="submit"]').addClass('submitButton');
	
	$('*[readonly="readonly"]').addClass('readOnly');
	
	
	$('select').addClass('selectBox');
	
	enableSetRoleAttributeForm();
	enableDeleteRoleAttributeForms();
	

	// $('select').change(function(){
	//	blinkBackground(this);
	// });
	
	formattedDNMagic();
	
	$('input.checkboxError').wrap("<span class='checkboxValidationError'></span>");
	
	$('ul.message li').each(function(){
		var el = this;
		setTimeout(function(){
			$(el).fadeOut('normal');
		}, 3000);
		
		return el;
	});
	
	$('ul.actionError li').click(function(){
		$(this).fadeOut('normal');
	});
	
	$('#membershipExpirationField[readonly!=readonly]').datepicker({ dateFormat: 'mm/dd/yy', constrainInput: true });
	// $('#membershipExpirationField[readonly!=readonly]').datepicker();
	
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

function colorTableRows(){
	
	$('.tableRow:odd').addClass('tableRowOdd');
	$('.tableRow:even').addClass('tableRowEven');
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

	


