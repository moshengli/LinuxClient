function submit(){
    $.ajax({
        url: '/cmd',
        type: 'post',
        data: {
            cmd: $("#input").val()
        },
        success: function (res) {
            // 只有请求成功（状态码为200）才会执行这个函数
            alert(res)
            document.getElementById("input").value="";
        },
        error: function (xhr) {
            // 只有请求不正常（状态码不为200）才会执行
            console.log('error', xhr);
        },
    });
}
$(function(){
    $('#input').bind('keypress',function(event){
        if(event.keyCode == "13")
        {
            console.log($("#input").val())
            submit();
        }
     });
});
