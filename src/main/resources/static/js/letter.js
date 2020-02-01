$(function(){
	$("#sendBtn").click(send_letter);
	$(".close").click(delete_msg);
});


function send_letter() {
	// 发送框隐藏
	$("#sendModal").modal("hide");
	var toName = $("#recipient-name").val();
	var content = $("#message-text").val()
	//异步发送一个post请求
	$.post(
		CONTEXT_PATH + "/letter/send",
		{"toName":toName, "content":content},
		function (data) {
			data = $.parseJSON(data);
			if (data.code == 0){
				//通过id过去提示框，改变提示框内容
				$("#hintBody").text("发送成功");
			} else {
				$("#hintBody").text(data.msg);
			}

			//显示提示框
			$("#hintModal").modal("show");
			setTimeout(function(){
				$("#hintModal").modal("hide");
				//当前页面刷新，也就是重载
				location.reload();
			}, 2000);
		}
	);
}

function delete_msg() {
	// TODO 删除数据
	$(this).parents(".media").remove();
}