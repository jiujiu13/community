$(function () {

    var strFullPath=window.document.location.href;
    var strPath=window.document.location.pathname;
    var pos=strFullPath.indexOf(strPath);
    var prePath=strFullPath.substring(0,pos);

    $.get("/notice","",function (data){
        data = $.parseJSON(data);
        if(data.code == 0) {
            if(data.unreadFollow==0){
                $("#a_follow").hide()
            }else {
                $("#span_follow").html(data.unreadFollow);
            }
            if(data.unreadComment==0){
                $("#a_comment").hide()
            }else {
                $("#span_comment").html(data.unreadComment);
            }
            if(data.unreadLike==0){
                $("#a_like").hide()
            }else {
                $("#span_like").html(data.unreadLike);
            }
            if(data.unreadFollow!=0 || data.unreadComment!=0 || data.unreadLike!=0){
                $("#img_notice").attr('src',prePath+"/img/havenotice.png");
            }else {
                $("#img_notice").attr('src',prePath+"/img/nonotice.png");
            }
        } 
    });

});