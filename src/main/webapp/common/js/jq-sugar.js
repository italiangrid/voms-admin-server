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
	
});


