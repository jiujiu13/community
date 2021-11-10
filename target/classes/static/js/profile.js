
function follow(btn,userId) {


	var entityId = $(btn).prev().val();

	if ($(btn).hasClass("btn-info")) {

		// 关注TA
		$.ajax({
			url:CONTEXT_PATH + "/follow",
			async:false,
			type:'post',
			datatype:'json',
			data: {"entityType": 3, "entityId": entityId},
			success: function (data) {
				data = $.parseJSON(data);
				if (data.code == 0) {
					$.ajax({
						url: CONTEXT_PATH + "/user/profile/ajax/"+userId,
						async:false,
						type:'get',
						datatype:'json',
						data:null,
						success:function(data){
							data = $.parseJSON(data);
							if (data.code == 0) {
								$("#follower").html(data.followerCount);
								$(btn).removeClass("btn-info").addClass("btn-secondary");
								$(btn).text("已关注");
							} else {
								alert(data.msg);
							}
						},
					});
				} else {
					alert(data.msg);
				}
			}
		});

	} else {
		// 取消关注
		$.post(
			CONTEXT_PATH + "/unfollow",
			{"entityType": 3, "entityId": entityId},
			function (data) {
				data = $.parseJSON(data);
				if (data.code == 0) {
					$.ajax({
						url: CONTEXT_PATH + "/user/profile/ajax/"+userId,
						async:false,
						type:'get',
						datatype:'json',
						data:null,
						success:function(data){
							data = $.parseJSON(data);
							if (data.code == 0) {
								$("#follower").html(data.followerCount);
								$(btn).removeClass("btn-secondary").addClass("btn-info");
								$(btn).text("关注TA");
							} else {
								alert(data.msg);
							}
						},
					});
				} else {
					alert(data.msg);
				}
			}
		);

	}
}