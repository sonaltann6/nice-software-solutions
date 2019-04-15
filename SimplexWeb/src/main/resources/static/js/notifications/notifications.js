/**
 * 
 */

$(function(){
	
	buildNavPath({'Home':'user/home', 'Notification' : 'notifications'});
	
});
function readNotif(notifId){
	
	//alert("hi! notifId : " + notifId);
	$.ajax({
		url: contextRoot+'notification/notificationsController/getReadNotification',
		type: 'GET',
		data: 'notificationId='+notifId,
		success: function(data){
			//console.log(data);
			//console.log(data.NOTIFICATION_LIST.entityTable.onClickUrl);
			window.location.href = data.NOTIFICATION_LIST.entityTable.onClickUrl;
		}
	});
}