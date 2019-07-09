$(function(){
	
	buildNavPath({'Home':'user/home', 'Document Tracking List' : 'documentTrackingList'});
	
});


$('#statusSelect').change(function(){
	var statusId = $('.statusName').val();
	console.log(statusId);
	$.ajax({
		url: contextRoot + 'documents/documentTrackingController/getListByStatus',
		type: 'GET',
		data: 'statusId='+statusId,
		success: function(data){
			console.log(data);
			$('.ibox-content').html("");
			$('.ibox-content').append(data);
		}
	});
});