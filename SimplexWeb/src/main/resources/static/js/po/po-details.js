$(function(){
	//Compulsory first line should be buildNavPath **
	buildNavPath({'Home':'user/home', 'PO':'#', 'PO Details':'#'});
	
	$('.openPOEmailModalBtn').on('click', function(){
		$('#sendPOEmailModal').modal('show');
	});
	
	$('.sendPOEmailBtn').on('click', function(){
		var $form = $('#sendPOEmailModalForm');
		
		//$form.submit();
		$.ajax({
			url: $form.attr('action'),
			data: $form.serialize(),
			method: 'POST',
			success: function(data) {
				alert(data);
			}
		});
	});
});