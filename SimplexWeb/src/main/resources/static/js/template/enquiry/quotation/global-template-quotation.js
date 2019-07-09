$(document).ready(function(){
	
	buildNavPath({'Home':'user/home', 'Enquiry':'#', 'Enquiry Quotation':'#'});
	
	$('.tagsinput').tagsinput({
        tagClass: 'label label-primary'
    });
	
	$('.openEnquiryQuotationEmailModalBtn').on('click', function(){
		$('#sendEnquiryQuotationEmailModal').modal('show');
	});
	
	$('.sendEnquiryQuotationEmailBtn').on('click', function(){
		var $form = $('#sendEnquiryQuotationEmailModalForm');
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
	
	$('.dowmloadEnquiryQuotationBtn').on('click', function(){
		var enqNum = $('.ENQUIRY_NUMBER').html();
		var enqId = $('.ENQUIRY_ID').html();
		console.log(enqNum);
		console.log(enqId)
		var url = contextRoot+"enquiry/globalTemplateController/downloadGlobalTemplateQuotation?enquiryId=" +enqId +"&enquiryNumber=" +enqNum;
		
		var win = window.open(url);
		if (win) {
		    //Browser has allowed it to be opened
			win.blur();
			window.focus();
		} else {
		    //Browser has blocked it
		    alert('Please allow popups for this website');
		}
	});
});