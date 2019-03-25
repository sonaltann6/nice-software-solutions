$(document).ready(function(){
	//Compulsory first line should be buildNavPath **
	buildNavPath({'Home':'user/home', 'Documents':'#', 'My Documents':'#'});
	
	toastr.options = TOASTER_OPTIONS;
	
	/*chosen-select*/
	$('.chosen-select').chosen({
		width: "100%",
		max_selected_options: 3
	});
	
	$('.documentCategoryLi').on('click', function() {
		var classActive = 'btn-warning';
		var documentCategoryAbbr = $(this).data('document-category-abbr');
		$('.documentCategoryLi').each(function(i, obj) {
			$(obj).removeClass(classActive);
		});
		$(this).addClass(classActive);
		loadFiles();
	});
	
	if($('.partnerIdTextBox').val() < 1){
		var documentOwnerPartner = $('#documentOwnerPartner').val();
		var partnerIdTextBoxValue = $('.partnerIdTextBox').val();
		
		$('.partnerIdTextBox').val(documentOwnerPartner);
	}else{
		var documentOwnerPartner = $('#documentOwnerPartner').val();
		var partnerIdTextBoxValue = $('.partnerIdTextBox').val();
		
		if($('#documentOwnerPartner option[value='+partnerIdTextBoxValue+']').length > 0){
			$('#documentOwnerPartner').val(partnerIdTextBoxValue).trigger("chosen:updated");
		}else{
			var firstSelectValue = $("#documentOwnerPartner option:first").val();
			$("#documentOwnerPartner").val(documentOwnerPartner);
			$('.partnerIdTextBox').val(firstSelectValue);
		}
	}
	
	loadFiles();
	
	//Upload file directly after choosing
	$('input[type=file].upload-file-input').change(function() { 
	    // select the form and submit
		debugger
		if ($('.upload-file-input').get(0).files.length == 0) {
		    return false;
		}
		
		var form = $('#uploadDocumentInFolderForm')[0]; // You need to use standard javascript object here
		var formData = new FormData(form);
		var documentCategoryId = $('li.documentCategoryLi.btn-warning').data('document-category-id');
		formData.append('documentCategoryId', documentCategoryId);
		
		$.ajax({
            type: "POST",
            enctype: 'multipart/form-data',
            url: $('#uploadDocumentInFolderForm').attr('action'),
            data: formData,
            processData: false,
            contentType: false,
            cache: false,
            success: function (data) {
            	try{
                	toastr['success']("File saved successfully", "Success");
                	setTimeout(function(){// wait for 2 secs
                        location.reload(false); // then reload the page.
                   }, 2000);
            	}catch (e) {
            		hideloader();
				}
            },
            error: function (request, status, error) {
            	console.log("document-manager.js error : " + error);
            	toastr['error']("Error occurred", "Error");
            },
            complete: function(){
            	//debugger
            	//$('.fileinput-exists').trigger('click');
            	//window.location.reload();
            }
        });
    });
	
	//Document search box
	$('.file-search-box').on('input', function(e) {
		var searchBoxText = $(this).val();
		
		//Check if files are loaded
		if($('.file-box').length < 1){
			return false;
		}
		
		$('.file-box').each(function(index, $filebox) {
			var fileNameText = $($filebox).find('.file-name-text')[0].innerText.toLowerCase();
			var fileDateText = $($filebox).find('.file-added-date')[0].innerText.toLowerCase();
			
			if(fileNameText.indexOf(searchBoxText.toLowerCase()) != -1 || 
				fileDateText.indexOf(searchBoxText.toLowerCase()) != -1 ||
				searchBoxText==''){
				$(this).show();
			}else{
				$(this).hide();
			}
			
		});
	});
});


//On change of user dropdown list
$('#documentOwnerPartner').on('change', function(){
	loadFiles();
});


//Display all files to ajax page (right section)
function loadFiles(){
	var documentOwnerPartnerId = $('#documentOwnerPartner').val();
	var documentCategoryId = $('.documentCategoryLi.btn-warning').data('document-category-id');
	
	var URL = contextRoot + 'documents/documentManagerController/getDocumentsListAjaxPageForDocumentManager';
	
	$.ajax({
		url : URL,
		data : 'documentOwnerPartnerId='+documentOwnerPartnerId+'&documentCategoryId='+documentCategoryId,
		method : 'post',
		success : function(data){
			$('.documentManagerFilesDiv').html('');
			$('.documentManagerFilesDiv').append(data);
		}
	});
	
//Download file on click
	function downloadDocument(obj){
		var file_path = $(obj).data('doc-url');
		var a = document.createElement('A');
		a.href = file_path;
		a.download = file_path.substr(file_path.lastIndexOf('/') + 1);
		document.body.appendChild(a);
		a.click();
		document.body.removeChild(a);
	}
}