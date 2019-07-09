/**
 * http://usejsdoc.org/
 */
$(document).ready(function(){
	//Compulsory first line should be buildNavPath **
	buildNavPath({'Home':'user/home', 'Master':'#', 'Company Master':'#'});
	
	toastr.options = TOASTER_OPTIONS;
	
	/*chosen-select*/
	$('.chosen-select').chosen({
		width: "100%",
		max_selected_options: 3
	});
	
	$('.dataTables-example').DataTable({
        pageLength: 25,
        responsive: true,
        dom: '<"html5buttons"B>lTfgitp',
        buttons: [
            {extend: 'copy',
            	exportOptions: {
                    columns: 'th:not(:last-child)'
                }
            },
            {extend: 'csv',
            	exportOptions: {
                    columns: 'th:not(:last-child)'
                }
            },
            {extend: 'excel', title: 'Company List',
            	exportOptions: {
                    columns: 'th:not(:last-child)'
                }
            },
            {extend: 'pdf', title: 'Company List',
            	exportOptions: {
                    columns: 'th:not(:last-child)'
                }
            },

            {extend: 'print',
                 customize: function (win){
                        $(win.document.body).addClass('white-bg');
                        $(win.document.body).css('font-size', '10px');

                        $(win.document.body).find('table')
                                .addClass('compact')
                                .css('font-size', 'inherit');
                },
            	exportOptions: {
                    columns: 'th:not(:last-child)'
                }
            }
        ]

    });
	
	
	$('.addNewCompanyBtn').on('click', function(){
		var MODAL_TYPE = $(this).data('modal-type');
		var MODAL_TITLE_NEW = "New Company";
		var MODAL_SUBTITLE_NEW = "Please fill the required fields to add new company.";
		
		var $modalElement = $('#addCompanyModal');
		var $viewForm = $('#addCompanyForm');
		
		setUpModelAccordingToType(MODAL_TYPE, $modalElement
				, $viewForm, null
				, MODAL_TITLE_NEW, MODAL_SUBTITLE_NEW
				, null, null)
		
	});
	
	$('.editCompanyBtn').on('click', function(){
		var MODAL_TYPE = $(this).data('modal-type');
		
		var MODAL_TITLE_UPDATE = "Company Information";
		var MODAL_SUBTITLE_UPDATE = "Shows the available details of selected company to edit.";
		var DATA_MAP = null; 
		var $modalElement = $('#editCompanyModal');
		var $viewForm = $('#editCompanyForm');
		
		var companyid = $(this).data('companyid');
    	if($(this).hasClass('editCompanyBtn')){
    		$.ajax({
    			url : contextRoot + 'master/company/getCompanyDetailsById',
    			type: 'GET',
    			dataType:'html',
	            data:'companyid='+companyid,
	            async:false,
	    		beforeSend: function(){
	    		    // Show loading image container
	    		    showloader();
	    		},
	    		success:function(data) {
	            	 data = JSON.parse(data).COMPANY;
	            	 DATA_MAP = {
	            			 'companyId' : data.companyId,
	            			 'companyName' : data.companyName,
	            			 'companyAddressLineOne' :data.companyAddressLineOne,
	            			 'companyAddressLineTwo' : data.companyAddressLineTwo,
	            			 'companyAddressPIN' : data.companyAddressPIN,
	            			 'companyAddressCountry.countryId' : data.companyAddressCountry.countryId,
	            			 'companyEmail' : data.companyEmail,
	            			 'companyPhonePrimary' : data.companyPhonePrimary,
	            			 'companyPhoneSecondary' : data.companyPhoneSecondary,
	            			 'companyUniqueCode' : data.companyUniqueCode
	            	 };
	    		},
	    		complete: function(data){
	                 // Hide loading image container
	            	 hideloader();
	            }
    		});
    	}
    	setUpModelAccordingToType(MODAL_TYPE, $modalElement
				, $viewForm, DATA_MAP
				, null ,null
				, MODAL_TITLE_UPDATE, MODAL_SUBTITLE_UPDATE)
	});
	
	$('.saveCompanyBtn').on('click', function(e){
		debugger
		e.preventDefault();
		var $modalElement = $('#addCompanyModal');
		var $viewForm  = '';
		var MODAL_TYPE = $modalElement.data('modal-type');
		var URL_STR = '';
		var saveType = $(this).data('save-type');
		
		if(saveType == 'NEW'){
			URL_STR = contextRoot+'master/company/saveNewCompany';
				
				$viewForm = $('#addCompanyForm');
		}else{
			URL_STR = contextRoot+'master/company/updateCompany';
			$modalElement = $('#editCompanyModal');
			$viewForm = $('#editCompanyForm');
		}
		console.log($viewForm.serialize());
		if(!$viewForm.valid()){
    		return false;
    	}else{
    		$.ajax({
    			url:URL_STR,
                type:'POST',
                dataType:'html',
                data:$viewForm.serialize(),
        		async:false,
        		beforeSend: function(){
        		    // Show loading image container
        		    showloader();
        		},
        		success:function(data) {
                	try{
                		$modalElement.modal('hide');
	                	toastr['success']("Company saved successfully", "Success");
	                	setTimeout(function(){// wait for 2 secs
	                        location.reload(false); // then reload the page.
	                   }, 2000);
                	}catch (e) {
                		hideloader();
					}
        		},
        		error: function (request, status, error) {
                	console.log("company.js error : " + error);
                	toastr['error']("Error occurred", "Error");
                },
                complete:function(data){
	                 // Hide loading image container
                	 hideloader();
                }
    		});
    	}
	});
	
	$('.viewCompanyBtn').on('click', function(){
		var companyid = $(this).data('companyid');
		$.ajax({
			url : contextRoot + 'master/company/getCompanyDetailsById',
			type:'GET',
            dataType:'html',
            data:'companyid='+companyid,
    		async:false,
    		beforeSend: function(){
    		    // Show loading image container
    		    showloader();
    		},
    		success : function(data){
    			data = JSON.parse(data);
    			try{
    				var dataMap = {
    					'companyId' : data.COMPANY.companyId,
	            		'companyName' : data.COMPANY.companyName,
	            		'companyAddressLineOne' :data.COMPANY.companyAddressLineOne,
	            		'companyAddressLineTwo' : data.COMPANY.companyAddressLineTwo,
	            	    'companyAddressPIN' : data.COMPANY.companyAddressPIN,
	            		'companyAddressCountry.countryName' : data.COMPANY.companyAddressCountry.countryName,
	            		'companyEmail' : data.COMPANY.companyEmail,
	            		'companyPhonePrimary' : data.COMPANY.companyPhonePrimary,
	            		'companyPhoneSecondary' : data.COMPANY.companyPhoneSecondary,
	            		'companyUniqueCode' : data.COMPANY.companyUniqueCode
    				};
    				var $viewCompanyForm = $('#viewCompanyForm');
		            var $viewCompanyModal = $('#viewCompanyModal');
		            buildViewForm($viewCompanyForm, dataMap, $viewCompanyModal);
		            $viewCompanyModal.modal('show');
    			}catch (e) {
            		console.log(e);
            		hideloader();
				}
    		},
    		complete:function(data){
                // Hide loading image container
           	 hideloader();
           }
		});
	});
	
	$('.deleteCompanyBtn').on('click', function(){
		var companyid = $(this).data('companyid');
		var companyname = $(this).data('companyname');
		
		swal({
            title: "Are you sure, you want to delete " + companyname + "?",
            //text: "You will not be able to recover this imaginary file!",
            type: "warning",
            showCancelButton: true,
            confirmButtonColor: "#DD6B55",
            confirmButtonText: "Yes, delete it!",
            closeOnConfirm: false
        }, function(){
        	$.ajax({
                url:contextRoot+'master/company/deleteCompany',
                type:'GET',
                dataType:'html',
                data:'companyid='+companyid,
        		async:false,
        		beforeSend: function(){
        		    // Show loading image container
        		    showloader();
        		},
        		success:function(data) {
                	try{
		               if(data != undefined){
		            	   try{
		            		   swal("Deleted!", "Company " + companyname + " has been deleted.", "success");
			                	setTimeout(function(){// wait for 2 secs
			                        location.reload(); // then reload the page.
			                   }, 1000);
		                	}catch (e) {
		                		hideloader();
							}
		               }else{
		            	   swal("Error!", "Error occurred while deleting an distributer.", "error");
		               }
                	}catch (e) {
                		console.log(e);
                		hideloader();
    				}
                },
                complete:function(data){
                    // Hide loading image container
               	 hideloader();
               }
        	});
        });
	});
	
});