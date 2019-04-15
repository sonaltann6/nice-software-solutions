var table;
$(function(){
	//Compulsory first line should be buildNavPath **
	buildNavPath({'Home':'user/home', 'PO':'#', 'Place Order':'#'});
	
	/*chosen-select*/
	$('.chosen-select').chosen({
		width: "100%",
		max_selected_options: 3
	});
	
	//Data table
	table = $('.dataTables-example').DataTable({
        responsive: true,
        searching: false, paging: false, info: false,
        dom: '<"html5buttons"B>lTfgitp',
        columnDefs: [{
                targets: 5,
                className: 'text-center'
            }],
        buttons: [{
                text: 'Add Entry',
                className: 'addPORowBtn',
                /*action: function ( e, dt, node, config ) {
                    //alert( 'Button activated' );
                }*/
            }]
    });
	
	
	//Add PO row button
	$('body').on('click', '.addPORowBtn', function() {
		var newRowNumber = 0;
		if(table.rows().count() > 0){
			//debugger
			newRowNumber = parseInt($.parseHTML(table.rows(table.rows().count() - 1).data()[0][1])[0].name.replace('poItemsList[', '').replace('].poItemNumber', '')) + 1; 
		}
		
		var product_model_type_options = $('.PRODUCT_MODEL_TYPE_LIST_DUMMY_12345').html();
		var enquiry_number_options = $('.ENQUIRY_NUMBER_LIST').html();
		table.row.add([
			/*'<span id="showIcon" class="field-tip form-control enquiry-details" style="display: none;"><i class="fa fa-eye"></i><span class="tip-content" id="enquiry_id"></span></span>',*/
			'<div class="btn-group"><button data-toggle="dropdown" class="btn btn-warning btn-sm dropdown-toggle btn-xs" aria-expanded="true">Details<span class="caret"></span></button><ul class="dropdown-menu dropdown-menu-'+newRowNumber+'"></ul>',
			'<select data-row-number="'+newRowNumber+'" id="poItemsList'+newRowNumber+'.enquiryNumber" name="poItemsList['+newRowNumber+'].enquiryNumber" class="form-control invisible-text-input poItemsList-enquiryNumber" style="width:100%" required="required" onChange="poItemsListEnquiryNumberChange(this)">'+ 
				enquiry_number_options +
			'</select>',
            '<input type="text" data-row-number="'+newRowNumber+'" id="poItemsList'+newRowNumber+'.poItemNumber" name="poItemsList['+newRowNumber+'].poItemNumber" class="form-control invisible-text-input" style="width:100%" required="required">',
            '<input type="text" data-row-number="'+newRowNumber+'" id="poItemsList'+newRowNumber+'.poItemDesc" name="poItemsList['+newRowNumber+'].poItemDesc" class="form-control invisible-text-input" style="width:100%">',
            '<input type="" data-row-number="'+newRowNumber+'" id="poItemsList'+newRowNumber+'.productModelTypeId" name="poItemsList['+newRowNumber+'].productModelType.modelTypeId" class="form-control invisible-text-input" style="width:100%;display:none;">',
            '<input type="text" data-row-number="'+newRowNumber+'" id="poItemsList'+newRowNumber+'.productModelType"  class="form-control invisible-text-input" style="width:100%" required="required">',
            '<input type="text" data-row-number="'+newRowNumber+'" id="poItemsList'+newRowNumber+'.poItemQty" name="poItemsList['+newRowNumber+'].poItemQty" class="form-control invisible-text-input poItemQty" style="width:100%" required="required">',
            '<input type="text" data-row-number="'+newRowNumber+'" id="poItemsList'+newRowNumber+'.poItemRate" name="poItemsList['+newRowNumber+'].poItemRate" class="form-control invisible-text-input poItemRate" style="width:100%" required="required" onclick="" readonly="readonly">',
            '<input type="text" data-row-number="'+newRowNumber+'" id="poItemsList'+newRowNumber+'.poItemAmount" name="poItemsList['+newRowNumber+'].poItemAmount" class="form-control invisible-text-input" style="width:100%" readonly="readonly">',
            
            '<button type="button" class="btn btn-warning btn-xs addPORowBtn"> <i class="fa fa-plus"></i> </button>'+
            '<button type="button" class="btn btn-warning btn-xs removePORowBtn"> <i class="fa fa-minus"></i> </button>'
        ]).draw( false );
		rearrangeTableRowsIndexes();
		$('.table-responsive').css('overflow','visible');
	});
		
	//Remove PO row button
	$('body').on('click', '.removePORowBtn', function() {
		if(table.row($(this).closest('tr')).index() != 0){	//Not a first row
			table.row($(this).closest('tr')).remove().draw( false );
			rearrangeTableRowsIndexes();
        }else{
        	alert('You can not delete this row');
        }
	});
	
	//Generate first row on page load
	$('.addPORowBtn').trigger('click');
	
	//Adjust text field width according typed text
	/*$('.invisible-text-input').on('keypress', function() {
		this.style.width = ((this.value.length + 1) * 8) + 'px';
	});*/
	
	//Skip po info checkbox
	$('#checkbox-skip-po-info').on('change', function() {
		if($(this). prop("checked") == true){
			//Disable the fields
			$('.po-table-fields').val('').attr('disabled', 'disabled');
			$('.addPORowBtn').attr('disabled', 'disabled');
			$('.removePORowBtn').attr('disabled', 'disabled');
			
			//Remove table rows excluding first row
			/*for(i=table.rows().count(); i>0; i--){
				table.row( i ).remove().draw( false );
			}*/
			
			//Remove all table rows
			table
				.rows()
			    .remove()
			    .draw();
		}else{
			//Enable the fields
			$('.po-table-fields').val('').removeAttr('disabled');
			$('.addPORowBtn').removeAttr('disabled');
			$('.removePORowBtn').removeAttr('disabled');
		}
	});
	
	
	
	/*//tooltip on option
	$('.dropdown-menu').hover(function (){
		var enquiryId = $('.poItemsList-enquiryNumber').val();
		//alert(enquiryId);
		$.ajax({
			url: contextRoot+'rest/enquiry/getEnquiryBean',
			type: 'GET',
			data: 'enquiryId='+enquiryId,
			success: function(data){
				console.log(data.ENQUIRY_HISTORY_LIST.productModelType.modelTypeName);
				//$("#enquiry-list").append(JSON.stringify(data));
				if($('.hover-tbl').length > 0) {
					$('.hover-tbl').remove();
				}
				$('.dropdown-menu').append('<li><a href="#">Enquiry details</a></li>'
						
						
						
						
						'<table class="hover-tbl" width="100px" height="50px">'+
										'<tr>'+'<td>Product Type : '+data.ENQUIRY_HISTORY_LIST.productType.productTypeName+ '</td>'+'</tr>'+
										'<tr>'+'<td>Model Type : '+data.ENQUIRY_HISTORY_LIST.productModelType.modelTypeName+ '</td>'+'</tr>'+
										'<tr>'+'<td>Surface Type : '+data.ENQUIRY_HISTORY_LIST.productSurfaceType.surfaceTypeName+ '</td>'+'</tr>'+
										'<tr>'+'<td>Length : '+data.ENQUIRY_HISTORY_LIST.surfaceLength+ '</td>'+'</tr>'+
										'<tr>'+'<td>Width : '+data.ENQUIRY_HISTORY_LIST.surfaceWidth+ '</td>'+'</tr>'+
										'<tr>'+'<td>Height : '+data.ENQUIRY_HISTORY_LIST.surfaceHeight+ '</td>'+'</tr>'+
										'<tr>'+'<td>SWL : '+data.ENQUIRY_HISTORY_LIST.swl+ '</td>'+'</tr>'+
										'<tr>'+'<td>Fabric Type : '+data.ENQUIRY_HISTORY_LIST.fabricType.fabricTypeName+ '</td>'+'</tr>'+
										'<tr>'+'<td>Fabric GSM : '+data.ENQUIRY_HISTORY_LIST.fabricGSMValue+ '</td>'+'</tr>'+
										'<tr>'+'<td>Loop Type : '+data.ENQUIRY_HISTORY_LIST.loopType.loopTypeName+ '</td>'+'</tr>'+
										'<tr>'+'<td>Loop Material : '+data.ENQUIRY_HISTORY_LIST.loopMaterial.loopMaterialName+ '</td>'+'</tr>'+
										'<tr>'+'<td>Top Filling Type : '+data.ENQUIRY_HISTORY_LIST.topFillingType.topFillingTypeName+ '</td>'+'</tr>'+
										'<tr>'+'<td>Top Type : '+data.ENQUIRY_HISTORY_LIST.topType.topTypeName+ '</td>'+'</tr>'+
										'<tr>'+'<td>Bottom Type : '+data.ENQUIRY_HISTORY_LIST.bottomType.bottomTypeName+ '</td>'+'</tr>'+
										'<tr>'+'<td>Shipment Type : '+data.ENQUIRY_HISTORY_LIST.shipmentType.shipmentTypeName+ '</td>'+'</tr>'+
									 '</table>'
				);
			}
		
		});
	});*/
	
	/*$('.poItemQty, .poItemRate').on('keyup', function(){
		debugger
		var thisRowNumber = parseInt($(this).data('row-number'));
		calculateAmount(thisRowNumber);
	});*/
	
	$(document).on('keyup', '.poItemQty, .poItemRate', function(){
		var thisRowNumber = parseInt($(this).data('row-number'));
		calculateAmount(thisRowNumber);
	});
	
	/*$('.poItemRate').on('keyup', function(){
		calculateAmount
	});*/
	
	
	
	//**important function**
	function rearrangeTableRowsIndexes(){
		var rowCount = table.rows().count();	//Total rows
		
		for(var i=0; i<rowCount; i++){
			var columnCount = table.rows(i).data()[0].length - 1;	//Total columns in current row
			
			for(var j=1; j<columnCount; j++){
				var oldName = $.parseHTML(table.rows(i).data()[0][j])[0].name;
				var oldId = oldName.replace('[', '').replace(']', '');
				
				var newName = oldName.replace(/\[([0-9]|[1-8][0-9]|9[0-9]|[1-8][0-9]{2}|9[0-8][0-9]|99[0-9]|1000)\]/, "["+i+"]");	//0-1000 range
				var newId = newName.replace('[', '').replace(']', '');
				
				$('#'+oldId.replace('.', '\\.')).attr('name', newName);
				$('#'+oldId.replace('.', '\\.')).attr('id', newId);
				
			}
		}
	}
	
});

