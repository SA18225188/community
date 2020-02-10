$(function(){
	$("#publishBtn").click(publish);
});

function publish() {
	// 把发布框隐藏
	$("#publishModal").modal("hide");
	// 发送AJAX请求之前,将CSRF令牌设置到请求的消息头中.
	//太麻烦了，因为有很多地方涉及到异步请求，这里仅供演示
   // var token = $("meta[name='_csrf']").attr("content");
   // var header = $("meta[name='_csrf_header']").attr("content");
   // $(document).ajaxSend(function(e, xhr, options){
   //     xhr.setRequestHeader(header, token);
   // });


	// 发布异步请求
	// 获取title和content
	var title = $("#recipient-name").val();
	var content = $("#message-text").val();
	// 发送异步请求（post）
	//以下是一个常见过程
	$.post(
		CONTEXT_PATH + "/discuss/add",
		{"title":title, "content":content},
		function (data) {
			data = $.parseJSON(data);
			//提示框提示返回信息
			$("#hintBody").text(data.msg);
			//显示提示框
			$("#hintModal").modal("show");
			//2秒后自动隐藏提示框
			setTimeout(function () {
				$("#hintModal").modal("hide");
				//刷新页面
				if (data.code == 0){
					window.location.reload();
				}
			}, 2000)
		}
	)

	// 显示提示框
	$("#hintModal").modal("show");
	setTimeout(function(){
		$("#hintModal").modal("hide");
	}, 2000);
}