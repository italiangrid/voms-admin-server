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

$(function() {
	pageSetup();
});