$(function(){
	
	buildNavPath({'Home':'user/home', 'FIBC Costing' : 'fibcCosting'});
	
	$(function(){
		var userId = $('select[name="userId"] option:selected').val();
		$.ajax({
			url : contextRoot + 'fibc/costing/getByUser',
			type : 'GET',
			data: 'userId='+userId,
			success : function(data){
				var itemList = data;
				console.log(itemList);
				itemList.forEach(function(item){
					$("."+item.itemId).val(item.itemCost);
					$(".srNo_"+item.itemId).val(item.srNo);
				});
			}
		});
	});
	
	$('.submitValuesbtn').on('click', function(){
		var itemListArray = [];
		var userId = $('select[name="userId"] option:selected').val();
		try{
			$('.fibcCostingTable').find('tbody').find('tr').each(function(el){
				console.log(el);
				var index = parseInt(el);
				if(el>=0){
					//debugger
					var itemId=$('.itemId_'+index)[0].innerText;
					var itemName=$('.itemName_'+index)[0].innerText;
					var costUnit=$('.costUnit_'+index)[0].innerText;
					var itemCost=$('.itemCost_'+index).val();
					var srNo=$(".srNo_"+itemId).val();
					
					var itemObj={
							'itemId':itemId,
							'itemName':itemName,
							'costUnit':costUnit,
							'itemCost':itemCost,
							'srNo':srNo,
							'userId': {
								"userId": userId
							}
					};
					
					if(itemObj != null){
						itemListArray.push(itemObj);
					}
					
					//console.log('itemObj : ' + JSON.stringify(itemObj));
				}
				//console.log('itemObj : ' + JSON.stringify(itemListArray));
			});
			
			$.ajax({
		          url: contextRoot + "fibc/costing/saveFibcCost",
		          type: 'POST',
		          data: JSON.stringify(itemListArray),
		          contentType: "application/json",
		         // data: 'userId' + $("#myselect").val(),
		          success: function(data){
		        	  alert('saved successfully');
		        	  window.location.reload(false);
		          }
			 });
		}catch(e){
			console.log('exception : ' + e);
		}
		
	});
	
});