function calculateAmount(rowNumber){
	var qty = parseFloat($('#poItemsList'+rowNumber+'\\.poItemQty').val());
	var rate = parseFloat($('#poItemsList'+rowNumber+'\\.poItemRate').val());
	var $amount = $('#poItemsList'+rowNumber+'\\.poItemAmount');
	
	if(isNaN(qty)){
		qty = 0.0;
	}
	
	if(isNaN(rate)){
		rate = 0.0;
	}
	
	var calculated_amount = qty * rate;
	$amount.val(calculated_amount);
	
	//Calculate Total Amount
	calculateTotalAmount();
}

function calculateTotalAmount(){
	var rowCount = table.rows().count();
	var $totalAmount = $('#poTotalAmount');
	var totalAmount = 0.0;
	
	for(var rowNumber=0; rowNumber<rowCount; rowNumber++){
		var thisAmount = $('#poItemsList'+rowNumber+'\\.poItemAmount').val();
		if(isNaN(thisAmount)){
			thisAmount = 0.0;
		}
		totalAmount += parseFloat(thisAmount); 
	}
	
	$totalAmount.val(totalAmount);
}


//ENQUIRY_NUMBER_LIST on change
function poItemsListEnquiryNumberChange(obj){
	console.log(obj);
	var enquiryId = $(obj).val();
	var rowNumber = $(obj).data('row-number');
	//alert(enquiryId);
	$.ajax({
		url: contextRoot+'enquiry/globalTemplateController/getEnquiryBean',
		type: 'GET',
		data: 'enquiryId='+enquiryId,
		success: function(data){
			console.log(data);
			if(data.ENQUIRY_HISTORY_LIST.topFilling == true){
				var topFillingType = data.ENQUIRY_HISTORY_LIST.topFillingType.topFillingTypeName;
				var topType = data.ENQUIRY_HISTORY_LIST.topType.topTypeName;
			}else{
				var topFillingType = 'none';
				var topType = 'none';
			}
			if(data.ENQUIRY_HISTORY_LIST.bottomDischarge == true){
				var bottomDischargeType = data.ENQUIRY_HISTORY_LIST.bottomDischargeType.bottomDischargeTypeName;
				var bottomType = data.ENQUIRY_HISTORY_LIST.loopMaterial.loopMaterialName;
			}else{
				var bottomDischargeType = 'none';
				var bottomType = 'none';
			}
			$('.dropdown-menu-'+rowNumber).html('');
			$('.dropdown-menu-'+rowNumber).append('<li><a href="#">Product Type : '+data.ENQUIRY_HISTORY_LIST.productType.productTypeName+ ',  Model Type : '+data.ENQUIRY_HISTORY_LIST.productModelType.modelTypeName+ ',  Surface Type : '+data.ENQUIRY_HISTORY_LIST.productSurfaceType.surfaceTypeName+'</a></li>'+
									   '<li><a href="#">Length : '+data.ENQUIRY_HISTORY_LIST.surfaceLength+ ',  Width : '+data.ENQUIRY_HISTORY_LIST.surfaceWidth+ ',  Height : '+data.ENQUIRY_HISTORY_LIST.surfaceHeight+ '</a></li>'+
									   '<li><a href="#">SWL : '+data.ENQUIRY_HISTORY_LIST.swl+ ',  Fabric Type : '+data.ENQUIRY_HISTORY_LIST.fabricType.fabricTypeName+ ',  Fabric GSM : ' +data.ENQUIRY_HISTORY_LIST.fabricGSMValue+ '</a></li>'+
									   '<li><a href="#">Loop Type : '+data.ENQUIRY_HISTORY_LIST.loopType.loopTypeName+ ',  Loop Material : '+data.ENQUIRY_HISTORY_LIST.loopMaterial.loopMaterialName+ ',  Shipment Type : '+data.ENQUIRY_HISTORY_LIST.shipmentType.shipmentTypeName+ '</a></li>'+
									   '<li><a href="#">Top Filling Type : '+topFillingType+ ',  Top Type : '+topType+ '</a></li>'+
									   '<li><a href="#">Bottom Filling Type : ' +bottomDischargeType+ ',  Bottom Type : '+bottomType+ '</a></li>'
									   
			);
			$("#poItemsList"+rowNumber+"\\.productModelTypeId").val(data.ENQUIRY_HISTORY_LIST.productModelType.modelTypeId);
			$("#poItemsList"+rowNumber+"\\.productModelType").val(data.ENQUIRY_HISTORY_LIST.productModelType.modelTypeName);
			$("#poItemsList"+rowNumber+"\\.poItemDesc").val("Product Type : " +data.ENQUIRY_HISTORY_LIST.productType.productTypeName +"Model Type : " +data.ENQUIRY_HISTORY_LIST.productModelType.modelTypeName +"Surface Type : " +data.ENQUIRY_HISTORY_LIST.productSurfaceType.surfaceTypeName 
																				   +"Length :" +data.ENQUIRY_HISTORY_LIST.surfaceLength +"Width :" +data.ENQUIRY_HISTORY_LIST.surfaceWidth +"Height :" +data.ENQUIRY_HISTORY_LIST.surfaceHeight
																				   +"SWL :" +data.ENQUIRY_HISTORY_LIST.swl +"Fabric Type :" +data.ENQUIRY_HISTORY_LIST.fabricType.fabricTypeName +" Fabric GSM :" +data.ENQUIRY_HISTORY_LIST.fabricGSMValue
																				   +"Loop Type :" +data.ENQUIRY_HISTORY_LIST.loopType.loopTypeName +"Loop Material :" +data.ENQUIRY_HISTORY_LIST.loopMaterial.loopMaterialName +"Shipment Type :" +data.ENQUIRY_HISTORY_LIST.shipmentType.shipmentTypeName
																				   +"Top Filling Type :" +topFillingType +"Top Type :" +topType
																				   +"Bottom Filling Type :" +bottomDischargeType +"Bottom Type :" +bottomType);
			$("#poItemsList"+rowNumber+"\\.poItemQty").val(data.ENQUIRY_HISTORY_LIST.shipmentQuantity);
			$("#poItemsList"+rowNumber+"\\.poItemRate").val(data.ENQUIRY_HISTORY_LIST.totalBagCost / data.ENQUIRY_HISTORY_LIST.shipmentQuantity);
			calculateAmount(rowNumber);
			/*$('#showIcon').show();*/
			//$("#enquiry-list").append(JSON.stringify(data));
		}
	});
}