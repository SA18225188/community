$(function(){
    $("#uploadForm").submit(upload);
});

function upload() {
    $.ajax({
        url: "http://upload.qiniup.com",//七牛云
        method: "post",
        processData: false,//必要
        contentType: false,//必要
        data: new FormData($("#uploadForm")[0]),
        success: function(data) {
            if(data && data.code == 0) {
                // 更新头像访问路径
                $.post(
                    //访问路径
                    CONTEXT_PATH + "/user/header/url",
                    //传给后台的参数
                    {"fileName":$("input[name='key']").val()},
                    function(data) {
                        data = $.parseJSON(data);
                        if(data.code == 0) {
                            console.log(data);
                            window.location.reload();
                        } else {
                            alert(data.msg);
                        }
                    });
            } else {
                alert("上传失败!");
            }
        }
    });
    return false;
}