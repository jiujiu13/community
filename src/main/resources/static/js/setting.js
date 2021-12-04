$(function () {
    //没填信息前设置注册键不能使用
    $("#btn_update").attr("disabled", "disabled");

    //当某一个组件失去焦点是，调用对应的校验方法
    $("#old-password").blur(checkOldPassword);
    $("#new-password").blur(checkNewPassword);
    $("#confirm-password").blur(checkConfirmPassword);

});


//检查是否所有信息都填了
function check() {
    var newPassword = $("#new-password").val();
    //在客户端浏览器debug出来的结果！！！
    var span_newPassword = $("#span_newPassword")[0].innerHTML;
    var oldPassword = $("#old-password").val();
    var span_oldPassword = $("#span_oldPassword")[0].innerHTML;
    var passwordConfirm = $("#confirm-password").val();
    var span_passwordConfirm = $("#span_newPasswordConfirm")[0].innerHTML;
    //所有项都要填并且span标签下的值必须全为"",里面不能有值
    if ( newPassword != "" && oldPassword != "" && passwordConfirm != "" &&  span_newPassword == "" && span_oldPassword == "" && span_passwordConfirm == "" && newPassword== passwordConfirm) {
        $("#btn_update").removeAttr("disabled");
    } else {
        $("#btn_update").attr("disabled", "disabled");
    }
}

function checkOldPassword(){
    var oldPassword = $("#old-password").val();
    if (oldPassword != null || oldPassword != "") {
        $.get("/check/oldPassword", {oldPassword:oldPassword}, function (data) {
            data = $.parseJSON(data);
            if (data.code==0) {
                $("#span_oldPassword").html("");
                check();
            } else {
                $("#span_oldPassword").html("密码不正确");
                check();
            }
        });
    }

    check();
}
function checkNewPassword(){
    //1.获取密码值
    var newPassword = $("#new-password").val();

    //2.定义正则
    var reg_password = /^\w{3,20}$/;


    //3.判断，给出提示信息
    var flag = reg_password.test(newPassword);
    if (flag) {
        //密码合法
        $("#span_newPassword").html("");
        check();
    } else {
        //密码非法
        $("#span_newPassword").html("密码该为3-20位");
        check();
    }

    check();


}
function checkConfirmPassword(){
    var newPassword= $("#new-password").val();
    var passwordConfirm = $("#confirm-password").val();
    if (passwordConfirm == null || passwordConfirm == "") {
    }
    //判断，给出提示信息
    if (newPassword == passwordConfirm) {
        //一致
        $("#span_newPasswordConfirm").html("");
        check();
    } else {
        //密码不一致
        $("#span_newPasswordConfirm").html("密码不一致");
        check();
    }
    check();

}


function upload() {
    var head = $("#head-image").val();
   if(head!="") {
       $.ajax({
           url: "http://upload-z1.qiniup.com",
           method: "post",
           processData: false,
           contentType: false,
           data: new FormData($("#uploadForm")[0]),
           success: function (data) {
               if (data && data.code == 0) {
                   // 更新头像访问路径
                   $.post(
                       CONTEXT_PATH + "/user/header/url",
                       {"fileName": $("input[name='key']").val()},
                       function (data) {
                           data = $.parseJSON(data);
                           if (data.code == 0) {
                               window.location.reload();
                           } else {
                               alert(data.msg);
                           }
                       }
                   );
               } else {
                   alert("上传失败!");
               }
           },

       });
   }
    return false;
}