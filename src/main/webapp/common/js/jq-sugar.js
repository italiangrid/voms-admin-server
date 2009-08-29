function vomsErrorMessage(node, text){
	
	$(node).prepend("<div class='errorMessage'>"+text+"</div>");
	
}
function hideIfNoPermission(node, gid, rid, perms){
	
	var permActionURL = ajaxBaseURL +"permission.action";
	var params = {groupId : gid, roleId : rid, permissionString: perms};
	
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
	var params = {groupId : gid, roleId : rid, permissionString: perms};
	
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
	
	if (url == undefined)
		return;
	
	if (params != "" && params != null)
		url += "?"+params;
	
	ajaxLoad(outputPanelId,url);
}

function ajaxLoad(id, url){
	
	$('#loadDiv').show();
	
	$('#'+id+' div.reloadable').fadeTo("fast",0.50);
	
	$('#'+id).load(url, null, function(responseText,textStatus,req){
		if (textStatus == 'error'){
			$('#loadDiv').hide("slow");
			eyeCandy();
		}
		else{
			
			$('#loadDiv').hide();
			var msg = $.cookie('error');
			if (msg != null){
				// Remove trailing quotes
				
				vomsErrorMessage(this,msg.substring(1,msg.length-1));
				$.cookie('error', null);
			}
			
			$('#'+id+' div.reloadable').fadeTo("fast",1.00);
			
			eyeCandy();
		}
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
	/** wufoo eye candy **/
	$('.field').focus(function(){
		$(this).parent().addClass("focused");
	});

	$('.field').blur(function(){
		$(this).parent().removeClass("focused");
	});
	
	$('.panelButton').click( function(){
		$(this).parent().find('.panelContent').toggle()
	});
	
	$('input[type="submit"]').addClass('submitButton');
	
	$('select').addClass('selectBox');
	
	enableSetRoleAttributeForm();
	enableDeleteRoleAttributeForms();
	
	$('select').change(function(){
		blinkBackground(this);
	});
	
	formattedDNMagic();
	
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
	$('#'+$('.aclEntrySelector:checked').val()).show();
	
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
			
			if (val != -1)
				$('#defaultACLSelector').attr('disabled','disabled');
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


	


