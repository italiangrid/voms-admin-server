$(document).ready(function(){

	$('#tabs').tabs();
	
	$('.tableRow:odd').addClass('tableRowOdd');
	$('.tableRow:even').addClass('tableRowEven');
	
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
	
	
	/** ACL entry selection code **/
	$('.entryType').hide();
	$('#'+$('.aclEntrySelector:checked').val()).show();
	
	$('.aclEntrySelector').click( function(){
		
		var selectedEntry = $(this).val();
		$('.entryType:visible').hide();
		$('#'+selectedEntry).show();
	});
	/** end of ACL entry selection code **/
	
	$('#allPermsCheckbox').click( function(){
		
			if ($(this).is(":checked")){
				$('.permissionCheckbox').attr("checked", true);
			}
			
			
				
	});
	
	$('.permissionCheckbox').click( function(){
		
		if (!$(this).is(":checked")){
			$('#allPermsCheckbox').attr("checked", false);
		}
	});
});
	


