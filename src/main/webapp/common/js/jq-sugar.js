$(document).ready(function(){

	$('.tableRow:odd').addClass('tableRowOdd');
	$('.tableRow:even').addClass('tableRowEven');
	$('ul.sf-menu').superfish();
	
	/** wufoo eye candy **/
	$('.field').focus(function(){
		$(this).parent().addClass("focused");
	});

	$('.field').blur(function(){
		$(this).parent().removeClass("focused");
	});
	
	$('#createForm').hide();
	
	/** create user/groups/roles link magic **/
	$('#startCreate').click( function(){
		$(this).hide();
		$('#createForm').show();
	});
	
	$('#stopCreate').click( function(){
		$("#createForm").hide();
		$('#startCreate').show();
		
	});
	
	
});


