$('body').on('hidden.bs.modal', '.modal', function () {
    $(this).removeData('bs.modal');
});


function bulkUserSetup(){
	$('#bulk-user-selector').click(function(e){
		var checked = $(this).prop("checked");
		var checkboxes = $('.user-checkbox');
		checkboxes.prop("checked", checked);
		$('.btn-bulk-user').attr("disabled", !checkboxes.is(":checked"));
		
	});
	
	var checkboxes = $('.user-checkbox');
	var buttons = $('.btn-bulk-user');
	buttons.attr("disabled", true);
	
	checkboxes.click(function() {
		buttons.attr("disabled", !checkboxes.is(":checked"));
	});
}

function setupFormattedDNMagic(){
	
	$('div.formattedDN').click(function(){
		$('span',this).toggle();
	});
	
	$('#toggleFormattedDNsHandle').click(function(){
		$('div.formattedDN').click();
	});
}


function pageSetup() {
	$('[data-toggle="tooltip"]').tooltip();
	$('#voms-menu-' + namespaceName + '-'+ actionName).addClass("active");

	$('#request-tab a').click(function(e) {
		e.preventDefault()
		$(this).tab('show')
	});
	
	
}

function voReqSetup() {
	
	$('#reject-vo-reqs-modal').on('show.bs.modal', function (event) {
		var numRequests = $('.vo-req-checkbox:checked').size();
		var modal = $(this);
		modal.find('#rej-req-count').text(numRequests);
	});
	
	$('#vo-req-toggler').click(function(e) {
		var checked = $(this).prop("checked");
		var checkboxes = $('.vo-req-checkbox');
		checkboxes.prop("checked", checked);
		$('.vo-req-buttons').attr("disabled", !checkboxes.is(":checked"));
		$('.vo-req-i-buttons').attr("disabled", 
				!$('.vo-req-buttons').attr("disabled"));
	});

	var checkboxes = $('.vo-req-checkbox');
	var buttons = $('.vo-req-buttons');
	var ibuttons = $('.vo-req-i-buttons');

	checkboxes.click(function() {
		buttons.attr("disabled", !checkboxes.is(":checked"));
		ibuttons.attr("disabled", checkboxes.is(":checked"));
		$('#vo-req-toggler').prop("checked",
				false);
	});
}

function cleanupValidationErrors(form){
	$(form).find("div.has-error").removeClass("has-error");
    $(form).find("div.has-feedback").removeClass("has-feedback");
    $(form).find("span.s2_help_inline").remove();
    $(form).find("span.s2_feedback").remove();
    $(form).find("div.s2_validation_errors").remove();
}

$(function() {
	pageSetup();
	bulkUserSetup();
	setupFormattedDNMagic();
});