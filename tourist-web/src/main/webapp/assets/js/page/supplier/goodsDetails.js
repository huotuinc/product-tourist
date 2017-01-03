/**
 * Created by Administrator slt 2016/12/30.
 */
$(function(){
    dateRangeEdit($('input[name="toursitDate"]'));
    bindDelteTouristDate();
    setRichText();
    showDiscountInfo();
    showRebateInfo();

});

var showDiscountInfo=function(){
    $("#discountInfo").on("click",function(){
        var that = this;
        layer.tips('无儿童价则此项不填，如果有则填入折扣值1-10，如：8折，则填入8即可。则前台收取儿童费用为成人价格的8折每位，不得大于10',that,{
            tips: [1, '#2F4056'],
            time: 4000
        });
    });
};

var showRebateInfo=function(){
    $("#rebateInfo").on("click",function(){
        var that = this;
        layer.tips('按路线价格的比例返佣比例。0-100。不得大于100',that,{
            tips: [1, '#2F4056'],
            time: 4000
        });
    });
};

var dateRangeEdit=function($ele){
    $ele.daterangepicker({
        locale: {
            format: 'YYYY-MM-DD hh:mm:ss',
            cancelLabel:'删除'
        },
        singleDatePicker: true,
        showDropdowns: true,
        drops: "up",
        autoApply: true
    });
};

var addTouristDate=function(){
    $(".goodsTouristDates").append('<div> <input type="hidden" name="routeId"/> ' +
        '<input  type="text" class="form-control datePicker"' +
        ' name="toursitDate" placeholder="出行时间"/> ' +
        '<button type="button" class="btn-lit">删除</button> </div>');

    dateRangeEdit($('input[name="toursitDate"]'));

};

var bindDelteTouristDate=function(){
    $(".goodsTouristDates").on("click","button",function(){

        var div=$(this).parent();
        if(!checkStringIsEmpty($("input[name='routeId'][type='hidden']",div).val())){
            //请求服务器删除行程
            layer.confirm('确定删除吗？', {
                btn: ['确定', '取消']
            }, function (index) {
                $.ajax({
                    type:'POST',
                    url: '',
                    dataType: 'json',
                    data: {id:id},
                    success:function(result){
                        layer.msg("删除成功！");
                    },
                    error:function(e){
                        layer.msg("删除出错！");
                    }
                });

                layer.close(index);
            });
        }else {
            $(div).remove();
        }

    });
};


var checkStringIsEmpty=function(str){
    if(str==undefined||str==null||str==""){
        return true;
    }else {
        return false;
    }
};

var setRichText=function(){
    $(".summernote").summernote({
        lang: "zh-CN",
        onImageUpload: function(files, editor, welEditable) {
            sendFile(files[0],editor,welEditable);
        }
    })

};

function sendFile(file,editor,welEditable) {
    data = new FormData();
    data.append("fileImage", file);
    $.ajax({
        data: data,
        type: "POST",
        url: "/resource/uploadMessageImage",
        cache: false,
        contentType: false,
        processData: false,
        success: function(data) {
            editor.insertImage(welEditable, data.url);
        }
    });
}


var uploadImage=function(){
    var loadPic=layer.load(0, {shade: false});
    $.ajaxFileUpload({
        url: '/resource/uploadMessageImage',
        secureuri: false,
        fileElementId: 'fileImage',
        dataType: 'json',
        data: null,
        success: function(resultModel) {
            if(resultModel.code==1){
                layer.close(loadPic);
                layer.msg("上传成功");
                $(".image-crop img").attr("src",resultModel.url);
            }
        },
        error: function(data, status, e) {
            layer.close(loadPic);
            layer.msg("上传失败，请检查网络后重试"+e);
        }
    });
};

//提交
var submitForm=function(obj) {
    if($(obj).attr("class")=="btn btn-primary disabled"){
        return;
    }
    var messageModel={};
    messageModel.id=$("input[name='messageId']").val();
    messageModel.title=$("input[name='title']").val();
    messageModel.customerId=/*[[${message.customerId}]]*/ '';
    messageModel.putAway=$("input[name='putAway']:checked").val();
    messageModel.pictureUrl=$("#pictureUrl").attr("src");
    messageModel.summary=$("input[name='summary']").val();
    messageModel.content=$(".summernote").code();
    messageModel.date=new Date();


    var ld=layer.load(5, {shade: false});
    $(obj).attr("class","btn btn-primary disabled");

    $.ajax({
        type:'POST',
        url: '/back/saveMessage',
        dataType: 'json',
        contentType:"application/json",
        data: JSON.stringify(messageModel),
        success:function(result){
            layer.close(ld);
            layer.msg("保存成功！");
//                    $(obj).attr("class","btn btn-primary");
            window.setTimeout("window.location='/back/showMessageList'",1000);
        },
        error:function(e){
            layer.close(ld);
            layer.msg("保存失败！");
            $(obj).attr("class","btn btn-primary");
        }
    });
};