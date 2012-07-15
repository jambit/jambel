$(function() {

	$('button').click(function() {
		var button = $(this);
		button.prop({
			disabled: true
		});
		var resetUrl = $(this).data('url');
		$.post(resetUrl, function(data, textStatus, jqXHR) {
			button.next().html(jqXHR.responseText);
			button.prop({
				disabled: false
			});
		});
	});

});